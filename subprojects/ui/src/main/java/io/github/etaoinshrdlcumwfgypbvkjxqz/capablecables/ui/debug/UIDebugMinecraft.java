package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Ui;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIFacade;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.IUIResourceParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserCheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.adapters.AbstractContainerScreenAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.viewmodels.UIViewModelMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.impl.UIComponentWindowMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.parsers.DefaultMinecraftUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.models.UIModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.UIExtensionCursorHandleProviderComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.impl.UIComponentButton;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.DefaultUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.DefaultUIComponentSchemaHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.minecraft.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.Binder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinding.EnumBindingType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.fields.BindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.fields.ObservableField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.methods.BindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
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

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public enum UIDebugMinecraft {
	;

	@NonNls
	private static final String PATH = "debug_ui";
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
			implements ScreenManager.IScreenFactory<DebugContainer, AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer>> {
		INSTANCE,
		;

		@NonNls
		public static final String COMPONENT_TEST_XML_PATH = "components_test.xml";
		private static final IUIResourceParser<IUIViewComponentMinecraft<?, ?>, Ui> PARSER =
				DefaultMinecraftUIComponentParser.makeParserMinecraft(
						DefaultUIComponentParser.makeParserStandard(
								new DefaultMinecraftUIComponentParser<>(
										CastUtilities.castUnchecked(IUIViewComponentMinecraft.class) // COMMENT should not matter
								)));

		static {
			InputStream is = AssertionUtilities.assertNonnull(DebugUI.class.getResourceAsStream(COMPONENT_TEST_XML_PATH));
			try {
				try {
					PARSER.parse((Ui) DefaultUIComponentSchemaHolder.getContext().createUnmarshaller().unmarshal(is));
				} catch (UIParserCheckedException | JAXBException e) {
					throw ThrowableUtilities.propagate(e);
				}
			} finally {
				ThrowableUtilities.runQuietly(is::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
			}
			try {
				PARSER.construct(); // COMMENT early check
			} catch (UIParserCheckedException e) {
				throw ThrowableUtilities.propagate(e);
			}
		}

		@Override
		public AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> create(DebugContainer container, PlayerInventory inv, ITextComponent title) {
			return createUI(container);
		}

		private static AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> createUI(DebugContainer container) {
			IBinder binder = new Binder();
			// COMMENT Color <-> Integer
			binder.addTransformer(EnumBindingType.FIELD, (Color color) -> Optional.ofNullable(color).map(Color::getRGB).orElse(null));
			binder.addTransformer(EnumBindingType.FIELD, (Integer t) -> Optional.ofNullable(t).map(i -> new Color(i, true)).orElse(null));

			AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> ret;
			try {
				ret = UIFacade.Minecraft.createScreen(
						getDisplayName(),
						UIFacade.Minecraft.createInfrastructure(
								PARSER.construct(),
								new ViewModel(),
								binder
						),
						container);
			} catch (UIParserCheckedException e) {
				throw ThrowableUtilities.propagate(e);
			}
			IExtensionContainer.StaticHolder.addExtensionChecked(ret.getInfrastructure().getView(),
					new UIExtensionCursorHandleProviderComponent<>(IUIViewComponent.class));

			return ret;
		}

		@SuppressWarnings("unused")
		private static final class CustomWindowRenderer<C extends UIComponentWindowMinecraft>
				extends UIComponentWindowMinecraft.DefaultRenderer<C> {
			public static final int CURSOR_SHAPE_RADIUS = 10;

			protected final Random random = new Random();

			@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS__CONTAINER_CLASS)
			public CustomWindowRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, Class<C> containerClass) { super(mappings, containerClass); }

			@Override
			public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {
				super.render(context, container, stage, partialTicks);
				if (stage.isPreChildren()) {
					context.getViewContext().getInputDevices().getPointerDevice()
							.map(IInputPointerDevice::getPositionView)
							.ifPresent(pointerPosition -> {
								Shape transformed = IUIComponentContext.StaticHolder.createContextualShape(
										context,
										new Ellipse2D.Double(
												pointerPosition.getX() - CURSOR_SHAPE_RADIUS, pointerPosition.getY() - CURSOR_SHAPE_RADIUS,
												CURSOR_SHAPE_RADIUS << 1, CURSOR_SHAPE_RADIUS << 1)
								);
								MinecraftDrawingUtilities.drawShape(transformed, true, new Color(getRandom().nextInt(), true), 0);
							});
				}
			}

			protected Random getRandom() { return random; }
		}

		@OnlyIn(Dist.CLIENT)
		private static final class ViewModel
				extends UIViewModelMinecraft<Model> {
			protected final IBindingField<Integer> anchoredWindowBorderColor = new BindingField<>(
					new ImmutableNamespacePrefixedString("anchoredWindowBorderColor"),
					new ObservableField<>(Integer.class, null));
			protected final IBindingMethodDestination<UIComponentButton.IUIEventActivate> buttonOnActivate = new BindingMethodDestination<>(
					UIComponentButton.IUIEventActivate.class,
					new ImmutableNamespacePrefixedString("buttonOnActivate"),
					this::onButtonActivate);
			protected boolean anchoredWindowFlickering = false;
			protected final Random random = new Random();
			protected final IBindingMethodDestination<IUIEvent> buttonOnActivated = new BindingMethodDestination<>(
					IUIEvent.class,
					new ImmutableNamespacePrefixedString("buttonOnActivated"),
					this::onButtonActivated);

			private ViewModel() { super(new Model()); }

			@Override
			public void tick(IUIViewModelContext context) {
				if (isAnchoredWindowFlickering())
					getAnchoredWindowBorderColor().setValue(getRandom().nextInt());
			}

			protected boolean isAnchoredWindowFlickering() { return anchoredWindowFlickering; }

			protected void setAnchoredWindowFlickering(boolean anchoredWindowFlickering) { this.anchoredWindowFlickering = anchoredWindowFlickering; }

			@SuppressWarnings("unused")
			protected IBindingMethodDestination<IUIEvent> getButtonOnActivated() { return buttonOnActivated; }

			@SuppressWarnings("unused")
			protected IBindingMethodDestination<UIComponentButton.IUIEventActivate> getButtonOnActivate() { return buttonOnActivate; }

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
		public ITextComponent getDisplayName() { return UIDebugMinecraft.getDisplayName(); }
	}
}
