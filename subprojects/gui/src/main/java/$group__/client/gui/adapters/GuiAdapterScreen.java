package $group__.client.gui.adapters;

import $group__.client.gui.core.IGuiAdapter;
import $group__.client.gui.core.IGuiComponent;
import $group__.client.gui.core.IGuiController;
import $group__.client.gui.core.IGuiExtension;
import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.client.gui.core.structures.GuiKeyboardKeyPressData;
import $group__.client.gui.core.structures.GuiMouseButtonPressData;
import $group__.client.gui.core.structures.IShapeDescriptor;
import $group__.client.gui.structures.Dimension2DDouble;
import $group__.client.gui.utilities.*;
import $group__.utilities.CastUtilities;
import $group__.utilities.client.GLUtilities;
import $group__.utilities.client.TransformUtilities;
import $group__.utilities.structures.Registry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class GuiAdapterScreen
		<M extends IGuiComponent.IManager<?, ?, ?, ?, ? extends IShapeDescriptor<? extends Rectangle2D, ?>>>
		extends Screen
		implements IGuiAdapter<M> {
	protected final M manager;
	protected final Set<Integer> closeKeys, changeFocusKeys;
	protected boolean paused = false;

	public GuiAdapterScreen(ITextComponent titleIn, M manager) { this(titleIn, manager, ImmutableSet.of(GLFW.GLFW_KEY_ESCAPE), ImmutableSet.of(GLFW.GLFW_KEY_TAB)); }

	public GuiAdapterScreen(ITextComponent titleIn, M manager, Set<Integer> closeKeys, Set<Integer> changeFocusKeys) {
		super(titleIn);
		this.manager = manager;
		this.closeKeys = new HashSet<>(closeKeys);
		this.changeFocusKeys = new HashSet<>(changeFocusKeys);
		getManager().addExtension(new GuiExtensionScreen(this));
	}

	@Override
	public M getManager() { return manager; }

	public Set<Integer> getCloseKeysView() { return ImmutableSet.copyOf(getCloseKeys()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getCloseKeys() { return closeKeys; }

	public boolean addCloseKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getCloseKeys().add(k);
		return ret;
	}

	public boolean removeCloseKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getCloseKeys().remove(k);
		return ret;
	}

	public Set<Integer> getChangeFocusKeysView() { return ImmutableSet.copyOf(getChangeFocusKeys()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getChangeFocusKeys() { return closeKeys; }

	public boolean addChangeFocusKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getChangeFocusKeys().add(k);
		return ret;
	}

	public boolean removeChangeFocusKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getChangeFocusKeys().remove(k);
		return ret;
	}

	@Override
	@Deprecated
	public void render(int mouseX, int mouseY, float partialTicks) {
		AffineTransformStack stack = getManager().getCleanTransformStack();
		{
			// COMMENT generate ONGOING synthetic events
			IGuiController.IManager<?, ?> controller = getManager().getController();
			controller.onFocus(stack, IGuiController.EnumStage.ONGOING);
			controller.getKeyboardKeysBeingPressedView().forEach((c, k) ->
					controller.onKeyboardKeyPress(stack, IGuiController.EnumStage.ONGOING,
							k.getData()));
			Point2D cursor = GLUtilities.getCursorPos();
			controller.getMouseButtonsBeingPressedView().forEach((c, m) ->
					controller.onMouseButtonPress(stack, IGuiController.EnumStage.ONGOING,
							new GuiMouseButtonPressData(cursor, m.getData().getButton())));
		}
		getManager().getView().render(stack, new Point2D.Double(mouseX, mouseY), partialTicks);
	}

	@Override
	@Deprecated
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (getCloseKeys().contains(key)) {
			onClose();
			return true;
		}
		if (getChangeFocusKeys().contains(key)) {
			changeFocus((modifiers & GLFW.GLFW_MOD_SHIFT) == 0);
			return true;
		}
		return getManager().getController().onKeyboardKeyPress(getManager().getCleanTransformStack(), IGuiController.EnumStage.START, new GuiKeyboardKeyPressData(key, scanCode, modifiers));
	}

	@Override
	@Deprecated
	public boolean shouldCloseOnEsc() { return getCloseKeys().contains(GLFW.GLFW_KEY_ESCAPE); }

	@Override
	@Deprecated
	protected void renderTooltip(ItemStack item, int mouseX, int mouseY) { TooltipUtilities.renderTooltip(getMinecraft(), width, height, font, itemRenderer, item, mouseX, mouseY); }

	@Override
	@Deprecated
	public List<String> getTooltipFromItem(ItemStack item) { return TooltipUtilities.getTooltipFromItem(getMinecraft(), item); }

	@Override
	@Deprecated
	public void renderTooltip(String tooltip, int mouseX, int mouseY) { TooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY) { TooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY, FontRenderer font) { TooltipUtilities.renderTooltip(width, height, itemRenderer, tooltip, mouseX, mouseY, font); }

	@Override
	@Deprecated
	protected void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { TextComponentUtilities.renderComponentHoverEffect(getMinecraft(), width, height, font, component, mouseX, mouseY); }

	@Override
	@Deprecated
	public boolean handleComponentClicked(ITextComponent component) { return TextComponentUtilities.handleComponentClicked(this, component); }

	@Override
	@Deprecated
	public void setSize(int width, int height) {
		super.setSize(width, height);
		getManager().reshape(s -> {
			s.getShapeRef().setRect(0, 0, width, height);
			return true;
		});
	}

	@Override
	@Deprecated
	public List<? extends IGuiEventListener> children() { return ImmutableList.of(); }

	@Override
	@Deprecated
	protected void init() {
		setSize(width, height);
		AffineTransformStack stack = getManager().getCleanTransformStack();
		getManager().getModel().initialize(stack);
		// COMMENT generate START synthetic events
		getManager().getController().onMouseHover(stack, IGuiController.EnumStage.START, GLUtilities.getCursorPos());
	}

	@Override
	@Deprecated
	public void tick() { getManager().getModel().tick(getManager().getCleanTransformStack()); }

	@Override
	@Deprecated
	public void removed() {
		AffineTransformStack stack = getManager().getCleanTransformStack();
		{
			// COMMENT generate END synthetic events
			IGuiController.IManager<?, ?> controller = getManager().getController();
			controller.getKeyboardKeysBeingPressedView().forEach((c, k) ->
					controller.onKeyboardKeyPress(stack, IGuiController.EnumStage.END,
							k.getData()));
			Point2D cursor = GLUtilities.getCursorPos();
			controller.getMouseButtonsBeingPressedView().forEach((c, m) ->
					controller.onMouseButtonPress(stack, IGuiController.EnumStage.END,
							new GuiMouseButtonPressData(cursor, m.getData().getButton())));
			controller.onMouseHover(stack, IGuiController.EnumStage.END, cursor);
			controller.setFocus(stack, null);
		}
		getManager().getModel().close(stack);
	}

	@Override
	@Deprecated
	public void renderBackground() { GuiBackgrounds.renderBackgroundAndNotify(getMinecraft(), width, height); }

	@Override
	@Deprecated
	public void renderBackground(int blitOffset) { GuiBackgrounds.renderBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

	@Override
	@Deprecated
	public void renderDirtBackground(int blitOffset) { GuiBackgrounds.renderDirtBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

	@Override
	@Deprecated
	public boolean isPauseScreen() { return isPaused(); }

	public boolean isPaused() { return paused; }

	public void setPaused(boolean paused) { this.paused = paused; }

	@Override
	@Deprecated
	public void resize(Minecraft client, int width, int height) {
		super.resize(client, width, height);
		// COMMENT resize calls init
	}

	@Override
	@Deprecated
	public boolean mouseClicked(double mouseX, double mouseY, int button) { return getManager().getController().onMouseButtonPress(getManager().getCleanTransformStack(), IGuiController.EnumStage.START, new GuiMouseButtonPressData(new Point2D.Double(mouseX, mouseY), button)); }

	@Override
	@Deprecated
	public boolean mouseReleased(double mouseX, double mouseY, int button) { return getManager().getController().onMouseButtonPress(getManager().getCleanTransformStack(), IGuiController.EnumStage.END, new GuiMouseButtonPressData(new Point2D.Double(mouseX, mouseY), button)); }

	@Override
	@Deprecated
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return true; }

	@Override
	@Deprecated
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) { return getManager().getController().onMouseScroll(getManager().getCleanTransformStack(), new Point2D.Double(mouseX, mouseY), delta); }

	@Override
	@Deprecated
	public boolean keyReleased(int key, int scanCode, int modifiers) { return getManager().getController().onKeyboardKeyPress(getManager().getCleanTransformStack(), IGuiController.EnumStage.END, new GuiKeyboardKeyPressData(key, scanCode, modifiers)); }

	@Override
	@Deprecated
	public boolean charTyped(char codePoint, int modifiers) { return getManager().getController().onKeyboardCharType(getManager().getCleanTransformStack(), codePoint, modifiers); }

	@Override
	@Deprecated
	public boolean changeFocus(boolean next) { return getManager().getController().changeFocus(getManager().getCleanTransformStack(), next); }

	@Override
	@Deprecated
	public void mouseMoved(double mouseX, double mouseY) { getManager().getController().onMouseMove(getManager().getCleanTransformStack(), new Point2D.Double(mouseX, mouseY)); }

	@Override
	@Deprecated
	protected void hLine(int x1, int x2, int y, int color) { DrawingUtilities.hLine(TransformUtilities.AffineTransformUtilities.getIdentity(), x1, x2, y, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void vLine(int x, int y1, int y2, int color) { DrawingUtilities.vLine(TransformUtilities.AffineTransformUtilities.getIdentity(), x, y1, y2, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void fillGradient(int x1, int y1, int x2, int y2, int colorTop, int colorBottom) { DrawingUtilities.fillGradient(TransformUtilities.AffineTransformUtilities.getIdentity(), GuiObjectUtilities.getRectangleFromDiagonal(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2)), colorTop, colorBottom, getBlitOffset()); }

	@Override
	@Deprecated
	public void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawCenteredString(TransformUtilities.AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawRightAlignedString(TransformUtilities.AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawString(TransformUtilities.AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@SuppressWarnings("MagicNumber")
	@Override
	@Deprecated
	public void blit(int x, int y, int u, int v, int w, int h) { DrawingUtilities.blit(TransformUtilities.AffineTransformUtilities.getIdentity(), new Rectangle2D.Double(x, y, w, h), new Point2D.Double(u, v), new Dimension2DDouble(256, 256), getBlitOffset()); }

	@OnlyIn(Dist.CLIENT)
	public static class WithContainer
			<M extends IGuiComponent.IManager<?, ?, ?, ?, ? extends IShapeDescriptor<? extends Rectangle2D, ?>>,
					C extends Container>
			extends GuiAdapterScreen<M>
			implements IHasContainer<C> {

		protected final C container;

		protected WithContainer(ITextComponent titleIn, M manager, C container) {
			super(titleIn, manager);
			this.container = container;
			getManager().addExtension(new GuiExtensionContainer(getContainer()));
		}

		@Override
		public C getContainer() { return container; }

		@OnlyIn(Dist.CLIENT)
		public static class GuiExtensionContainer implements IGuiExtension {
			public static final ResourceLocation KEY = new ResourceLocation("container");
			public static final Registry.RegistryObject<IType<GuiExtensionContainer, IGuiComponent<?, ?, ?>>> TYPE = Reg.INSTANCE.register(KEY, new IType<GuiExtensionContainer, IGuiComponent<?, ?, ?>>() {
				@Override
				public Optional<GuiExtensionContainer> get(IGuiComponent<?, ?, ?> component) { return component.getExtension(KEY).map(CastUtilities::castUnchecked); }

				@Override
				public ResourceLocation getKey() { return KEY; }
			});
			protected final Container container;

			public GuiExtensionContainer(Container container) { this.container = container; }

			public Container getContainer() { return container; }

			@Override
			public IType<?, ?> getType() { return TYPE.getValue(); }

			@Override
			public void onExtensionAdd(IGuiComponent<?, ?, ?> container) {}

			@Override
			public void onExtensionRemove() {}
		}
	}

	public static class GuiExtensionScreen implements IGuiExtension {
		public static final ResourceLocation KEY = new ResourceLocation("container");
		public static final Registry.RegistryObject<IType<GuiExtensionScreen, IGuiComponent<?, ?, ?>>> TYPE = Reg.INSTANCE.register(KEY, new IType<GuiExtensionScreen, IGuiComponent<?, ?, ?>>() {
			@Override
			public Optional<GuiExtensionScreen> get(IGuiComponent<?, ?, ?> component) { return component.getExtension(KEY).map(CastUtilities::castUnchecked); }

			@Override
			public ResourceLocation getKey() { return KEY; }
		});
		protected final GuiAdapterScreen<?> screen;

		public GuiExtensionScreen(GuiAdapterScreen<?> screen) { this.screen = screen; }

		public GuiAdapterScreen<?> getScreen() { return screen; }

		@Override
		public IType<?, ?> getType() { return TYPE.getValue(); }

		@Override
		public void onExtensionAdd(IGuiComponent<?, ?, ?> container) {}

		@Override
		public void onExtensionRemove() {}
	}
}
