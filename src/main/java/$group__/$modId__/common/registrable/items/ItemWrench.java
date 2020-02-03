package $group__.$modId__.common.registrable.items;

import $group__.$modId__.ModThis;
import $group__.$modId__.common.inventory.ContainerWrenchAdapter;
import $group__.$modId__.common.registrable.items.base.ItemUnstackable;
import $group__.$modId__.common.registrable.utilities.constructs.IRegistrableEventBusSubscriber;
import $group__.$modId__.utilities.constructs.interfaces.basic.IStruct;
import $group__.$modId__.utilities.variables.Constants;
import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

import static $group__.$modId__.common.registrable.utilities.helpers.Registrables.Blocks.checkNoEntityCollision;
import static $group__.$modId__.common.registrable.utilities.helpers.Registrables.Blocks.getPosition;
import static $group__.$modId__.common.registrable.utilities.helpers.Registrables.NBTs.*;
import static $group__.$modId__.utilities.helpers.Miscellaneous.markUnused;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.requireRunOnceOnly;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static $group__.$modId__.utilities.variables.Globals.MOD;

@Optional.InterfaceList({
		@Optional.Interface(iface = Constants.COFH_CORE_PACKAGE + ".api.item.IToolHammer", modid = Constants.COFH_CORE_ID),
		@Optional.Interface(iface = Constants.BUILDCRAFT_API_PACKAGE + ".api.tools.IToolWrench", modid = Constants.BUILDCRAFT_API_ID)
})
public class ItemWrench extends ItemUnstackable implements IRegistrableEventBusSubscriber, IToolHammer, IToolWrench {
	/* SECTION static variables */

	public static final Configuration CONFIGURATION = ModThis.Configuration.behavior.items.wrench;


	/* SECTION constructors */

	protected ItemWrench() { requireRunOnceOnly(); }


	/* SECTION static methods */

	@SuppressWarnings("unused")
	protected static boolean use(ItemStack stack, World world, EntityLivingBase user, RayTraceResult target, EnumHand hand) {
		Tag tag = new Tag(stack.getTagCompound());
		switch (target.typeOfHit) {
			case BLOCK:
				if (tag.pickedUpBlock != null) {
					BlockPos pos = target.getBlockPos().add(target.sideHit.getDirectionVec());
					//noinspection ConstantConditions
					IBlockState state = Block.getStateById(tag.pickedUpBlockState);
					Block block = state.getBlock();
					if (!world.setBlockState(pos, state)) {
						LOGGER.error("Cannot create block state ID {}", tag.pickedUpBlockState);
						return false;
					} else if (tag.pickedUpBlockTile != null) {
						@Nullable TileEntity tile = state.getBlock().createTileEntity(world, state);
						if (tile == null) {
							LOGGER.error("Cannot create tile entity of block state ID {}", tag.pickedUpBlockState);
							return false;
						}
						tile.deserializeNBT(tag.pickedUpBlockTile);
						world.setTileEntity(pos, tile);
						tag.pickedUpBlockTile = null;
					}
					tag.pickedUpBlockState = null;
					stack.setTagCompound(tag.serializeNBT());
				} else if (tag.pickedUpEntity != null) {
					@Nullable EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityFromNBT(tag.pickedUpEntity, world);
					if (entity == null) {
						LOGGER.error("Cannot create entity with tag '{}'", tag.pickedUpEntity);
						return false;
					}
					Vec3d targetPos = getPosition(target);
					entity.setPosition(targetPos.x, targetPos.y, targetPos.z);
					world.spawnEntity(entity);
					tag.pickedUpEntity = null;
				} else {
					BlockPos pos = target.getBlockPos();
					IBlockState state = user.world.getBlockState(pos);
					@Nullable TileEntity tile = state.getBlock().hasTileEntity(state) ? world.getTileEntity(pos) : null;
					tag.pickedUpBlockState = Block.getStateId(state);
					if (tile != null) {
						tag.pickedUpBlockTile = tile.serializeNBT();
						world.removeTileEntity(pos);
					}
					stack.setTagCompound(tag.serializeNBT());
					world.setBlockToAir(pos);
				}
				stack.setTagCompound(tag.serializeNBT());
				break;
			case ENTITY:
				EntityLivingBase entity = (EntityLivingBase) target.entityHit;
				tag.pickedUpEntity = entity.serializeNBT();
				stack.setTagCompound(tag.serializeNBT());
				world.removeEntity(entity);
				break;
		}
		return true;
	}

	@SuppressWarnings("unused")
	protected static boolean canUse(ItemStack stack, World world, EntityLivingBase user, RayTraceResult target, EnumHand hand) {
		if (!CONFIGURATION.enablePickup) return false;
		Tag tag = new Tag(stack.getTagCompound());
		switch (target.typeOfHit) {
			case BLOCK:
				if (user.isSneaking() && CONFIGURATION.enablePickupTile) {
					if (tag.pickedUpBlock != null) {
						BlockPos targetPos = target.getBlockPos().add(target.sideHit.getDirectionVec());
						//noinspection ConstantConditions
						IBlockState state = Block.getStateById(tag.pickedUpBlockState);
						return state.getBlock().canPlaceBlockOnSide(world, targetPos, target.sideHit) && checkNoEntityCollision(state, world, targetPos);
					}
					return true;
				}
				break;
			case ENTITY:
				return user.isSneaking() && CONFIGURATION.enablePickupTile && tag.pickedUpEntity == null && tag.pickedUpBlock == null && target.entityHit instanceof EntityLivingBase;
		}
		return false;
	}


