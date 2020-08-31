package $group__.ui.debug;

import $group__.ui.ConfigurationUI;
import $group__.ui.UIFacade;
import $group__.ui.core.mvvm.binding.IBinder;
import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.ui.core.mvvm.views.events.IUIEventKeyboard;
import $group__.ui.core.mvvm.views.events.IUIEventMouse;
import $group__.ui.core.parsers.IUIResourceParser;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import $group__.ui.minecraft.mvvm.adapters.AbstractContainerScreenAdapter;
import $group__.ui.minecraft.mvvm.components.UIComponentManagerMinecraft;
import $group__.ui.minecraft.mvvm.components.UIViewComponentMinecraft;
import $group__.ui.minecraft.mvvm.components.common.UIComponentWindowMinecraft;
import $group__.ui.minecraft.mvvm.components.parsers.dom.UIDOMPrototypeMinecraftParser;
import $group__.ui.minecraft.mvvm.viewmodels.UIViewModelMinecraft;
import $group__.ui.mvvm.binding.Binder;
import $group__.ui.mvvm.binding.BindingField;
import $group__.ui.mvvm.binding.BindingMethodDestination;
import $group__.ui.mvvm.binding.ObservableField;
import $group__.ui.mvvm.models.UIModel;
import $group__.ui.mvvm.views.components.common.UIComponentButton;
import $group__.ui.mvvm.views.components.extensions.UIExtensionCursorHandleProviderComponent;
import $group__.ui.parsers.components.UIDOMPrototypeParser;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
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
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static $group__.utilities.PreconditionUtilities.requireRunOnceOnly;
import static java.util.Objects.requireNonNull;

public enum UIDebugMinecraft {
	;

	public static final String PATH = "debug_ui";
	public static final ITextComponent DISPLAY_NAME = new StringTextComponent("MVVM GUI Debug UI");

	public static Block getBlockEntry() { return DebugBlock.INSTANCE; }

	public static TileEntityType<DebugTileEntity> getTileEntityEntry() { return DebugTileEntity.Type.INSTANCE; }

	public static ContainerType<DebugContainer> getContainerEntry() { return DebugContainer.Type.INSTANCE; }

	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public static void registerUIFactory() {
		{
			ContainerType<DebugContainer> ct = DebugContainer.Type.INSTANCE;
			DebugUI uf = DebugUI.INSTANCE;
			// COMMENT this should mean that class loading is executed in parallel
			DeferredWorkQueue.runLater(() ->
					ScreenManager.registerFactory(ct, uf));
		}
	}

	@SuppressWarnings("HardcodedFileSeparator")
	@OnlyIn(Dist.CLIENT)
	private enum DebugUI
			implements ScreenManager.IScreenFactory<DebugContainer, AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer>> {
		INSTANCE,
		;

		private static final Logger LOGGER = LogManager.getLogger();
		private static final IUIResourceParser<UIComponentManagerMinecraft, Document> PARSER =
				UIDOMPrototypeMinecraftParser.makeParserMinecraft(
						UIDOMPrototypeParser.makeParserStandard(
								new UIDOMPrototypeMinecraftParser<>(UIComponentManagerMinecraft.class)));

		static {
			Try.run(() ->
					PARSER.parse(UIFacade.Minecraft.parseResourceDocument(new NamespacePrefixedString(ConfigurationUI.getModId(), "ui/schemas/components_test.xml"))), LOGGER);
			ThrowableCatcher.rethrow(true);
			PARSER.construct(); // COMMENT early check
		}

		@Override
		public AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> create(DebugContainer container, PlayerInventory inv, ITextComponent title) {
			return createUI(container);
		}

		private static AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> createUI(DebugContainer container) {
			IBinder binder = new Binder();
			// COMMENT Color <-> Integer
			binder.addFieldTransformer((Color color) -> Optional.ofNullable(color).map(Color::getRGB).orElse(null));
			binder.addFieldTransformer((Integer t) -> Optional.ofNullable(t).map(i -> new Color(i, true)).orElse(null));

			AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> ret =
					UIFacade.Minecraft.createScreen(
							DISPLAY_NAME,
							UIFacade.Minecraft.createInfrastructure(
									new UIViewComponentMinecraft<>(PARSER.construct()),
									new ViewModel(),
									binder
							),
							container);
			IExtensionContainer.addExtensionChecked(ret.getInfrastructure().getView(),
					new UIExtensionCursorHandleProviderComponent<>(IUIViewComponent.class));

			return ret;
		}

		@SuppressWarnings("unused")
		private static final class CustomWindowRenderer<C extends UIComponentWindowMinecraft>
				extends UIComponentWindowMinecraft.DefaultRenderer<C> {
			public static final int CURSOR_SHAPE_RADIUS = 10;

			protected final Random random = new Random();

			@UIRendererConstructor(type = UIRendererConstructor.ConstructorType.MAPPING__CONTAINER_CLASS)
			public CustomWindowRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, Class<C> containerClass) { super(mapping, containerClass); }

			@Override
			public void render(C container, EnumRenderStage stage, IAffineTransformStack stack, Point2D cursorPosition, double partialTicks) {
				super.render(container, stage, stack, cursorPosition, partialTicks);
				if (stage == EnumRenderStage.PRE_CHILDREN) {
					AffineTransform transform = AssertionUtilities.assertNonnull(stack.getDelegated().peek());
					Shape transformed = transform.createTransformedShape(new Ellipse2D.Double(
							cursorPosition.getX() - CURSOR_SHAPE_RADIUS, cursorPosition.getY() - CURSOR_SHAPE_RADIUS,
							CURSOR_SHAPE_RADIUS << 1, CURSOR_SHAPE_RADIUS << 1));
					DrawingUtilities.drawShape(transformed, true, new Color(getRandom().nextInt(), true), 0);
				}
			}

			protected Random getRandom() { return random; }
		}

