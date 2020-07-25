package $group__.client.gui.components.roots;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.backgrounds.GuiBackground;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.EnumGuiMouseClickResult;
import $group__.client.gui.structures.GuiDragInfo;
import $group__.client.gui.traits.IGuiShapeRectangle;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.client.gui.utilities.GLUtilities;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.client.gui.utilities.GuiUtilities.DrawingUtilities;
import $group__.client.gui.utilities.TextComponents;
import $group__.client.gui.utilities.Tooltips;
import $group__.client.gui.utilities.Transforms.AffineTransforms;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;

import static $group__.utilities.Casts.castUncheckedUnboxedNonnull;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

// TODO fix onChangeFocus will never be called
@OnlyIn(CLIENT)
public abstract class GuiRoot<C extends Container> extends GuiContainer implements IGuiShapeRectangle, IGuiLifecycleHandler, IGuiReshapeHandler {
	@Nullable
	protected final C container;
	protected final Screen screen;
	protected final FakeParent fakeParent = new FakeParent();

	protected GuiRoot(ITextComponent title, @Nullable GuiBackground background) { this(title, background, null); }

	protected GuiRoot(ITextComponent title, @Nullable GuiBackground background, @Nullable C container) {
		super(getShapePlaceholder());
		this.container = container;
		screen = container == null ? new ScreenAdapted(title) : new ScreenAdaptedWithContainer(title);
		setBackground(background);
	}

	@Override
	protected Shape adaptToBounds(IGuiReshapeHandler handler, GuiComponent invoker, Rectangle2D rectangle) { return rectangle; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		GLUtilities.resetAll();
		super.render(stack, mouse, partialTicks);
	}

	@SuppressWarnings("SameReturnValue")
	public boolean isPaused() { return false; }

	@SuppressWarnings("SameReturnValue")
	public int getCloseKey() { return GLFW_KEY_ESCAPE; }

	public Optional<GuiBackground> getBackground() {
		if (!getChildren().isEmpty()) {
			GuiComponent ret = getChildren().get(0);
			if (ret instanceof GuiBackground) return Optional.of((GuiBackground) ret);
		}
		return Optional.empty();
	}

	public void setBackground(@Nullable GuiBackground background) {
		getBackground().ifPresent(this::remove);
		if (background != null) add(0, background);
	}

	protected Rectangle2D getRectangle() { return (Rectangle2D) super.getShape(); }

	@Override
	public Rectangle2D getRectangleView() { return (Rectangle2D) getRectangle().clone(); }

	@Override
	public void reshape(GuiComponent invoker) { reshape(invoker, getRectangle()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void reshape(GuiComponent invoker, Shape shape) { setBounds(this, invoker, shape.getBounds2D()); }

	@Override
	public void initialize(GuiComponent invoker) { onInitialize(this, invoker); }

	@Override
	public void tick(GuiComponent invoker) { onTick(this, invoker); }

	@Override
	public void close(GuiComponent invoker) { onClose(this, invoker); }

	@Override
	public void destroy(GuiComponent invoker) { onDestroyed(this, invoker); }

	@Override
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent invoker) {
		super.onInitialize(handler, invoker);
		reshape(this, new Rectangle2D.Double(0, 0, getScreen().width, getScreen().height));
	}

	@Override
	public void onClose(IGuiLifecycleHandler handler, GuiComponent invoker) {
		super.onClose(handler, invoker);

		// COMMENT synthetic events
		Point2D cursor = GLUtilities.getCursorPos();
		if (fakeParent.drag != null)
			onMouseDragged(new AffineTransformStack(), fakeParent.drag, (Point2D) cursor.clone(), fakeParent.drag.button);
		onMouseHovered(new AffineTransformStack(), (Point2D) cursor.clone());
		new ArrayDeque<>(keyPresses).forEach(k -> onKeyReleased(k.key, k.scanCode, k.modifiers));

		getScreen().getMinecraft().displayGuiScreen(null);
	}

	@Override
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		EnumGuiMouseClickResult ret = super.onMouseClicked(stack, drag, mouse, button);
		if (ret.result) fakeParent.drag = drag;
		return ret;
	}

