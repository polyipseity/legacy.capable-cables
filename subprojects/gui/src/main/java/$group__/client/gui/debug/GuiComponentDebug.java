package $group__.client.gui.debug;

import $group__.client.gui.components.GuiButton;
import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiWindow;
import $group__.client.gui.components.backgrounds.GuiBackgroundDefault;
import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.components.roots.GuiRootWindows;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static $group__.utilities.Preconditions.requireRunOnceOnly;
import static java.util.Objects.requireNonNull;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

public enum GuiComponentDebug {
	;

	public static final String PATH = "debug_gui_component";
	public static final ITextComponent DISPLAY_NAME = new StringTextComponent("Component-based GUI Debug GUI");

	public static Block getBlockEntry() { return BlockDebug.INSTANCE; }

	public static TileEntityType<TileEntityDebug> getTileEntityEntry() {
		if (TileEntityDebug.type == null)
			TileEntityDebug.type = TileEntityType.Builder.create(TileEntityDebug::new, BlockDebug.INSTANCE).build(null);
		return TileEntityDebug.type;
	}

	public static ContainerType<ContainerDebug> getContainerEntry() {
		if (ContainerDebug.type == null) {
			ContainerDebug.type = IForgeContainerType.create((windowId, inv, data) -> {
				assert Minecraft.getInstance().world != null;
				return new ContainerDebug(windowId, Minecraft.getInstance().world, data.readBlockPos());
			});
		}
		return ContainerDebug.type;
	}

	@OnlyIn(CLIENT)
	public static <T extends Screen & IHasContainer<ContainerDebug>> void registerGuiFactory() {
		assert ContainerDebug.type != null;
		// COMMENT compilation error without the cast
		ScreenManager.registerFactory(ContainerDebug.type, (ScreenManager.IScreenFactory<ContainerDebug, T>) (container, inv, title) -> new GuiDebug(title, container).getContainerScreen());
	}
}

@OnlyIn(CLIENT)
final class GuiDebug extends GuiRootWindows<GuiRoot.Data<GuiRoot.Events, ContainerDebug>, ContainerDebug> {
	private static final Logger LOGGER = LogManager.getLogger();

	@SuppressWarnings("MagicNumber")
	GuiDebug(ITextComponent title, ContainerDebug container) {
		super(title,
				new Data<>(new Events(), GuiDebug::getLogger, new GuiBackgroundDefault<>(
						new GuiComponent.Data<>(new GuiComponent.Events(), GuiDebug::getLogger)), container));
		{
			GuiWindow<?> window1 = new GuiWindow<>(new Rectangle2D.Double(10, 10, 100, 100),
					new GuiWindow.Data<>(new GuiComponent.Events(), GuiDebug::getLogger, new GuiWindow.Data.ColorData()));
			GuiWindow<?> window2 = new GuiWindow<GuiWindow.Data<?, ?>>(new Rectangle2D.Double(50, 50, 200, 200),
					new GuiWindow.Data<>(new GuiComponent.Events(), GuiDebug::getLogger, new GuiWindow.Data.ColorData())) {
				protected double rotation = 0;

				@Override
				public void renderPre(AffineTransformStack stack, Point2D mouse, float partialTicks) {
					rotation = (rotation + 0.25 * partialTicks) % 360;
					super.renderPre(stack, mouse, partialTicks);
				}

				@Override
				protected void transformThis(AffineTransformStack stack) {
					// COMMENT I will probably not support transforms with windows due to its draggable and resizable properties...
					super.transformThis(stack);
					Rectangle2D r = getRectangle();
					AffineTransform transform = stack.delegated.peek();
					transform.rotate(Math.toRadians(rotation), r.getX() + 50, r.getY() + 50);
					transform.scale(1.5, 0.5);
				}
			};
			{
				GuiButton<?> button;
				{
					Random random = new Random();
					button = new GuiButton.Functional<>(new Ellipse2D.Double(0, 0, 100, 100),
							new GuiButton.Functional.Data<>(new GuiComponent.Events(), GuiDebug::getLogger, new GuiButton.Data.ColorData(),
									(b, i) -> i == GLFW.GLFW_MOUSE_BUTTON_LEFT,
									(b, i) -> {
										b.data.colors.setBase(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
												.setBaseBorder(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
												.setHovering(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
												.setHoveringBorder(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
												.setClicking(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
												.setClickingBorder(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()));
										return true;
									}));
				}

				window2.add(button);
			}

			add(window1, window2);
		}
	}

	private static Logger getLogger() { return LOGGER; }
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

final class BlockDebug extends Block {
	static final BlockDebug INSTANCE = new BlockDebug();
	private static final Logger LOGGER = LogManager.getLogger();

	private BlockDebug() {
		super(Properties.from(Blocks.STONE));
		requireRunOnceOnly(LOGGER);
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
			if (tileEntity instanceof INamedContainerProvider)
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
			else
				throw BecauseOf.unexpected();
		}
		return ActionResultType.SUCCESS;
	}
}

final class TileEntityDebug extends TileEntity implements INamedContainerProvider {
	@Nullable
	static TileEntityType<TileEntityDebug> type;

	TileEntityDebug() { super(requireNonNull(type)); }

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) { return new ContainerDebug(id, requireNonNull(getWorld()), getPos()); }

	@Override
	public ITextComponent getDisplayName() { return GuiComponentDebug.DISPLAY_NAME; }
}
