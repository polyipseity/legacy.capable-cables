package $group__.client.gui.debug;

import $group__.client.gui.components.roots.GuiRootWindows;
import $group__.utilities.helpers.Preconditions;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

public enum GuiComponentDebug {
	;

	public static Block getBlockEntry(ResourceLocation name) { return BlockDebug.INSTANCE.setRegistryName(name); }

	public static TileEntityType<TileEntityDebug> getTileEntityEntry(ResourceLocation name) {
		if (TileEntityDebug.type == null) {
			TileEntityDebug.type = TileEntityType.Builder.create(TileEntityDebug::new, BlockDebug.INSTANCE).build(null);
			TileEntityDebug.type.setRegistryName(name);
		}
		return TileEntityDebug.type;
	}

	public static ContainerType<ContainerDebug> getContainerEntry(ResourceLocation name) {
		if (ContainerDebug.type == null) {
			ContainerDebug.type = IForgeContainerType.create((windowId, inv, data) -> {
				assert Minecraft.getInstance().world != null;
				return new ContainerDebug(windowId, Minecraft.getInstance().world, data.readBlockPos());
			});
			ContainerDebug.type.setRegistryName(name);
		}
		return ContainerDebug.type;
	}

	@OnlyIn(CLIENT)
	public static void registerGuiFactory() {
		assert ContainerDebug.type != null;
		ScreenManager.registerFactory(ContainerDebug.type, (container, inv, title) -> new GuiDebug(title, container).getContainerScreen());
	}
}

final class BlockDebug extends Block {
	public static final BlockDebug INSTANCE = new BlockDebug();
	private static final Logger LOGGER = LogManager.getLogger();

	private BlockDebug() {
		super(Properties.from(Blocks.STONE));
		Preconditions.requireRunOnceOnly(LOGGER);
	}

	@Override
	public boolean hasTileEntity(BlockState state) { return true; }

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new TileEntityDebug(); }

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = requireNonNull(worldIn.getTileEntity(pos));
			if (tileEntity instanceof INamedContainerProvider) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
			} else
				throw BecauseOf.unexpected();
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}
}

final class TileEntityDebug extends TileEntity implements IContainerProvider {
	@Nullable
	static TileEntityType<TileEntityDebug> type;

	public TileEntityDebug() { super(requireNonNull(type)); }

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) { return new ContainerDebug(id, requireNonNull(getWorld()), getPos()); }
}

final class ContainerDebug extends Container {
	@Nullable
	static ContainerType<ContainerDebug> type;
	private final TileEntity tileEntity;

	ContainerDebug(int id, World world, BlockPos pos) {
		super(type, id);
		tileEntity = requireNonNull(world.getTileEntity(pos));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerIn, BlockDebug.INSTANCE);
	}
}

@OnlyIn(CLIENT)
final class GuiDebug extends GuiRootWindows<ContainerDebug> {
	GuiDebug(ITextComponent title, ContainerDebug container) { super(title, container); }
}
