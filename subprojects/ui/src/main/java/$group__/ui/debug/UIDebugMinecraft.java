package $group__.ui.debug;

import $group__.ui.ConfigurationUI;
import $group__.ui.UIFacade;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.parsers.IUIResourceParser;
import $group__.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import $group__.ui.minecraft.mvvm.adapters.UIAdapterScreen;
import $group__.ui.minecraft.mvvm.components.UIComponentManagerMinecraft;
import $group__.ui.minecraft.mvvm.components.UIViewComponentMinecraft;
import $group__.ui.minecraft.mvvm.components.parsers.dom.UIDOMPrototypeMinecraftParser;
import $group__.ui.minecraft.mvvm.viewmodels.UIViewModelMinecraft;
import $group__.ui.mvvm.binding.Binder;
import $group__.ui.mvvm.models.UIModel;
import $group__.ui.mvvm.views.components.extensions.UIExtensionCursorHandleProviderComponent;
import $group__.ui.parsers.components.UIDOMPrototypeParser;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.structures.NamespacePrefixedString;
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
import org.w3c.dom.Document;

import static $group__.utilities.PreconditionUtilities.requireRunOnceOnly;
import static java.util.Objects.requireNonNull;

public enum UIDebugMinecraft {
	;

	public static final String PATH = "debug_ui";
	public static final ITextComponent DISPLAY_NAME = new StringTextComponent("MVVM GUI Debug UI");

	public static Block getBlockEntry() { return DebugBlock.INSTANCE; }

	public static TileEntityType<DebugTileEntity> getTileEntityEntry() { return DebugTileEntity.Type.INSTANCE; }

	public static ContainerType<DebugContainer> getContainerEntry() { return DebugContainer.Type.INSTANCE; }

	@OnlyIn(Dist.CLIENT)
	public static void registerUIFactory() { ScreenManager.registerFactory(DebugContainer.Type.INSTANCE, DebugUI.INSTANCE); }