	@Override
	public boolean onMouseReleased(AffineTransformStack stack, Point2D mouse, int button) {
		if (fakeParent.drag != null) {
			onMouseDragged(stack, fakeParent.drag, mouse, button);
			fakeParent.drag = null;
		}
		return super.onMouseReleased(stack, mouse, button);
	}

	@Override
	@Deprecated
	public final void onAdded(GuiContainer parent, int index) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

	@Override
	@Deprecated
	public final void onRemoved(GuiContainer parent) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

	@Override
	public boolean onKeyPressed(int key, int scanCode, int modifiers) {
		if (key == getCloseKey()) {
			close(this);
			return true;
		}
		return super.onKeyPressed(key, scanCode, modifiers);
	}

	@Override
	public Optional<GuiDragInfo> getDragInfo() { return Optional.ofNullable(fakeParent.drag); }

	@Override
	protected boolean isBeingDragged() { return fakeParent.drag != null; }

	@Override
	protected boolean isBeingHovered() { return true; }

	@OnlyIn(CLIENT)
	protected static class FakeParent {
		@Nullable
		protected GuiDragInfo drag = null;
	}

	////////// Screen Compatibility //////////

	public Screen getScreen() { return screen; }

	public <T extends Screen & IHasContainer<C>> T getContainerScreen() {
		if (container == null) throw BecauseOf.unsupportedOperation();
		return castUncheckedUnboxedNonnull(getScreen());
	}

	@OnlyIn(CLIENT)
	@SuppressWarnings("deprecation")
	protected class ScreenAdapted extends Screen {
		protected ScreenAdapted(ITextComponent title) { super(title); }

		@Override
		public void render(int mouseX, int mouseY, float partialTicks) { GuiRoot.this.render(mouseX, mouseY, partialTicks); }

		@Override
		public List<? extends IGuiEventListener> children() { return GuiRoot.this.children; }

		@Override
		public boolean isPauseScreen() { return isPaused(); }

		@Override
		protected void init() { GuiRoot.this.initialize(GuiRoot.this); }

		@Override
		public void resize(Minecraft client, int width, int height) {
			super.resize(client, width, height);
			//GuiRoot.this.onResize(); // resize calls init
		}

		@Override
		@Deprecated
		public void setSize(int width, int height) {
			super.setSize(width, height);
			GuiRoot.this.reshape(GuiRoot.this, new Rectangle2D.Double(0, 0, width, height));
		}

		@Override
		public void tick() { GuiRoot.this.tick(GuiRoot.this); }

		@Override
		public void onClose() { GuiRoot.this.close(GuiRoot.this); }

		@Override
		public void removed() { GuiRoot.this.destroy(GuiRoot.this); }

