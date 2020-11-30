package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.groups.ModItemGroups;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.utilities.BlockUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.utilities.NBTUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.utilities.RayTraceResultUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
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

import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;

public class WrenchItem extends Item {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());

	protected WrenchItem() {
		super(new Item.Properties().group(ModItemGroups.getDefault()).maxStackSize(1));
	}

	@Override
	public @Nonnull ActionResultType onItemUse(ItemUseContext context) {
		BlockRayTraceResult targetBlock = RayTraceResultUtilities.getBlockRayTraceResultFromItemUseContext(context);
		if (!canUse(context.getItem(), context.getWorld(), context.getPlayer(), targetBlock))
			return ActionResultType.PASS;
		return context.getWorld().isRemote || use(context.getItem(), context.getWorld(), targetBlock) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	protected static boolean canUse(ItemStack stack, World world, @Nullable LivingEntity user, RayTraceResult target) {
		Tag tag = new Tag(stack.getTag());
		switch (target.getType()) {
			case BLOCK:
				BlockRayTraceResult targetBlock = (BlockRayTraceResult) target;
				if (user != null && user.isSneaking()) {
					if (tag.getPickedUpBlock() != null) {
						BlockPos targetPos = BlockUtilities.getPlacePosition(targetBlock);
						assert tag.getPickedUpBlockState().isPresent();
						BlockState state = Block.getStateById(tag.getPickedUpBlockState().getAsInt());
						// todo blacklist and whitelist system
						return state.isValidPosition(world, targetPos) && BlockUtilities.checkNoEntityCollision(state, world, targetPos);
					}
					return true;
				}
				break;
			case ENTITY:
				EntityRayTraceResult targetEntity = (EntityRayTraceResult) target;
				return user != null && user.isSneaking() && tag.getPickedUpEntity() == null && tag.getPickedUpBlock() == null && targetEntity.getEntity() instanceof LivingEntity;
			default:
				throw new InternalError();
		}
		return false;
	}

	protected static boolean use(ItemStack stack, World world, RayTraceResult target) {
		Tag tag = new Tag(stack.getTag());
		switch (target.getType()) {
			case BLOCK:
				BlockRayTraceResult targetBlock = (BlockRayTraceResult) target;
				if (tag.getPickedUpBlock() != null) {
					BlockPos pos = BlockUtilities.getPlacePosition(targetBlock);
					assert tag.getPickedUpBlockState().isPresent();
					BlockState state = Block.getStateById(tag.getPickedUpBlockState().getAsInt());
					if (!world.setBlockState(pos, state)) {
						// COMMENT only a warning, since placing it outside the build height triggers this
						ModConfiguration.getInstance().getLogger()
								.atWarn()
								.addMarker(ModMarkers.getInstance().getRegistryEntryMarker(stack.getItem()))
								.addKeyValue("pos", pos).addKeyValue("state", state)
								.log(() -> getResourceBundle().getString("use.place.block.fail"));
						return false;
					} else if (tag.getPickedUpBlockTile() != null) {
						@Nullable TileEntity tile = state.getBlock().createTileEntity(state, world);
						if (tile == null) {
							ModConfiguration.getInstance().getLogger()
									.atError()
									.addMarker(ModMarkers.getInstance().getRegistryEntryMarker(stack.getItem()))
									.addKeyValue("pos", pos).addKeyValue("state", state).addKeyValue("world", world)
									.log(() -> getResourceBundle().getString("use.place.block.entity.fail"));
							return false;
						}
						tile.deserializeNBT(tag.getPickedUpBlockTile());
						world.setTileEntity(pos, tile);
						tag.setPickedUpBlockTile(null);
					}
					tag.setPickedUpBlockState(null);
					stack.setTag(tag.serializeNBT());
				} else if (tag.getPickedUpEntity() != null) {
					Optional<LivingEntity> entity = EntityType.loadEntityUnchecked(tag.getPickedUpEntity(), world)
							.filter(LivingEntity.class::isInstance)
							.map(LivingEntity.class::cast);
					if (!entity.filter(e -> {
						BlockPos targetPos = BlockUtilities.getPlacePosition(targetBlock);
						e.setPosition(targetPos.getX(), targetPos.getY(), targetPos.getZ());
						world.addEntity(e);
						tag.setPickedUpEntity(null);
						return true;
					}).isPresent()) {
						ModConfiguration.getInstance().getLogger()
								.atError()
								.addMarker(ModMarkers.getInstance().getRegistryEntryMarker(stack.getItem()))
								.addKeyValue("tag.pickedUpEntity", tag.getPickedUpEntity())
								.log(() -> getResourceBundle().getString("use.place.entity.fail"));
						return false;
					}
				} else {
					BlockPos pos = targetBlock.getPos();
					BlockState state = world.getBlockState(pos);
					@Nullable TileEntity tile = state.getBlock().hasTileEntity(state) ? world.getTileEntity(pos) : null;
					tag.setPickedUpBlockState(suppressBoxing(Block.getStateId(state)));
					if (tile != null) {
						tag.setPickedUpBlockTile(tile.serializeNBT());
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
				tag.setPickedUpEntity(entity.serializeNBT());
				stack.setTag(tag.serializeNBT());
				entity.remove(false);
				break;
			default:
				throw new InternalError();
		}
		return true;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public @Nonnull ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
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
		if (stack.getItem() instanceof WrenchItem) {
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
		private CompoundNBT pickedUpBlock;
		@Nullable
		private CompoundNBT pickedUpBlockTile;
		@Nullable
		private CompoundNBT pickedUpEntity;
		@Nullable
		private Integer pickedUpBlockState;

		@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
		protected Tag(@Nullable CompoundNBT tag) { deserializeNBT(tag); }

		@Nullable
		public CompoundNBT getPickedUpBlock() {
			return pickedUpBlock;
		}

		public void setPickedUpBlock(@Nullable CompoundNBT pickedUpBlock) {
			this.pickedUpBlock = pickedUpBlock;
		}

		@Override
		@Nullable
		public CompoundNBT serializeNBT() {
			CompoundNBT tag = new CompoundNBT();
			{
				CompoundNBT pickup = new CompoundNBT();
				{
					CompoundNBT pickedUpBlock = new CompoundNBT();
					NBTUtilities.setChildIfNotNull(pickedUpBlock, "state", OptionalUtilities.valueOf(getPickedUpBlockState()), CompoundNBT::putInt);
					NBTUtilities.setChildIfNotNull(pickedUpBlock, "tile", getPickedUpBlockTile(), CompoundNBT::put);
					if (NBTUtilities.setTagIfNotEmpty(pickup, "block", pickedUpBlock))
						this.setPickedUpBlock(pickedUpBlock);
				}
				NBTUtilities.setChildIfNotNull(pickup, "entity", getPickedUpEntity(), CompoundNBT::put);
				NBTUtilities.setTagIfNotEmpty(tag, "pickup", pickup);
			}
			return NBTUtilities.returnTagIfNotEmpty(tag).orElse(null);
		}

		@Override
		public void deserializeNBT(@Nullable CompoundNBT tag) {
			{
				AtomicReference<CompoundNBT> pickup = new AtomicReference<>();
				NBTUtilities.readChildIfHasKey(tag, "pickup", CompoundNBT::new, CompoundNBT::getCompound).ifPresent(pickup::set);
				{
					NBTUtilities.readChildIfHasKey(pickup.get(), "block", CompoundNBT::new, CompoundNBT::getCompound)
							.ifPresent(this::setPickedUpBlock);
					NBTUtilities.readChildIfHasKey(getPickedUpBlock(), "state", () -> IntNBT.valueOf(0), (compoundNBT, key) -> suppressBoxing(compoundNBT.getInt(key)))
							.ifPresent(this::setPickedUpBlockState);
					NBTUtilities.readChildIfHasKey(getPickedUpBlock(), "tile", CompoundNBT::new, CompoundNBT::getCompound)
							.ifPresent(this::setPickedUpBlockTile);
				}
				NBTUtilities.readChildIfHasKey(pickup.get(), "entity", CompoundNBT::new, CompoundNBT::getCompound).ifPresent(this::setPickedUpEntity);
			}
		}

		public OptionalInt getPickedUpBlockState() {
			return OptionalUtilities.ofInt(pickedUpBlockState);
		}

		@Nullable
		public CompoundNBT getPickedUpBlockTile() {
			return pickedUpBlockTile;
		}

		public void setPickedUpBlockTile(@Nullable CompoundNBT pickedUpBlockTile) {
			this.pickedUpBlockTile = pickedUpBlockTile;
		}

		@Nullable
		public CompoundNBT getPickedUpEntity() {
			return pickedUpEntity;
		}

		public void setPickedUpEntity(@Nullable CompoundNBT pickedUpEntity) {
			this.pickedUpEntity = pickedUpEntity;
		}

		public void setPickedUpBlockState(@Nullable Integer pickedUpBlockState) {
			this.pickedUpBlockState = pickedUpBlockState;
		}
	}
}
