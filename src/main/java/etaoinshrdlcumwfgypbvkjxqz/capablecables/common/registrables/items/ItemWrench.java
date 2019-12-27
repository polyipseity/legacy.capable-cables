package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.items;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.items.templates.ItemUnstackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables.LOGGER;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.RegistrablesHelper.ItemHelper.getSlotFor;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.RegistrablesHelper.NBTHelper.*;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.RegistrablesHelper.PositionHelper.getPosition;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.References.*;

@Optional.InterfaceList({
        @Optional.Interface(iface = COFH_CORE_PACKAGE + ".api.item.IToolHammer", modid = COFH_CORE_ID),
        @Optional.Interface(iface = BUILDCRAFT_API_PACKAGE + ".api.tools.IToolWrench", modid = BUILDCRAFT_API_ID)
})
public class ItemWrench extends ItemUnstackable implements IToolHammer, IToolWrench {
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return canUse(player.getHeldItem(hand), player, new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos), hand) ? EnumActionResult.PASS : EnumActionResult.FAIL;
    }
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) use(player.getHeldItem(hand), player, new RayTraceResult(new Vec3d(hitX, hitY, hitZ), facing, pos), hand);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        RayTraceResult targetRTR = new RayTraceResult(target);
        if (canUse(stack, playerIn, targetRTR, hand)) {
            ItemStack newStack = stack.copy();
            use(newStack, playerIn, targetRTR, hand);
            InventoryPlayer inventory = playerIn.inventory;
            inventory.setInventorySlotContents(getSlotFor(inventory, stack), newStack);
            inventory.markDirty();
            return true;
        }
        return false;
    }

    /* Helper methods */
    @SuppressWarnings("unused")
    protected boolean canUse(ItemStack stack, EntityLivingBase user, RayTraceResult target, EnumHand hand) {
        Tag tag = new Tag(stack.getTagCompound());
        switch (target.typeOfHit) {
            case BLOCK: return user.isSneaking() && (tag.pickedUpTile != null || tag.pickedUpEntity != null);
            case ENTITY: return user.isSneaking() && tag.pickedUpEntity == null && tag.pickedUpTile == null && target.entityHit instanceof EntityLivingBase;
        }
        return false;
    }

    @SuppressWarnings("unused")
    protected void use(ItemStack stack, EntityLivingBase user, RayTraceResult target, EnumHand hand) {
        Tag tag = new Tag(stack.getTagCompound());
        switch (target.typeOfHit) {
            case BLOCK:
                if (tag.pickedUpEntity != null) {
                    EntityLivingBase entity = (EntityLivingBase)EntityList.createEntityFromNBT(tag.pickedUpEntity, user.world);
                    if (entity == null) {
                        LOGGER.error("Cannot create entity with tag '{}'", tag.pickedUpEntity);
                        return;
                    }
                    Vec3d targetPos = getPosition(target);
                    entity.setPosition(targetPos.x, targetPos.y, targetPos.z);
                    entity.world.spawnEntity(entity);
                    tag.pickedUpEntity = null;
                }
                stack.setTagCompound(tag.serializeNBT());
                break;
            case ENTITY:
                EntityLivingBase entity = (EntityLivingBase)target.entityHit;
                tag.pickedUpEntity = entity.serializeNBT();
                entity.setDead();
                stack.setTagCompound(tag.serializeNBT());
                break;
        }
    }

    protected static class Tag implements INBTSerializable<NBTTagCompound> {
        protected Tag(@Nullable NBTTagCompound tag) { deserializeNBT(tag); }

        @Nullable
        public NBTTagCompound
                pickedUpTile,
                pickedUpEntity;

        /**
         * {@inheritDoc}
         */
        @Override
        @Nullable
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            {
                NBTTagCompound pickup = new NBTTagCompound();
                setChildIfNotNull(pickup, "tile", pickedUpTile, NBTTagCompound::setTag);
                setChildIfNotNull(pickup, "entity", pickedUpEntity, NBTTagCompound::setTag);
                setTagIfNotEmpty(tag, "pickup", pickup);
            }
            return returnTagIfNotEmpty(tag);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void deserializeNBT(@Nullable NBTTagCompound nbt) {
            {
                NBTTagCompound pickup = readChildIfHasKey(nbt, "pickup", NBTTagCompound.class, NBTTagCompound::getCompoundTag);
                pickedUpTile = readChildIfHasKey(pickup, "tile", NBTTagCompound.class, NBTTagCompound::getCompoundTag);
                pickedUpEntity = readChildIfHasKey(pickup, "entity", NBTTagCompound.class, NBTTagCompound::getCompoundTag);
            }
        }
    }

    /* IToolHammer */
    /**
     * {@inheritDoc}
     */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) { return true; }
    /**
     * {@inheritDoc}
     */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) { return true; }
    /**
     * {@inheritDoc}
     */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {}
    /**
     * {@inheritDoc}
     */
    @Override
    @Optional.Method(modid = COFH_CORE_ID)
    public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {}

    /* IToolWrench */
    /**
     * {@inheritDoc}
     */
    @Override
    @Optional.Method(modid = BUILDCRAFT_API_ID)
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) { return true; }
    /**
     * {@inheritDoc}
     */
    @Override
    @Optional.Method(modid = BUILDCRAFT_API_ID)
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {}
}
