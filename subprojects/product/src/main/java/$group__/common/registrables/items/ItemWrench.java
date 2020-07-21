package $group__.common.registrables.items;

import $group__.common.registrables.items.groups.ItemGroupsThis;
import $group__.common.registrables.utilities.helpers.RegistrableUtilities.BlockUtilities;
import $group__.common.registrables.utilities.helpers.RegistrableUtilities.NBTs;
import $group__.common.registrables.utilities.helpers.RegistrableUtilities.RayTraceResults;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

import static $group__.Globals.LOGGER;
import static $group__.utilities.specific.Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.utilities.specific.Optionals.unboxOptional;

public class ItemWrench extends Item {
	protected ItemWrench() {
		super(new Item.Properties().group(ItemGroupsThis.DEFAULT).maxStackSize(1));
	}


	protected static boolean use(ItemStack stack, World world, RayTraceResult target) {
		Tag tag = new Tag(stack.getTag());
		switch (target.getType()) {
			case BLOCK:
				BlockRayTraceResult targetBlock = (BlockRayTraceResult) target;
				if (tag.pickedUpBlock != null) {
					BlockPos pos = BlockUtilities.getPlacePosition(targetBlock);
					assert tag.pickedUpBlockState != null;
					BlockState state = Block.getStateById(tag.pickedUpBlockState);
					if (!world.setBlockState(pos, state)) {
						LOGGER.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Cannot create block state ID {}"
								, tag.pickedUpBlockState));
						return false;
					} else if (tag.pickedUpBlockTile != null) {
						@Nullable TileEntity tile = state.getBlock().createTileEntity(state, world);
						if (tile == null) {
							LOGGER.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Cannot create tile entity of block state ID {}", tag.pickedUpBlockState));
							return false;
						}
						tile.deserializeNBT(tag.pickedUpBlockTile);
						world.setTileEntity(pos, tile);
						tag.pickedUpBlockTile = null;
					}
					tag.pickedUpBlockState = null;
					stack.setTag(tag.serializeNBT());
				} else if (tag.pickedUpEntity != null) {
					@Nullable LivingEntity entity = unboxOptional(EntityType.loadEntityUnchecked(tag.pickedUpEntity, world).filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e));
					if (entity == null) {
						LOGGER.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Cannot create entity with tag '{}'", tag.pickedUpEntity));
						return false;
					}
					BlockPos targetPos = BlockUtilities.getPlacePosition(targetBlock);
					entity.setPosition(targetPos.getX(), targetPos.getY(), targetPos.getZ());
					world.addEntity(entity);
					tag.pickedUpEntity = null;
				} else {
					BlockPos pos = targetBlock.getPos();
					BlockState state = world.getBlockState(pos);
					@Nullable TileEntity tile = state.getBlock().hasTileEntity(state) ? world.getTileEntity(pos) : null;
					tag.pickedUpBlockState = Block.getStateId(state);
					if (tile != null) {
						tag.pickedUpBlockTile = tile.serializeNBT();
						world.removeTileEntity(pos);
					}
					stack.setTag(tag.serializeNBT());
					world.removeBlock(pos, false);
				}
				stack.setTag(tag.serializeNBT());
				break;
			case ENTITY:
				EntityRayTraceResult targetEntity = (EntityRayTraceResult) target;
				LivingEntity entity = (LivingEntity) targetEntity.getEntity();
				tag.pickedUpEntity = entity.serializeNBT();
				stack.setTag(tag.serializeNBT());
				entity.remove(false);
				break;
			default:
				throw BecauseOf.unexpected();
		}
		return true;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	protected static boolean canUse(ItemStack stack, World world, @Nullable LivingEntity user, RayTraceResult target) {
		Tag tag = new Tag(stack.getTag());
		switch (target.getType()) {
			case BLOCK:
				BlockRayTraceResult targetBlock = (BlockRayTraceResult) target;
				if (user != null && user.isSneaking()) {
					if (tag.pickedUpBlock != null) {
						BlockPos targetPos = BlockUtilities.getPlacePosition(targetBlock);
						assert tag.pickedUpBlockState != null;
						BlockState state = Block.getStateById(tag.pickedUpBlockState);
						// todo blacklist and whitelist system
						return state.isValidPosition(world, targetPos) && BlockUtilities.checkNoEntityCollision(state, world, targetPos);
					}
					return true;
				}
				break;
			case ENTITY:
				EntityRayTraceResult targetEntity = (EntityRayTraceResult) target;
				return user != null && user.isSneaking() && tag.pickedUpEntity == null && tag.pickedUpBlock == null && targetEntity.getEntity() instanceof LivingEntity;
			default:
				throw BecauseOf.unexpected();
		}
		return false;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockRayTraceResult targetBlock = RayTraceResults.getBlockRayTraceResultFromItemUseContext(context);
		if (!canUse(context.getItem(), context.getWorld(), context.getPlayer(), targetBlock))
			return ActionResultType.PASS;
		return context.getWorld().isRemote || use(context.getItem(), context.getWorld(), targetBlock) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isSneaking()) {
			if (!worldIn.isRemote) ;
			//NetworkHooks.openGui((ServerPlayerEntity) playerIn, null); //todo
			return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPlayerInteractsEntity(PlayerInteractEvent.EntityInteract e) {
		ItemStack stack = e.getItemStack();
		if (stack.getItem() instanceof ItemWrench) {
			World world = e.getWorld();
			PlayerEntity player = e.getPlayer();
			EntityRayTraceResult targetEntity = new EntityRayTraceResult(e.getTarget());

			if (!canUse(stack, world, player, targetEntity)) return;
			boolean ret = true;
			if (!world.isRemote) ret = use(stack, world, targetEntity);

			e.setCancellationResult(ret ? ActionResultType.SUCCESS : ActionResultType.FAIL);
			e.setCanceled(true);
		}
	}


	protected static class Tag implements INBTSerializable<CompoundNBT> {
		@Nullable
		public CompoundNBT
				pickedUpBlock,
				pickedUpBlockTile,
				pickedUpEntity;
		@Nullable
		public Integer
				pickedUpBlockState;


		protected Tag(@Nullable CompoundNBT tag) { deserializeNBT(tag); }


		@Override
		@Nullable
		public CompoundNBT serializeNBT() {
			CompoundNBT tag = new CompoundNBT();
			{
				CompoundNBT pickup = new CompoundNBT();
				{
					CompoundNBT pickedUpBlock = new CompoundNBT();
					NBTs.setChildIfNotNull(pickedUpBlock, "state", pickedUpBlockState, CompoundNBT::putInt);
					NBTs.setChildIfNotNull(pickedUpBlock, "tile", pickedUpBlockTile, CompoundNBT::put);
					if (NBTs.setTagIfNotEmpty(pickup, "block", pickedUpBlock))
						this.pickedUpBlock = pickedUpBlock;
				}
				NBTs.setChildIfNotNull(pickup, "entity", pickedUpEntity, CompoundNBT::put);
				NBTs.setTagIfNotEmpty(tag, "pickup", pickup);
			}
			return unboxOptional(NBTs.returnTagIfNotEmpty(tag));
		}

		@Override
		public void deserializeNBT(@Nullable CompoundNBT tag) {
			{
				AtomicReference<CompoundNBT> pickup = new AtomicReference<>();
				NBTs.readChildIfHasKey(tag, "pickup", CompoundNBT::new, CompoundNBT::getCompound).ifPresent(pickup::set);
				{
					NBTs.readChildIfHasKey(pickup.get(), "block", CompoundNBT::new, CompoundNBT::getCompound).ifPresent(t -> pickedUpBlock = t);
					NBTs.readChildIfHasKey(pickedUpBlock, "state", () -> IntNBT.valueOf(0), CompoundNBT::getInt).ifPresent(t -> pickedUpBlockState = t);
					NBTs.readChildIfHasKey(pickedUpBlock, "tile", CompoundNBT::new, CompoundNBT::getCompound).ifPresent(t -> pickedUpBlockTile = t);
				}
				NBTs.readChildIfHasKey(pickup.get(), "entity", CompoundNBT::new, CompoundNBT::getCompound).ifPresent(t -> pickedUpEntity = t);
			}
		}
	}
}
