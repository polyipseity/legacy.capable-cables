package $group__.client.gui.debug;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.backgrounds.GuiBackgroundDefault;
import $group__.client.gui.components.common.GuiButton;
import $group__.client.gui.components.common.GuiWindow;
import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.components.roots.GuiRootWindows;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.ShapeDescriptor;
import $group__.client.gui.utilities.GuiObjectUtilities;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static $group__.utilities.PreconditionUtilities.requireRunOnceOnly;
import static java.util.Objects.requireNonNull;

public enum GuiComponentDebug {
	;

	public static final String PATH = "debug_gui_component";
	public static final ITextComponent DISPLAY_NAME = new StringTextComponent("Component-based GUI Debug GUI");

	public static Block getBlockEntry() { return BlockDebug.INSTANCE; }

	public static TileEntityType<TileEntityDebug> getTileEntityEntry() { return TileEntityDebug.Type.INSTANCE; }

	public static ContainerType<ContainerDebug> getContainerEntry() { return ContainerDebug.Type.INSTANCE; }

	@OnlyIn(Dist.CLIENT)
	public static <T extends Screen & IHasContainer<ContainerDebug>> void registerGuiFactory() {
		// COMMENT compilation error without the cast
		ScreenManager.registerFactory(ContainerDebug.Type.INSTANCE, (ScreenManager.IScreenFactory<ContainerDebug, T>) (container, inv, title) -> new GuiDebug(title, container).getContainerScreen());
	}
}

@OnlyIn(Dist.CLIENT)
final class GuiDebug extends GuiManagerWindows<ShapeDescriptor.Rectangular<Rectangle2D>, GuiManagers.Data<GuiManagers.Events, ContainerDebug>, ContainerDebug> {
	private static final Logger LOGGER = LogManager.getLogger();

	@SuppressWarnings("MagicNumber")
	GuiDebug(ITextComponent title, ContainerDebug container) {
		super(title, ShapeDescriptor.Rectangular::new,
				new Data<>(new Events(), GuiDebug::getLogger, new GuiBackgroundDefault<>(ShapeDescriptor.Rectangular::new,
						new GuiComponent.Data<>(new GuiComponent.Events(), GuiDebug::getLogger)), container));
		{
			GuiWindow<GuiWindow.Model<?>, GuiWindow.View<?, ?>, GuiWindow.Controller<?, IGuiComponent<?, ?, ?>>, IGuiComponent<?, ?, ?>> window1 = new GuiWindow<>();
			window1.setModel(new GuiWindow.Model<>(window1));
			window1.setView(new GuiWindow.View<>(window1));
			window1.getView().setShapeDescriptor(window1.getView().new ShapeDescriptor<>(
					new Rectangle2D.Double(10, 10, 100, 100),
					new GuiAnchorSet<>(null)));
			window1.setController(new GuiWindow.Controller<>(window1));
			GuiWindow<?, ?, ?, ?> window1 = new GuiWindow<>(new ShapeDescriptor.Rectangular<>(,
					new GuiWindow.Data<>(new GuiComponent.Events(), GuiDebug::getLogger, new GuiWindow.Data.ColorData()));
			GuiWindow<?, ?> window2 = new GuiWindow<ShapeDescriptor.Rectangular<Rectangle2D.Double>, GuiWindow.Data<GuiComponent.Events, GuiWindow.Data.ColorData>>(new ShapeDescriptor.Rectangular<>(new Rectangle2D.Double(50, 50, 200, 200)),
					new GuiWindow.Data<>(new GuiComponent.Events(), GuiDebug::getLogger, new GuiWindow.Data.ColorData())) {
				protected final Rectangle2D current = new Rectangle2D.Double();
				protected final Random random = new Random();
				protected double tick = 0;

				@Override
				public void renderTick(final AffineTransformStack stack, Point2D mouse, float partialTicks) {
					tick = (tick + partialTicks) % 360;
					if (tick % 120 == 0) {
						GuiObjectUtilities.acceptRectangular(getShapeDescriptor().getShape(), (x, y) -> (w, h) -> current.setFrame(x * random.nextDouble() * 2, y * random.nextDouble() * 2, w * random.nextDouble() * 2, y * random.nextDouble() * 2));
						data.setActive(!data.isActive());
					}
					reshape(this, this, s -> s.adapt(current));
					super.renderTick(stack, mouse, partialTicks);
				}

				@Override
				public void onTick(IGuiLifecycleHandler handler, GuiComponent<?, ?> invoker) {
					tick = (tick + 1) % 360;
					if (tick % 120 == 0)
						data.setActive(!data.isActive());
					super.onTick(handler, invoker);
				}
			};
			{
				GuiButton<?, ?> button;
				{
					Random random = new Random();
					// FIXME ellipse
					button = new GuiButton<ShapeDescriptor.Rectangular<?>, GuiButton.Data<?, ?>>(new ShapeDescriptor.Rectangular<>(new Ellipse2D.Double(0, 0, 100, 100)),
							new GuiButton.Data<>(new GuiComponent.Events(), GuiDebug::getLogger, new GuiButton.Data.ColorData())) {
						@Override
						protected boolean onButtonKeyboardPressed(int key, int scanCode, int modifiers) { return key == GLFW.GLFW_KEY_ENTER; }

						@Override
						protected boolean onButtonMousePressed(int button) { return button == GLFW.GLFW_MOUSE_BUTTON_LEFT; }

						@Override
						protected boolean onButtonMouseReleased(int button) { return button == GLFW.GLFW_MOUSE_BUTTON_LEFT && onActivate(); }

						protected boolean onActivate() {
							data.colors.setBase(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
									.setBaseBorder(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
									.setHovering(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
									.setHoveringBorder(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
									.setClicking(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()))
									.setClickingBorder(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()));
							data.visible = !data.visible;
							return true;
						}

						@Override
						protected boolean onButtonKeyboardReleased(int key, int scanCode, int modifiers) { return key == GLFW.GLFW_KEY_ENTER && onActivate(); }
					};
				}

				window2.add(button);
			}

			add(window1, window2);
		}
	}

	private static Logger getLogger() { return LOGGER; }
}

final class ContainerDebug extends Container {
	private final TileEntity tileEntity;

	ContainerDebug(int id, World world, BlockPos pos) {
		super(Type.INSTANCE, id);
		tileEntity = requireNonNull(world.getTileEntity(pos));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerIn, BlockDebug.INSTANCE);
	}

	enum Type {
		;

		static final ContainerType<ContainerDebug> INSTANCE = IForgeContainerType.create((windowId, inv, data) -> {
			assert Minecraft.getInstance().world != null;
			return new ContainerDebug(windowId, Minecraft.getInstance().world, data.readBlockPos());
		});
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
				throw new InternalError();
		}
		return ActionResultType.SUCCESS;
	}
}

final class TileEntityDebug extends TileEntity implements INamedContainerProvider {
	TileEntityDebug() { super(requireNonNull(Type.INSTANCE)); }

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) { return new ContainerDebug(id, requireNonNull(getWorld()), getPos()); }

	@Override
	public ITextComponent getDisplayName() { return GuiComponentDebug.DISPLAY_NAME; }

	enum Type {
		;

		static final TileEntityType<TileEntityDebug> INSTANCE = TileEntityType.Builder.create(TileEntityDebug::new, BlockDebug.INSTANCE).build(null);
	}
}