	@SuppressWarnings("HardcodedFileSeparator")
	@OnlyIn(Dist.CLIENT)
	private enum DebugUI
			implements ScreenManager.IScreenFactory<DebugContainer, UIAdapterScreen.WithContainer<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer>> {
		INSTANCE,
		;

		private static final Logger LOGGER = LogManager.getLogger();
		private static final IUIResourceParser<UIComponentManagerMinecraft, Document> PARSER =
				UIDOMPrototypeMinecraftParser.makeParserMinecraft(
						UIDOMPrototypeParser.makeParserStandard(
								new UIDOMPrototypeMinecraftParser<>(UIComponentManagerMinecraft.class)));

		static {
			Try.run(() ->
					PARSER.parse(UIFacade.UFMinecraft.parseResourceDocument(new NamespacePrefixedString(ConfigurationUI.getModId(), "ui/schemas/components_test.xml"))), LOGGER);
			ThrowableCatcher.rethrow(true);
		}

		@Override
		public UIAdapterScreen.WithContainer<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> create(DebugContainer container, PlayerInventory inv, ITextComponent title) {
			return createUI(container);
		}

		private static UIAdapterScreen.WithContainer<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> createUI(DebugContainer container) {
			UIAdapterScreen.WithContainer<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> ret =
					UIFacade.UFMinecraft.createScreen(
							DISPLAY_NAME,
							UIFacade.UFMinecraft.createInfrastructure(
									new UIViewComponentMinecraft<>(PARSER.construct()),
									new ViewModel(),
									new Binder()
							),
							container);
			IExtensionContainer.addExtensionSafe(ret.getInfrastructure().getView(),
					new UIExtensionCursorHandleProviderComponent<>(IUIViewComponent.class));
			return ret;
		}

		@OnlyIn(Dist.CLIENT)
		private static final class ViewModel
				extends UIViewModelMinecraft<Model> {
			private ViewModel() { super(new Model()); }
		}

		@OnlyIn(Dist.CLIENT)
		private static final class Model
				extends UIModel {}
	}

/* TODO
@OnlyIn(Dist.CLIENT)
final class UIDebug extends GuiManagerWindows<AbstractShapeDescriptor.Rectangular<Rectangle2D>, GuiManagers.Data<GuiManagers.Events, DebugContainer>, DebugContainer> {
	private static final Logger LOGGER = LogManager.getLogger();

	@SuppressWarnings("MagicNumber")
	UIDebug(ITextComponent title, DebugContainer container) {
		super(title, AbstractShapeDescriptor.Rectangular::new,
				new Data<>(new Events(), UIDebug::getLogger, new GuiBackgroundDefault<>(AbstractShapeDescriptor.Rectangular::new,
						new UIComponentMinecraft.Data<>(new UIComponentMinecraft.Events(), UIDebug::getLogger)), container));
		{
			UIComponentMinecraftWindow<UIComponentMinecraftWindow.Model, UIComponentMinecraftWindow.View<?, ?>, UIComponentMinecraftWindow.Controller, IUIComponent> window1 = new UIComponentMinecraftWindow<>();
			window1.setModel(new UIComponentMinecraftWindow.Model<>(window1));
			window1.setView(new UIComponentMinecraftWindow.View<>(window1));
			window1.getView().setShapeDescriptor(window1.getView().new AbstractShapeDescriptor<>(
					new Rectangle2D.Double(10, 10, 100, 100),
					new ShapeAnchorSet<>(null)));
			window1.setController(new UIComponentMinecraftWindow.Controller<>(window1));
			UIComponentMinecraftWindow<?, ?, ?, ?> window1 = new UIComponentMinecraftWindow<>(new AbstractShapeDescriptor.Rectangular<>(
					new UIComponentMinecraftWindow.Data<>(new UIComponentMinecraft.Events(), UIDebug::getLogger, new UIComponentMinecraftWindow.Data.ColorData()));
			UIComponentMinecraftWindow<?, ?> window2 = new UIComponentMinecraftWindow<AbstractShapeDescriptor.Rectangular<Rectangle2D.Double>, UIComponentMinecraftWindow.Data<UIComponentMinecraft.Events, UIComponentMinecraftWindow.Data.ColorData>>(new AbstractShapeDescriptor.Rectangular<>(new Rectangle2D.Double(50, 50, 200, 200)),
					new UIComponentMinecraftWindow.Data<>(new UIComponentMinecraft.Events(), UIDebug::getLogger, new UIComponentMinecraftWindow.Data.ColorData())) {
				protected final Rectangle2D current = new Rectangle2D.Double();
				protected final Random random = new Random();
				protected double tick = 0;

				@Override
				public void renderTick(IAffineTransformStack stack, Point2D mouse, float partialTicks) {
					tick = (tick + partialTicks) % 360;
					if (tick % 120 == 0) {
						UIObjectUtilities.acceptRectangular(getShapeDescriptor().getShape(), (x, y) -> (w, h) -> current.setFrame(x * random.nextDouble() * 2, y * random.nextDouble() * 2, w * random.nextDouble() * 2, y * random.nextDouble() * 2));
						data.setActive(!data.isActive());
					}
					reshapeComponent(this, this, s -> s.adapt(current));
					super.renderTick(stack, mouse, partialTicks);
				}

				@Override
				public void onTick(IGuiLifecycleHandler handler, UIComponentMinecraft<?, ?> invoker) {
					tick = (tick + 1) % 360;
					if (tick % 120 == 0)
						data.setActive(!data.isActive());
					super.onTick(handler, invoker);
				}
			};
			{
				UIButtonComponentMinecraft button;
				{
					Random random = new Random();
					button = new UIButtonComponentMinecraft(new AbstractShapeDescriptor.Rectangular<>(new Ellipse2D.Double(0, 0, 100, 100)),
							new UIButtonComponentMinecraft.Data<>(new UIComponentMinecraft.Events(), UIDebug::getLogger, new UIButtonComponentMinecraft.Data.ColorData())) {
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
}*/

	private static final class DebugContainer extends Container {
		private final TileEntity tileEntity;

		private DebugContainer(int id, World world, BlockPos pos) {
			super(Type.INSTANCE, id);
			tileEntity = requireNonNull(world.getTileEntity(pos));
		}

		@Override
		public boolean canInteractWith(PlayerEntity playerIn) {
			return isWithinUsableDistance(IWorldPosCallable.of(requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerIn, DebugBlock.INSTANCE);
		}

		private enum Type {
			;

			private static final ContainerType<DebugContainer> INSTANCE = IForgeContainerType.create((windowId, inv, data) -> {
				assert Minecraft.getInstance().world != null;
				return new DebugContainer(windowId, Minecraft.getInstance().world, data.readBlockPos());
			});
		}
	}

	private static final class DebugBlock extends Block {
		private static final DebugBlock INSTANCE = new DebugBlock();
		private static final Logger LOGGER = LogManager.getLogger();

		private DebugBlock() {
			super(Properties.from(Blocks.STONE));
			requireRunOnceOnly(LOGGER);
		}

		@Override
		public boolean hasTileEntity(BlockState state) { return true; }

		@Override
		public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new DebugTileEntity(); }

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

	private static final class DebugTileEntity extends TileEntity implements INamedContainerProvider {
		private DebugTileEntity() { super(requireNonNull(Type.INSTANCE)); }

		@Override
		public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) { return new DebugContainer(id, requireNonNull(getWorld()), getPos()); }

		@Override
		public ITextComponent getDisplayName() { return UIDebugMinecraft.DISPLAY_NAME; }

		private enum Type {
			;

			private static final TileEntityType<DebugTileEntity> INSTANCE = TileEntityType.Builder.create(DebugTileEntity::new, DebugBlock.INSTANCE).build(null);
		}
	}
}
