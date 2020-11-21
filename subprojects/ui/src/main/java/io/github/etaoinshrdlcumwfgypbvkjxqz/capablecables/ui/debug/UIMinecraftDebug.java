package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ComponentTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ComponentUI;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIFacade;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControl;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.UINamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls.UIStandardAnimationControlFactory;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.easings.EnumUICommonAnimationEasing;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.targets.UIAnimationTargetUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.IUIMinecraftInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.adapters.AbstractContainerScreenAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.viewmodels.UIDefaultMinecraftViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.UIDefaultMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.models.UIAbstractModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.UIComponentCursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.UIButtonComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.UIWindowComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIDefaultComponentSchemaHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBImmutableAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.RectangularShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIEmptyTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ColorUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding.EnumBindingType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.MemoryObservableField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;
import io.netty.buffer.Unpooled;
import io.reactivex.rxjava3.observers.DisposableObserver;
import jakarta.xml.bind.JAXBException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;
import static java.util.Objects.requireNonNull;

public enum UIMinecraftDebug {
	;

	@NonNls
	public static final String PATH = "debug_ui";
	private static final ITextComponent DISPLAY_NAME = MinecraftTextComponentUtilities.getEmpty();

	public static Block getBlockEntry() { return DebugBlock.getInstance(); }

	public static TileEntityType<DebugTileEntity> getTileEntityEntry() { return DebugTileEntity.getTypeInstance(); }

	public static ContainerType<DebugContainer> getContainerEntry() { return DebugContainer.getTypeInstance(); }

	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public static void registerUIFactory() {
		{
			ContainerType<DebugContainer> containerType = DebugContainer.getTypeInstance();
			DebugUI uiFactory = DebugUI.INSTANCE;
			// COMMENT this should mean that classloading is executed in parallel
			DeferredWorkQueue.runLater(() ->
					ScreenManager.registerFactory(containerType, uiFactory));
		}
	}

	public static String getPath() { return PATH; }

	public static ITextComponent getDisplayName() { return DISPLAY_NAME; }

