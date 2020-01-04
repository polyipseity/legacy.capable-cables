package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.GuiWrench;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.templates.ItemUnstackable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.IEventBusSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables.LOGGER;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.RegistrableHelper.Blocks.checkNoEntityCollision;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.RegistrableHelper.Blocks.getPosition;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.RegistrableHelper.NBT.*;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.References.*;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.requireRunOnceOnly;

@Optional.InterfaceList({
        @Optional.Interface(iface = COFH_CORE_PACKAGE + ".api.item.IToolHammer", modid = COFH_CORE_ID),
        @Optional.Interface(iface = BUILDCRAFT_API_PACKAGE + ".api.tools.IToolWrench", modid = BUILDCRAFT_API_ID)
})
public class ItemWrench extends ItemUnstackable implements IEventBusSubscriber, IToolHammer, IToolWrench {
    public ItemWrench() { super(); }

    @SuppressWarnings({"MethodCallSideOnly", "NewExpressionSideOnly"})
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            if (worldIn.isRemote) Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiWrench(stack)));
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

      /** {@inheritDoc} */
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return canUse(player.getHeldItem(hand), world, player, new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos), hand) ? EnumActionResult.PASS : EnumActionResult.FAIL;
    }
      /** {@inheritDoc} */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return worldIn.isRemote || use(player.getHeldItem(hand), worldIn, player, new RayTraceResult(new Vec3d(hitX, hitY, hitZ), facing, pos), hand) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
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
            if (ret && !world.isRemote) {
                ret = use(stack, world, player, targetRTR, hand);
            }
            e.setCancellationResult(ret ? EnumActionResult.SUCCESS : EnumActionResult.FAIL);
            e.setCanceled(true);
        }
    }

    /* Helper methods */
    @SuppressWarnings("unused")
    protected boolean canUse(ItemStack stack, World world, EntityLivingBase user, RayTraceResult target, EnumHand hand) {
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

    @SuppressWarnings("unused")
    protected boolean use(ItemStack stack, World world, EntityLivingBase user, RayTraceResult target, EnumHand hand) {
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
                        TileEntity tile = state.getBlock().createTileEntity(world, state);
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
                    EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityFromNBT(tag.pickedUpEntity, world);
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
                    TileEntity tile = state.getBlock().hasTileEntity(state) ? world.getTileEntity(pos) : null;
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

    protected static class Tag implements INBTSerializable<NBTTagCompound> {
        protected Tag(@Nullable NBTTagCompound tag) { deserializeNBT(tag); }

        @Nullable
        public NBTTagCompound
                pickedUpBlock,
                pickedUpBlockTile,
                pickedUpEntity;
        @Nullable
        public Integer
                pickedUpBlockState;

         /** {@inheritDoc} */
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
            return returnTagIfNotEmpty(tag);
        }
         /** {@inheritDoc} */
        @Override
        public void deserializeNBT(@Nullable NBTTagCompound nbt) {
            {
                NBTTagCompound pickup = readChildIfHasKey(nbt, "pickup", NBTTagCompound.class, NBTTagCompound::getCompoundTag);
                {
                    pickedUpBlock = readChildIfHasKey(pickup, "block", NBTTagCompound.class, NBTTagCompound::getCompoundTag);
                    pickedUpBlockState = readChildIfHasKey(pickedUpBlock, "state", int.class, NBTTagCompound::getInteger);
                    pickedUpBlockTile = readChildIfHasKey(pickedUpBlock, "tile", NBTTagCompound.class, NBTTagCompound::getCompoundTag);
                }
                pickedUpEntity = readChildIfHasKey(pickup, "entity", NBTTagCompound.class, NBTTagCompound::getCompoundTag);
            }
        }
    }

    /* IToolHammer */
      /** {@inheritDoc} */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) { return true; }
      /** {@inheritDoc} */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) { return true; }
      /** {@inheritDoc} */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {}
      /** {@inheritDoc} */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {}

    /* IToolWrench */
      /** {@inheritDoc} */
    @Override
    @Optional.Method(modid = BUILDCRAFT_API_ID)
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) { return true; }
      /** {@inheritDoc} */
    @Override
    @Optional.Method(modid = BUILDCRAFT_API_ID)
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {}

    // Configuration
    public static final Configuration CONFIGURATION = CapableCables.Configuration.behavior.items.wrench;
    @SuppressWarnings("CanBeFinal")
    public static final class Configuration {
        public static final String LANG_KEY_BASE = CapableCables.Configuration.Behavior.Items.LANG_KEY_BASE + ".wrench";
        public Configuration() { requireRunOnceOnly(); }

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
    }
}
