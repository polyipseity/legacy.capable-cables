package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Theme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Ui;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIFacade;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControl;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.IUIParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserCheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls.UIStandardAnimationControlFactory;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.easings.EnumUICommonAnimationEasing;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.targets.UIAnimationTargetUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.IUIMinecraftInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.adapters.AbstractContainerScreenAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.viewmodels.UIDefaultMinecraftViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.impl.UIMinecraftWindowComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.parsers.UIDefaultMinecraftComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.models.UIAbstractModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.UIComponentCursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.UIButtonComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.UIDefaultComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.UIDefaultComponentSchemaHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.UIDefaultComponentThemeParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding.EnumBindingType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

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

		public static final @NonNls String COMPONENT_TEST_XML_PATH = "components_test.xml";
		public static final @NonNls String COMPONENT_TEST_THEME_XML_PATH = "components_test_theme.xml";
		private static final IUIParser<IUIMinecraftViewComponent<?, ?>, Ui> VIEW_PARSER =
				UIDefaultMinecraftComponentParser.makeParserMinecraft(
						UIDefaultComponentParser.makeParserStandard(
								new UIDefaultMinecraftComponentParser<>(
										CastUtilities.castUnchecked(IUIMinecraftViewComponent.class) // COMMENT should not matter
								)));
		private static final IUIParser<IUITheme, Theme> THEME_PARSER =
				UIDefaultComponentThemeParser.makeParserStandard(
						new UIDefaultComponentThemeParser()
				);

		static {
			{
				// COMMENT view parser
				InputStream is = AssertionUtilities.assertNonnull(DebugUI.class.getResourceAsStream(getComponentTestXmlPath()));
				try {
					try {
						getViewParser().parse((Ui) UIDefaultComponentSchemaHolder.getContext().createUnmarshaller().unmarshal(is));
					} catch (UIParserCheckedException | JAXBException e) {
						throw ThrowableUtilities.propagate(e);
					}
				} finally {
					ThrowableUtilities.runQuietly(is::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
				}
			}
			{
				// COMMENT theme parser
				InputStream is = AssertionUtilities.assertNonnull(DebugUI.class.getResourceAsStream(getComponentTestThemeXmlPath()));
				try {
					try {
						getThemeParser().parse((Theme) UIDefaultComponentSchemaHolder.getContext().createUnmarshaller().unmarshal(is));
					} catch (UIParserCheckedException | JAXBException e) {
						throw ThrowableUtilities.propagate(e);
					}
				} finally {
					ThrowableUtilities.runQuietly(is::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
				}
			}

			try {
				// COMMENT early check
				IUIMinecraftViewComponent<?, ?> view = getViewParser().construct();
				IUITheme theme = getThemeParser().construct();
				view.getThemeStack().push(theme);
			} catch (UIParserCheckedException e) {
				throw ThrowableUtilities.propagate(e);
			}
		}

		private static String getComponentTestXmlPath() { return COMPONENT_TEST_XML_PATH; }

		private static String getComponentTestThemeXmlPath() { return COMPONENT_TEST_THEME_XML_PATH; }

		@Override
		public AbstractContainerScreenAdapter<? extends IUIMinecraftInfrastructure<?, ?, ?>, DebugContainer> create(DebugContainer container, PlayerInventory inv, ITextComponent title) {
			return createUI(container);
		}

		private static AbstractContainerScreenAdapter<? extends IUIMinecraftInfrastructure<?, ?, ?>, DebugContainer> createUI(DebugContainer container) {
			IBinder binder = new DefaultBinder();
			// COMMENT Color <-> Integer
			binder.addTransformer(EnumBindingType.FIELD, (Color color) ->
					Optional.ofNullable(color).map(Color::getRGB).orElse(null));
			binder.addTransformer(EnumBindingType.FIELD, (Integer t) ->
					Optional.ofNullable(t).map(i -> new Color(i, true)).orElse(null));

			AbstractContainerScreenAdapter<? extends IUIMinecraftInfrastructure<?, ?, ?>, DebugContainer> screen;
			IUITheme theme;
			try {
				screen = UIFacade.Minecraft.createScreen(
						getDisplayName(),
						UIFacade.Minecraft.createInfrastructure(
								getViewParser().construct(),
								new ViewModel(),
								binder
						),
						container);
				theme = getThemeParser().construct();
			} catch (UIParserCheckedException e) {
				throw ThrowableUtilities.propagate(e);
			}

			IUIMinecraftView<?> view = screen.getInfrastructure().getView();
			IExtensionContainer.addExtensionChecked(view, new UIComponentCursorHandleProviderExtension());
			view.getThemeStack().push(theme);

			return screen;
		}

		private static IUIParser<IUIMinecraftViewComponent<?, ?>, Ui> getViewParser() { return VIEW_PARSER; }

		private static IUIParser<IUITheme, Theme> getThemeParser() { return THEME_PARSER; }

		@SuppressWarnings("unused")
		@OnlyIn(Dist.CLIENT)
		private static final class CustomWindowMinecraftComponentRenderer<C extends UIMinecraftWindowComponent>
				extends UIMinecraftWindowComponent.DefaultRenderer<C>
				implements IShapeDescriptorProvider {
			public static final int CURSOR_SHAPE_RADIUS = 10;

			private Color cursorHighlighterColor = Color.WHITE;
			@SuppressWarnings("ThisEscapedInObjectConstruction")
			private final IUIAnimationControl animations =
					UIStandardAnimationControlFactory.createSimple(UIStandardAnimationControlFactory.EnumDirection.ALTERNATE,
							UIAnimationTargetUtilities.andThen(
									UIAnimationTargetUtilities.range(rgb -> setCursorHighlighterColor(new Color((int) rgb)), 0, 0xFFFFFFL, EnumUICommonAnimationEasing.LINEAR),
									UIAnimationTargetUtilities.range(UIAnimationTargetUtilities.ShapeDescriptors.translateY(this), 0, 100, EnumUICommonAnimationEasing.IN_OUT_BOUNCE)
							),
							ITicker.StaticHolder.getSystemTicker(),
							true,
							TimeUnit.SECONDS.toNanos(5),
							TimeUnit.SECONDS.toNanos(1),
							TimeUnit.SECONDS.toNanos(2),
							UIStandardAnimationControlFactory.getInfiniteLoop());

			@UIRendererConstructor
			public CustomWindowMinecraftComponentRenderer(UIRendererConstructor.IArguments arguments) { super(arguments); }

			@Override
			@SuppressWarnings({"rawtypes", "RedundantSuppression"})
			public void onRendererAdded(C container) {
				super.onRendererAdded(container);
				getAnimations().reset();
				getContainer()
						.flatMap(IUIComponent::getManager)
						.flatMap(IUIComponentManager::getView)
						.ifPresent(view ->
								view.getAnimationController().add(getAnimations()));
			}

			protected IUIAnimationControl getAnimations() { return animations; }

			@Override
			@SuppressWarnings({"rawtypes", "RedundantSuppression"})
			public void onRendererRemoved() {
				getContainer()
						.flatMap(IUIComponent::getManager)
						.flatMap(IUIComponentManager::getView)
						.ifPresent(view ->
								view.getAnimationController().remove(getAnimations()));
				getAnimations().reset();
				super.onRendererRemoved();
			}

			@Override
			public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {
				super.render(context, container, stage, partialTicks);
				if (stage.isPreChildren()) {
					context.getViewContext().getInputDevices().getPointerDevice()
							.map(IInputPointerDevice::getPositionView)
							.ifPresent(pointerPosition -> {
								Shape transformed = IUIComponentContext.createContextualShape(
										context,
										new Ellipse2D.Double(
												pointerPosition.getX() - CURSOR_SHAPE_RADIUS, pointerPosition.getY() - CURSOR_SHAPE_RADIUS,
												CURSOR_SHAPE_RADIUS << 1, CURSOR_SHAPE_RADIUS << 1)
								);
								MinecraftDrawingUtilities.drawShape(transformed, true, getCursorHighlighterColor(), 0);
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
					ImmutableNamespacePrefixedString.of(IHasBindingKey.StaticHolder.getDefaultNamespace(), "anchoredWindowBorderColor"),
					new MemoryObservableField<>(Integer.class, null));
			private final IBindingMethodDestination<UIButtonComponent.IUIEventActivate> buttonOnActivate = new ImmutableBindingMethodDestination<>(
					UIButtonComponent.IUIEventActivate.class,
					ImmutableNamespacePrefixedString.of(IHasBindingKey.StaticHolder.getDefaultNamespace(), "buttonOnActivate"),
					this::onButtonActivate);
			private final Random random = new Random();
			private boolean anchoredWindowFlickering = false;
			private final IBindingMethodDestination<IUIEvent> buttonOnActivated = new ImmutableBindingMethodDestination<>(
					IUIEvent.class,
					ImmutableNamespacePrefixedString.of(IHasBindingKey.StaticHolder.getDefaultNamespace(), "buttonOnActivated"),
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
			public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
				super.initializeBindings(binderObserverSupplier);
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
						() -> ImmutableBinderAction.bind(getAnchoredWindowBorderColor(), getButtonOnActivate(), getButtonOnActivated()));
			}
		}

		@OnlyIn(Dist.CLIENT)
		private static final class Model
				extends UIAbstractModel {}
	}

	private static final class DebugContainer extends Container {
		private static final Supplier<ContainerType<DebugContainer>> TYPE_INSTANCE = Suppliers.memoize(() ->
				IForgeContainerType.create((windowId, inv, data) ->
						new DebugContainer(windowId, AssertionUtilities.assertNonnull(MinecraftClientUtilities.getMinecraftNonnull().world), data.readBlockPos())));
		private final TileEntity tileEntity;

		private DebugContainer(int id, World world, BlockPos pos) {
			super(getTypeInstance(), id);
			tileEntity = requireNonNull(world.getTileEntity(pos));
		}

		private static ContainerType<DebugContainer> getTypeInstance() { return AssertionUtilities.assertNonnull(TYPE_INSTANCE.get()); }

		@Override
		public boolean canInteractWith(PlayerEntity playerIn) {
			return isWithinUsableDistance(IWorldPosCallable.of(requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerIn, DebugBlock.getInstance());
		}
	}

	private static final class DebugBlock extends Block {
		private static final Supplier<DebugBlock> INSTANCE = Suppliers.memoize(DebugBlock::new);

		private static DebugBlock getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

		private DebugBlock() {
			super(Properties.from(Blocks.STONE));
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
		private static final Supplier<TileEntityType<DebugTileEntity>> TYPE_INSTANCE = Suppliers.memoize(() ->
				TileEntityType.Builder.create(DebugTileEntity::new, DebugBlock.getInstance()).build(null));

		private DebugTileEntity() { super(requireNonNull(getTypeInstance())); }

		private static TileEntityType<DebugTileEntity> getTypeInstance() { return AssertionUtilities.assertNonnull(TYPE_INSTANCE.get()); }

		@Override
		public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) { return new DebugContainer(id, requireNonNull(getWorld()), getPos()); }

		@Override
		public ITextComponent getDisplayName() { return UIMinecraftDebug.getDisplayName(); }
	}
}