	@OnlyIn(Dist.CLIENT)
	private enum DebugUI
			implements ScreenManager.IScreenFactory<DebugContainer, AbstractContainerScreenAdapter<? extends IUIMinecraftInfrastructure<?, ?, ?>, DebugContainer>> {
		INSTANCE,
		;

		public static final @NonNls String COMPONENT_TEST_XML_PATH = "components-test.xml";
		public static final @NonNls String COMPONENT_TEST_THEME_XML_PATH = "components-test-theme.xml";
		private static final ComponentUI JAXB_VIEW;
		private static final ComponentTheme JAXB_THEME;

		static {
			{
				// COMMENT view parser
				InputStream is = AssertionUtilities.assertNonnull(DebugUI.class.getResourceAsStream(getComponentTestXMLPath()));
				try {
					try {
						JAXB_VIEW = (ComponentUI) UIDefaultComponentSchemaHolder.getContext().createUnmarshaller().unmarshal(is);
					} catch (JAXBException e) {
						throw ThrowableUtilities.propagate(e);
					}
				} finally {
					ThrowableUtilities.runQuietly(is::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
				}
			}
			{
				// COMMENT theme parser
				InputStream is = AssertionUtilities.assertNonnull(DebugUI.class.getResourceAsStream(getComponentTestThemeXMLPath()));
				try {
					try {
						JAXB_THEME = (ComponentTheme) UIDefaultComponentSchemaHolder.getContext().createUnmarshaller().unmarshal(is);
					} catch (JAXBException e) {
						throw ThrowableUtilities.propagate(e);
					}
				} finally {
					ThrowableUtilities.runQuietly(is::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
				}
			}

			// COMMENT early check
			IUIMinecraftViewComponent<?, ?> view = createView();
			IUITheme theme = createTheme();
			IUIView.getThemeStack(view).push(theme);
		}

		private static String getComponentTestXMLPath() { return COMPONENT_TEST_XML_PATH; }

		private static String getComponentTestThemeXMLPath() { return COMPONENT_TEST_THEME_XML_PATH; }

		@Override
		public AbstractContainerScreenAdapter<? extends IUIMinecraftInfrastructure<?, ?, ?>, DebugContainer> create(DebugContainer container, PlayerInventory inv, ITextComponent title) {
			return createUI(container);
		}

		private static AbstractContainerScreenAdapter<? extends IUIMinecraftInfrastructure<?, ?, ?>, DebugContainer> createUI(DebugContainer container) {
			IBinder binder = new DefaultBinder();
			// COMMENT Color <-> Integer
			binder.addTransformer(EnumBindingType.FIELD, Color::getRGB);
			binder.addTransformer(EnumBindingType.FIELD, ColorUtilities::ofRGBA);

			AbstractContainerScreenAdapter<? extends IUIMinecraftInfrastructure<?, ?, ?>, DebugContainer> screen =
					UIFacade.Minecraft.createScreen(
							getDisplayName(),
							UIFacade.Minecraft.createInfrastructure(
									createView(),
									new ViewModel(),
									binder
							),
							container);
			IUITheme theme = createTheme();

			IUIMinecraftView<?> view = screen.getInfrastructure().getView();
			IExtensionContainer.addExtensionChecked(view, new UIComponentCursorHandleProviderExtension());
			IUIView.getThemeStack(view).push(theme);

			return screen;
		}

		private static IUIMinecraftViewComponent<?, ?> createView() {
			return (IUIMinecraftViewComponent<?, ?>) IJAXBAdapterRegistry.adaptFromJAXB(
					JAXBImmutableAdapterContext.of(UIDefaultComponentSchemaHolder.getAdapterRegistry()),
					getJAXBView()
			);
		}

		private static IUITheme createTheme() {
			return (IUITheme) IJAXBAdapterRegistry.adaptFromJAXB(
					JAXBImmutableAdapterContext.of(UIDefaultComponentSchemaHolder.getAdapterRegistry()),
					getJAXBTheme()
			);
		}

		public static ComponentUI getJAXBView() {
			return JAXB_VIEW;
		}

		public static ComponentTheme getJAXBTheme() {
			return JAXB_THEME;
		}

		@SuppressWarnings("unused")
		@OnlyIn(Dist.CLIENT)
		private static final class CustomWindowMinecraftComponentRenderer<C extends UIMinecraftWindowComponent>
				extends UIMinecraftWindowComponent.DefaultRenderer<C>
				implements IShapeDescriptorProvider {
			public static final int CURSOR_SHAPE_RADIUS = 10;

			private Color cursorHighlighterColor = Color.WHITE;
			private final IUIAnimationControl animations =
					UIStandardAnimationControlFactory.createSimple(UIStandardAnimationControlFactory.EnumDirection.ALTERNATE,
							UIAnimationTargetUtilities.andThen(
									UIAnimationTargetUtilities.range(rgb -> setCursorHighlighterColor(new Color((int) rgb)), 0, 0xFFFFFFL, EnumUICommonAnimationEasing.LINEAR),
									UIAnimationTargetUtilities.range(UIAnimationTargetUtilities.ShapeDescriptors.translateY(suppressThisEscapedWarning(() -> this)), 0, 100, EnumUICommonAnimationEasing.IN_OUT_BOUNCE)
							),
							ITicker.StaticHolder.getSystemTicker(),
							true,
							TimeUnit.SECONDS.toNanos(5),
							TimeUnit.SECONDS.toNanos(1),
							TimeUnit.SECONDS.toNanos(2),
							UIStandardAnimationControlFactory.getInfiniteLoop());

			@UIRendererConstructor
			public CustomWindowMinecraftComponentRenderer(IUIRendererArguments arguments) { super(arguments); }

			@Override
			@SuppressWarnings({"rawtypes", "RedundantSuppression"})
			public void onRendererAdded(C container) {
				super.onRendererAdded(container);
				getAnimations().reset();
				getContainer()
						.flatMap(IUIComponent::getManager)
						.flatMap(IUIComponentManager::getView)
						.map(IUIView::getAnimationController)
						.ifPresent(animationController ->
								animationController.add(getAnimations()));
			}

			protected IUIAnimationControl getAnimations() { return animations; }

			@Override
			@SuppressWarnings({"rawtypes", "RedundantSuppression"})
			public void onRendererRemoved() {
				getContainer()
						.flatMap(IUIComponent::getManager)
						.flatMap(IUIComponentManager::getView)
						.map(IUIView::getAnimationController)
						.ifPresent(animationController ->
								animationController.remove(getAnimations()));
				getAnimations().reset();
				super.onRendererRemoved();
			}

			@Override
			public void render(IUIComponentContext context, EnumRenderStage stage, C component, double partialTicks) {
				super.render(context, stage, component, partialTicks);
				if (stage.isPreChildren()) {
					context.getViewContext().getInputDevices().getPointerDevice()
							.map(IInputPointerDevice::getPositionView)
							.ifPresent(pointerPosition -> {
								Shape relativeShape = new Ellipse2D.Double(
										pointerPosition.getX() - CURSOR_SHAPE_RADIUS, pointerPosition.getY() - CURSOR_SHAPE_RADIUS,
										CURSOR_SHAPE_RADIUS << 1, CURSOR_SHAPE_RADIUS << 1);
								try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
									graphics.setColor(getCursorHighlighterColor());
									graphics.fill(relativeShape);
								}
							});
				}
			}

			protected Color getCursorHighlighterColor() { return cursorHighlighterColor; }

			protected void setCursorHighlighterColor(Color cursorHighlighterColor) { this.cursorHighlighterColor = cursorHighlighterColor; }

			@Override
			public IShapeDescriptor<?> getShapeDescriptor() {
				return getContainer().orElseThrow(AssertionError::new).getShapeDescriptor();
			}

			@Override
			public boolean modifyShape(BooleanSupplier action) throws ConcurrentModificationException {
				return getContainer().orElseThrow(AssertionError::new).modifyShape(action);
			}

			@Override
			public boolean isModifyingShape() {
				return getContainer().orElseThrow(AssertionError::new).isModifyingShape();
			}

			@Override
			public Shape getAbsoluteShape() throws IllegalStateException { return getContainer().orElseThrow(AssertionError::new).getAbsoluteShape(); }
		}

		@OnlyIn(Dist.CLIENT)
		private static final class ViewModel
				extends UIDefaultMinecraftViewModel<Model> {
			private final IBindingField<Integer> anchoredWindowBorderColor = new ImmutableBindingField<>(
					ImmutableNamespacePrefixedString.of(UINamespaceUtilities.getRendererBindingNamespace(), "anchoredWindowBorderColor"),
					new MemoryObservableField<>(Integer.class, ColorUtilities.getColorless().getRGB()));
			private final IBindingMethodDestination<UIButtonComponent.IUIEventActivate> buttonOnActivate = new ImmutableBindingMethodDestination<>(
					UIButtonComponent.IUIEventActivate.class,
					ImmutableNamespacePrefixedString.of(UINamespaceUtilities.getViewBindingNamespace(), "buttonOnActivate"),
					this::onButtonActivate);
			private final Random random = new Random();
			private boolean anchoredWindowFlickering = false;
			private final IBindingMethodDestination<IUIEvent> buttonOnActivated = new ImmutableBindingMethodDestination<>(
					IUIEvent.class,
					ImmutableNamespacePrefixedString.of(UINamespaceUtilities.getViewBindingNamespace(), "buttonOnActivated"),
					this::onButtonActivated);

			@Override
			public void tick() {
				if (isAnchoredWindowFlickering())
					getAnchoredWindowBorderColor().setValue(getRandom().nextInt());
			}

			private ViewModel() { super(new Model()); }

			protected boolean isAnchoredWindowFlickering() { return anchoredWindowFlickering; }

			protected void setAnchoredWindowFlickering(boolean anchoredWindowFlickering) { this.anchoredWindowFlickering = anchoredWindowFlickering; }

			@SuppressWarnings("unused")
			protected IBindingMethodDestination<IUIEvent> getButtonOnActivated() { return buttonOnActivated; }

			@SuppressWarnings("unused")
			protected IBindingMethodDestination<UIButtonComponent.IUIEventActivate> getButtonOnActivate() { return buttonOnActivate; }

			protected void onButtonActivate(UIButtonComponent.IUIEventActivate e) {
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

			@Override
			@OverridingMethodsMustInvokeSuper
			public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
				super.initializeBindings(binderObserverSupplier);
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
						() -> ImmutableBinderAction.bind(
								getAnchoredWindowBorderColor(),
								getButtonOnActivate(), getButtonOnActivated()
						)
				);
			}

			@Override
			@OverridingMethodsMustInvokeSuper
			public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
				super.cleanupBindings(binderObserverSupplier);
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
						() -> ImmutableBinderAction.unbind(
								getAnchoredWindowBorderColor(),
								getButtonOnActivate(), getButtonOnActivated()
						)
				);
			}
		}

		@OnlyIn(Dist.CLIENT)
		private static final class Model
				extends UIAbstractModel {}
	}

	private static final class DebugContainer extends Container {
		private static final Supplier<@Nonnull ContainerType<DebugContainer>> TYPE_INSTANCE = Suppliers.memoize(() ->
				IForgeContainerType.create((windowID, inv, data) ->
						new DebugContainer(windowID, AssertionUtilities.assertNonnull(inv.player.getEntityWorld().getTileEntity(data.readBlockPos())))));
		private final TileEntity tileEntity;

		private DebugContainer(int id, TileEntity tileEntity) {
			super(getTypeInstance(), id);
			this.tileEntity = tileEntity;
		}

		private static ContainerType<DebugContainer> getTypeInstance() { return AssertionUtilities.assertNonnull(TYPE_INSTANCE.get()); }

		@Override
		public boolean canInteractWith(PlayerEntity playerIn) {
			return isWithinUsableDistance(IWorldPosCallable.of(requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerIn, DebugBlock.getInstance());
		}
	}

	private static final class DebugBlock extends Block {
		private static final Supplier<@Nonnull DebugBlock> INSTANCE = Suppliers.memoize(DebugBlock::new);

		private static DebugBlock getInstance() { return INSTANCE.get(); }

		private DebugBlock() {
			super(Properties.from(Blocks.STONE));
		}

		@Override
		public boolean hasTileEntity(BlockState state) { return true; }

		@Override
		public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new DebugTileEntity(); }

		@SuppressWarnings("deprecation")
		@Override
		public @Nonnull ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
			if (!worldIn.isRemote()) {
				TileEntity tileEntity = AssertionUtilities.assertNonnull(worldIn.getTileEntity(pos));
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
			}
			return ActionResultType.SUCCESS;
		}
	}

	private static final class DebugTileEntity extends TileEntity implements INamedContainerProvider {
		private static final Supplier<@Nonnull TileEntityType<DebugTileEntity>> TYPE_INSTANCE = Suppliers.memoize(() ->
				TileEntityType.Builder.create(DebugTileEntity::new, DebugBlock.getInstance()).build(null));

		private DebugTileEntity() {
			super(getTypeInstance());
		}

		private static TileEntityType<DebugTileEntity> getTypeInstance() {
			return AssertionUtilities.assertNonnull(TYPE_INSTANCE.get());
		}

		@Override
		public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
			return DebugContainer.getTypeInstance().create(id, playerInventory, new PacketBuffer(Unpooled.buffer(Long.BYTES)).writeBlockPos(getPos()));
		}

		@Override
		public @Nonnull ITextComponent getDisplayName() { return UIMinecraftDebug.getDisplayName(); }
	}
}
