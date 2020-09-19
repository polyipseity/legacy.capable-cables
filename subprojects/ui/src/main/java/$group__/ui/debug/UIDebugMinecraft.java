package $group__.ui.debug;

import $group__.ui.UIConfiguration;
import $group__.ui.UIFacade;
import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.ui.core.mvvm.views.events.IUIEventKeyboard;
import $group__.ui.core.mvvm.views.events.IUIEventMouse;
import $group__.ui.core.parsers.IUIResourceParser;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import $group__.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import $group__.ui.minecraft.mvvm.adapters.AbstractContainerScreenAdapter;
import $group__.ui.minecraft.mvvm.components.common.UIComponentWindowMinecraft;
import $group__.ui.minecraft.mvvm.components.parsers.DefaultMinecraftUIComponentParser;
import $group__.ui.minecraft.mvvm.viewmodels.UIViewModelMinecraft;
import $group__.ui.mvvm.models.UIModel;
import $group__.ui.mvvm.views.components.common.UIComponentButton;
import $group__.ui.mvvm.views.components.extensions.UIExtensionCursorHandleProviderComponent;
import $group__.ui.parsers.components.DefaultUIComponentParser;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.binding.Binder;
import $group__.utilities.binding.core.IBinder;
import $group__.utilities.binding.core.IBinding.EnumBindingType;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.core.methods.IBindingMethodDestination;
import $group__.utilities.binding.fields.BindingField;
import $group__.utilities.binding.fields.ObservableField;
import $group__.utilities.binding.methods.BindingMethodDestination;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.minecraft.client.ClientUtilities;
import $group__.utilities.structures.NamespacePrefixedString;
import jakarta.xml.bind.Unmarshaller;
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
import net.minecraft.util.text.StringTextComponent;
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
import java.awt.geom.Point2D;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public enum UIDebugMinecraft {
	;

	@NonNls
	private static final String PATH = "debug_ui";
	private static final ITextComponent DISPLAY_NAME = new StringTextComponent("");

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

	public static String getPATH() { return PATH; }

	public static ITextComponent getDisplayName() { return DISPLAY_NAME; }

	@OnlyIn(Dist.CLIENT)
	private enum DebugUI
			implements ScreenManager.IScreenFactory<DebugContainer, AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer>> {
		INSTANCE,
		;

		@NonNls
		public static final String COMPONENT_TEST_XML_PATH = "components_test.xml";
		private static final IUIResourceParser<IUIViewComponentMinecraft<?, ?>, Function<? super Unmarshaller, ?>> PARSER =
				DefaultMinecraftUIComponentParser.makeParserMinecraft(
						DefaultUIComponentParser.makeParserStandard(
								new DefaultMinecraftUIComponentParser<>(
										CastUtilities.castUnchecked(IUIViewComponentMinecraft.class) // COMMENT should not matter
								)));

		static {
			Try.run(() -> PARSER.parse(u -> Try.call(() -> {
				try (InputStream is = AssertionUtilities.assertNonnull(DebugUI.class.getResourceAsStream(COMPONENT_TEST_XML_PATH))) {
					return u.unmarshal(is);
				}
			}, UIConfiguration.getInstance().getLogger()).orElseThrow(ThrowableCatcher::rethrow)), UIConfiguration.getInstance().getLogger());
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
			binder.addTransformer(EnumBindingType.FIELD, (Color color) -> Optional.ofNullable(color).map(Color::getRGB).orElse(null));
			binder.addTransformer(EnumBindingType.FIELD, (Integer t) -> Optional.ofNullable(t).map(i -> new Color(i, true)).orElse(null));

			AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, DebugContainer> ret =
					UIFacade.Minecraft.createScreen(
							new StringTextComponent(""),
							UIFacade.Minecraft.createInfrastructure(
									PARSER.construct(),
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

			@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS__CONTAINER_CLASS)
			public CustomWindowRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, Class<C> containerClass) { super(mappings, containerClass); }

			@Override
			public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {
				super.render(context, container, stage, partialTicks);
				if (stage == EnumRenderStage.PRE_CHILDREN) {
					Point2D cursorPosition = context.getCursorPositionView();
					Shape transformed = context.getTransformStack().element().createTransformedShape(new Ellipse2D.Double(
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
			protected final IBindingMethodDestination<UIComponentButton.IUIEventActivate> buttonOnActivate = new BindingMethodDestination<>(
					UIComponentButton.IUIEventActivate.class,
					new NamespacePrefixedString("buttonOnActivate"),
					this::onButtonActivate);
			protected boolean anchoredWindowFlickering = false;
			protected final Random random = new Random();
			protected final IBindingMethodDestination<IUIEvent> buttonOnActivated = new BindingMethodDestination<>(
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
					new DebugContainer(windowId, AssertionUtilities.assertNonnull(ClientUtilities.getMinecraftNonnull().world), data.readBlockPos()));
		}
	}

	private static final class DebugBlock extends Block {
		private static final DebugBlock INSTANCE = new DebugBlock();

		private DebugBlock() {
			super(Properties.from(Blocks.STONE));
			PreconditionUtilities.requireRunOnceOnly(UIConfiguration.getInstance().getLogger());
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
		public ITextComponent getDisplayName() { return UIDebugMinecraft.getDisplayName(); }

		private enum Type {
			;

			private static final TileEntityType<DebugTileEntity> INSTANCE = TileEntityType.Builder.create(DebugTileEntity::new, DebugBlock.INSTANCE).build(null);
		}
	}
}