		@Override
		public boolean keyPressed(int key, int scanCode, int modifiers) { return GuiRoot.this.keyPressed(key, scanCode, modifiers); }

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) { return GuiRoot.this.mouseClicked(mouseX, mouseY, button); }

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button) { return GuiRoot.this.mouseReleased(mouseX, mouseY, button); }

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return GuiRoot.this.mouseDragged(mouseX, mouseY, button, mouseXDiff, mouseYDiff); }

		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) { return GuiRoot.this.mouseScrolled(mouseX, mouseY, scrollDelta); }

		@Override
		public boolean keyReleased(int key, int scanCode, int modifiers) { return GuiRoot.this.keyReleased(key, scanCode, modifiers); }

		@Override
		public boolean charTyped(char codePoint, int modifiers) { return GuiRoot.this.charTyped(codePoint, modifiers); }

		@Override
		public boolean changeFocus(boolean next) { return GuiRoot.this.changeFocus(next); }

		@Override
		public void mouseMoved(double mouseX, double mouseY) { GuiRoot.this.mouseMoved(mouseX, mouseY); }

		@Override
		public boolean isMouseOver(double mouseX, double mouseY) { return GuiRoot.this.isMouseOver(mouseX, mouseY); }

		@Override
		@Deprecated
		public boolean shouldCloseOnEsc() { return getCloseKey() == GLFW_KEY_ESCAPE; }

		@Override
		@Deprecated
		protected void renderTooltip(ItemStack item, int mouseX, int mouseY) { Tooltips.renderTooltip(getMinecraft(), width, height, font, itemRenderer, item, mouseX, mouseY); }

		@Override
		@Deprecated
		public List<String> getTooltipFromItem(ItemStack item) { return Tooltips.getTooltipFromItem(getMinecraft(), item); }

		@Override
		@Deprecated
		public void renderTooltip(String tooltip, int mouseX, int mouseY) { Tooltips.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

		@Override
		@Deprecated
		public void renderTooltip(List<String> tooltip, int mouseX, int mouseY) { Tooltips.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

		@Override
		@Deprecated
		public void renderTooltip(List<String> tooltip, int mouseX, int mouseY, FontRenderer font) { Tooltips.renderTooltip(width, height, itemRenderer, tooltip, mouseX, mouseY, font); }

		@Override
		@Deprecated
		protected void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { TextComponents.renderComponentHoverEffect(getMinecraft(), width, height, font, component, mouseX, mouseY); }

		@Override
		@Deprecated
		public boolean handleComponentClicked(ITextComponent component) { return TextComponents.handleComponentClicked(this, component); }

		@Override
		@Deprecated
		protected void hLine(int x1, int x2, int y, int color) { DrawingUtilities.hLine(AffineTransforms.getIdentity(), x1, x2, y, color); }

		@Override
		@Deprecated
		protected void vLine(int x, int y1, int y2, int color) { DrawingUtilities.vLine(AffineTransforms.getIdentity(), x, y1, y2, color); }

		@Override
		@Deprecated
		protected void fillGradient(int x1, int y1, int x2, int y2, int colorTop, int colorBottom) { DrawingUtilities.fillGradient(AffineTransforms.getIdentity(), GuiUtilities.ObjectUtilities.getRectangleFromDiagonal(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2)), colorTop, colorBottom, getBlitOffset()); }

		@Override
		@Deprecated
		public void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawCenteredString(AffineTransforms.getIdentity(), font, string, new Point2D.Double(x, y), color); }

		@Override
		@Deprecated
		public void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawRightAlignedString(AffineTransforms.getIdentity(), font, string, new Point2D.Double(x, y), color); }

		@Override
		@Deprecated
		public void drawString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawString(AffineTransforms.getIdentity(), font, string, new Point2D.Double(x, y), color); }

		@SuppressWarnings("MagicNumber")
		@Override
		@Deprecated
		public void blit(int x, int y, int u, int v, int w, int h) { DrawingUtilities.blit(AffineTransforms.getIdentity(), new Rectangle2D.Double(x, y, w, h), new Point2D.Double(u, v), new Dimension(256, 256), getBlitOffset()); }

		@Override
		@Deprecated
		public void renderBackground() { GuiUtilities.GuiBackgrounds.renderBackground(getMinecraft(), width, height); }

		@Override
		@Deprecated
		public void renderBackground(int blitOffset) { GuiUtilities.GuiBackgrounds.renderBackground(getMinecraft(), width, height, blitOffset); }

		@Override
		@Deprecated
		public void renderDirtBackground(int blitOffset) { GuiUtilities.GuiBackgrounds.renderDirtBackground(getMinecraft(), width, height, blitOffset); }
	}

	@OnlyIn(CLIENT)
	protected class ScreenAdaptedWithContainer extends ScreenAdapted implements IHasContainer<C> {
		protected ScreenAdaptedWithContainer(ITextComponent title) { super(title); }

		@Override
		public C getContainer() {
			assert container != null;
			return container;
		}
	}
}