		@OnlyIn(Dist.CLIENT)
		private static final class ViewModel
				extends UIViewModelMinecraft<Model> {
			protected final IBindingField<Integer> anchoredWindowBorderColor = new BindingField<>(
					new NamespacePrefixedString("anchoredWindowBorderColor"),
					new ObservableField<>(Integer.class, null));
			protected final IBindingMethod.Destination<UIComponentButton.IUIEventActivate> buttonOnActivate = new BindingMethodDestination<>(
					UIComponentButton.IUIEventActivate.class,
					new NamespacePrefixedString("buttonOnActivate"),
					this::onButtonActivate);
			protected boolean anchoredWindowFlickering = false;
			protected final Random random = new Random();
			protected final IBindingMethod.Destination<IUIEvent> buttonOnActivated = new BindingMethodDestination<>(
					IUIEvent.class,
					new NamespacePrefixedString("buttonOnActivated"),
					this::onButtonActivated);

			private ViewModel() { super(new Model()); }

			@Override
			public void tick() {
				if (isAnchoredWindowFlickering())
					getAnchoredWindowBorderColor().setValue(getRandom().nextInt());
			}

			protected boolean isAnchoredWindowFlickering() { return anchoredWindowFlickering; }

			protected void setAnchoredWindowFlickering(boolean anchoredWindowFlickering) { this.anchoredWindowFlickering = anchoredWindowFlickering; }

			@SuppressWarnings("unused")
			protected IBindingMethod.Destination<IUIEvent> getButtonOnActivated() { return buttonOnActivated; }

			@SuppressWarnings("unused")
			protected IBindingMethod.Destination<UIComponentButton.IUIEventActivate> getButtonOnActivate() { return buttonOnActivate; }

			protected void onButtonActivate(UIComponentButton.IUIEventActivate e) {
				boolean ret = false;
				IUIEvent ec = e.getCause();
				if (ec instanceof IUIEventMouse) {
					IUIEventMouse ecc = (IUIEventMouse) ec;
					if (ecc.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
						ret = true;
				} else if (ec instanceof IUIEventKeyboard) {
					IUIEventKeyboard ecc = (IUIEventKeyboard) ec;
					if (ecc.getData().getKey() == GLFW.GLFW_KEY_ENTER)
						ret = true;
				}
				if (!ret)
					e.preventDefault();
			}

			protected void onButtonActivated(IUIEvent e) { setAnchoredWindowFlickering(!isAnchoredWindowFlickering()); }

			protected IBindingField<Integer> getAnchoredWindowBorderColor() { return anchoredWindowBorderColor; }

			protected Random getRandom() { return random; }
		}

		@OnlyIn(Dist.CLIENT)
		private static final class Model
				extends UIModel {}
	}

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

			private static final ContainerType<DebugContainer> INSTANCE = IForgeContainerType.create((windowId, inv, data) ->
					new DebugContainer(windowId, AssertionUtilities.assertNonnull(Minecraft.getInstance().world), data.readBlockPos()));
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