	/* SECTION methods */

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return worldIn.isRemote || use(player.getHeldItem(hand), worldIn, player, new RayTraceResult(new Vec3d(hitX, hitY, hitZ), facing, pos), hand) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isSneaking()) {
			if (!worldIn.isRemote)
				playerIn.openGui(MOD, ContainerWrenchAdapter.ID, worldIn, handIn.ordinal(), markUnused(int.class), markUnused(int.class));
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return canUse(player.getHeldItem(hand), world, player, new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos), hand) ? EnumActionResult.PASS : EnumActionResult.FAIL;
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPlayerInteractsEntity(PlayerInteractEvent.EntityInteract e) {
		ItemStack stack = e.getItemStack();
		Item item = stack.getItem();
		if (item instanceof ItemWrench) {
			World world = e.getWorld();
			EntityPlayer player = e.getEntityPlayer();
			RayTraceResult targetRTR = new RayTraceResult(e.getTarget());
			EnumHand hand = e.getHand();
			boolean ret = canUse(stack, world, player, targetRTR, hand);
			if (ret && !world.isRemote) ret = use(stack, world, player, targetRTR, hand);
			e.setCancellationResult(ret ? EnumActionResult.SUCCESS : EnumActionResult.FAIL);
			e.setCanceled(true);
		}
	}

	@Override
	@Optional.Method(modid = Constants.COFH_CORE_ID)
	public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) { return true; }

	@Override
	@Optional.Method(modid = Constants.COFH_CORE_ID)
	public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) { return true; }

	@Override
	@Optional.Method(modid = Constants.COFH_CORE_ID)
	public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {  /* MARK empty */ }

	@Override
	@Optional.Method(modid = Constants.COFH_CORE_ID)
	public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {  /* MARK empty */ }


	/* SECTION static methods */

	@Override
	@Optional.Method(modid = Constants.BUILDCRAFT_API_ID)
	public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) { return true; }

	@Override
	@Optional.Method(modid = Constants.BUILDCRAFT_API_ID)
	public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {  /* MARK empty */ }


	/* SECTION static classes */

	protected static class Tag implements IStruct, INBTSerializable<NBTTagCompound> {
		/* SECTION variables */

		@Nullable
		public NBTTagCompound
				pickedUpBlock,
				pickedUpBlockTile,
				pickedUpEntity;
		@Nullable
		public Integer
				pickedUpBlockState;


		/* SECTION constructors */

		protected Tag(@Nullable NBTTagCompound tag) { deserializeNBT(tag); }


		/* SECTION methods */

		@Override
		@Nullable
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			{
				NBTTagCompound pickup = new NBTTagCompound();
				{
					NBTTagCompound pickedUpBlock = new NBTTagCompound();
					setChildIfNotNull(pickedUpBlock, "state", pickedUpBlockState, NBTTagCompound::setInteger);
					setChildIfNotNull(pickedUpBlock, "tile", pickedUpBlockTile, NBTTagCompound::setTag);
					if (setTagIfNotEmpty(pickup, "block", pickedUpBlock)) this.pickedUpBlock = pickedUpBlock;
				}
				setChildIfNotNull(pickup, "entity", pickedUpEntity, NBTTagCompound::setTag);
				setTagIfNotEmpty(tag, "pickup", pickup);
			}
			return unboxOptional(returnTagIfNotEmpty(tag));
		}

		@Override
		public void deserializeNBT(@Nullable NBTTagCompound tag) {
			{
				AtomicReference<NBTTagCompound> pickup = new AtomicReference<>();
				readChildIfHasKey(tag, "pickup", NBTTagCompound.class, NBTTagCompound::getCompoundTag).ifPresent(pickup::set);
				{
					readChildIfHasKey(pickup.get(), "block", NBTTagCompound.class, NBTTagCompound::getCompoundTag).ifPresent(t -> pickedUpBlock = t);
					readChildIfHasKey(pickedUpBlock, "state", int.class, NBTTagCompound::getInteger).ifPresent(t -> pickedUpBlockState = t);
					readChildIfHasKey(pickedUpBlock, "tile", NBTTagCompound.class, NBTTagCompound::getCompoundTag).ifPresent(t -> pickedUpBlockTile = t);
				}
				readChildIfHasKey(pickup.get(), "entity", NBTTagCompound.class, NBTTagCompound::getCompoundTag).ifPresent(t -> pickedUpEntity = t);
			}
		}
	}


	@SuppressWarnings("CanBeFinal")
	public static final class Configuration {
		public static final String LANG_KEY_BASE = ModThis.Configuration.Behavior.Items.LANG_KEY_BASE + ".wrench";
		@Config.Name("Enable picking up")
		@Config.Comment({
				"Whether the wrench can pickup things for moving.",
				"This overrides other options."
		})
		public boolean enablePickup = true;
		@Config.Name("Enable picking up blocks")
		@Config.Comment("Whether the wrench can pickup blocks for moving.")
		public boolean enablePickupTile = true;
		@Config.Name("Enable picking up entity")
		@Config.Comment("Whether the wrench can pickup entities for moving.")
		public boolean enablePickupEntity = true;

		public Configuration() { requireRunOnceOnly(); }
	}
}
