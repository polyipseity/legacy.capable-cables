package $group__.client.gui.components.roots;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.backgrounds.GuiBackground;
import $group__.client.gui.structures.*;
import $group__.client.gui.traits.IGuiShapeRectangle;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.client.gui.utilities.GLUtilities;
import $group__.client.gui.utilities.GLUtilities.GLStacks;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.client.gui.utilities.GuiUtilities.DrawingUtilities;
import $group__.client.gui.utilities.TextComponents;
import $group__.client.gui.utilities.Tooltips;
import $group__.client.gui.utilities.Transforms.AffineTransforms;
import $group__.utilities.specific.Maps;
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
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.Capacities.INITIAL_CAPACITY_1;
import static $group__.utilities.Capacities.INITIAL_CAPACITY_3;
import static $group__.utilities.Casts.castUncheckedUnboxedNonnull;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

@OnlyIn(CLIENT)
public abstract class GuiRoot<D extends GuiRoot.Data<?, C>, C extends Container> extends GuiContainer<D> implements IGuiShapeRectangle, IGuiLifecycleHandler, IGuiReshapeHandler {
	public final Screen screen;
	protected final FakeParent fakeParent = new FakeParent();
	protected List<Runnable> tasks = new ArrayList<>(INITIAL_CAPACITY_3);

	protected GuiRoot(ITextComponent title, D data) {
		super(getShapePlaceholder(), data);
		screen = makeScreen(title, data.container != null);

		data.events.dBackgroundSet.add(par -> {
			if (par.previous != null) remove(par.previous); // TODO add schedule function for render
			if (par.current != null) add(0, par.current);
		});
		schedule(() -> data.setBackground(data.background));
	}

	@SuppressWarnings("SameReturnValue")
	public boolean isPaused() { return false; }

	@SuppressWarnings("SameReturnValue")
	public int getCloseKey() { return GLFW.GLFW_KEY_ESCAPE; }

	protected Screen makeScreen(ITextComponent title, boolean hasContainer) { return hasContainer ? new ScreenAdaptedWithContainer(title) : new ScreenAdapted(title); }

	public void schedule(Runnable task) { tasks.add(task); }

	@Override
	protected Shape adaptToBounds(IGuiReshapeHandler handler, GuiComponent<?> invoker, Rectangle2D rectangle) { return rectangle; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		GLStacks.resetAll();
		tasks.forEach(Runnable::run);
		tasks.clear();
		if (EnumGuiState.READY.isReachedBy(data.getState())) {
			constrain(stack);
			super.render(stack, mouse, partialTicks);
		}
	}

	@Override
	public void constrain(AffineTransformStack stack) {
		data.constraints.clear();
		super.constrain(stack);
	}

	@SuppressWarnings("SameReturnValue")
	public int getFocusChangeKey() { return GLFW.GLFW_KEY_TAB; }

	protected Rectangle2D getRectangle() { return (Rectangle2D) super.getShape(); }

	@Override
	public Rectangle2D getRectangleView() { return (Rectangle2D) getRectangle().clone(); }

	@Override
	public void reshape(GuiComponent<?> invoker) { reshape(invoker, getRectangle()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void reshape(GuiComponent<?> invoker, Shape shape) { setBounds(this, invoker, shape.getBounds2D()); }

	@Override
	public void initialize(GuiComponent<?> invoker) { onInitialize(this, invoker); }

	@Override
	public void tick(GuiComponent<?> invoker) { onTick(this, invoker); }

	@Override
	public void close(GuiComponent<?> invoker) { onClose(this, invoker); }

	@Override
	public void destroy(GuiComponent<?> invoker) { onDestroyed(this, invoker); }

	@Override
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		super.onInitialize(handler, invoker);
		reshape(this, new Rectangle2D.Double(0, 0, getScreen().width, getScreen().height));
	}

	@Override
	public void onReshape(IGuiReshapeHandler handler, GuiComponent<?> invoker, Shape shapePrevious) {
		data.anchors.clear();
		super.onReshape(handler, invoker, shapePrevious);
	}

	@Override
	public void onClose(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		super.onClose(handler, invoker);

		// COMMENT synthetic events
		AffineTransformStack stack = new AffineTransformStack();
		Point2D cursor = GLUtilities.getCursorPos();
		data.getDragsView().forEach((i, d) -> onMouseDragged(stack, d, (Point2D) cursor.clone(), i));
		onMouseHovered(new AffineTransformStack(), (Point2D) cursor.clone());
		new ArrayDeque<>(data.getKeyPresses()).forEach(k -> onKeyReleased(k.key, k.scanCode, k.modifiers));

		getScreen().getMinecraft().displayGuiScreen(null);
	}

	@Override
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		EnumGuiMouseClickResult ret = super.onMouseClicked(stack, drag, mouse, button);
		if (ret.result) fakeParent.drags.put(button, drag);
		return ret;
	}

	@Override
	public boolean onMouseReleased(AffineTransformStack stack, Point2D mouse, int button) {
		data.getDragsView().forEach((i, d) -> onMouseDragged(stack, d, mouse, i));
		return super.onMouseReleased(stack, mouse, button);
	}

	@Override
	@Deprecated
	public final void onAdded(GuiContainer<?> parentCurrent, int index) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

	@Override
	@Deprecated
	public final void onRemoved(GuiContainer<?> parentPrevious) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

	@Override
	public boolean onKeyPressed(int key, int scanCode, int modifiers) {
		if (key == getCloseKey()) {
			close(this);
			return true;
		}
		if (key == getFocusChangeKey() && onChangeFocus((modifiers & GLFW.GLFW_MOD_SHIFT) == 0))
			return true;
		return super.onKeyPressed(key, scanCode, modifiers);
	}

	@Override
	public Optional<GuiDragInfo> getDragInfo(int button) { return Optional.ofNullable(fakeParent.drags.get(button)); }

	@Override
	protected boolean isBeingDragged() { return data.getDragsView().isEmpty() && !fakeParent.drags.isEmpty(); }

	@Override
	protected boolean isBeingHovered() { return true; }

	public <T extends Screen & IHasContainer<C>> T getContainerScreen() {
		if (data.container == null) throw BecauseOf.unsupportedOperation();
		return castUncheckedUnboxedNonnull(getScreen());
	}

	@OnlyIn(CLIENT)
	protected static class FakeParent {
		protected Map<Integer, GuiDragInfo> drags = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();
	}

	@OnlyIn(CLIENT)
	public static class Data<E extends Events, C extends Container> extends GuiContainer.Data<E> {
		@Nullable
		public final C container;
		@Nullable
		protected GuiBackground<?, ?> background;

		public Data(E events, Supplier<Logger> logger, @Nullable GuiBackground<?, ?> background, @Nullable C container) {
			super(events, logger);
			this.container = container;
			this.background = background;
		}

		public Optional<GuiBackground<?, ?>> getBackground() { return Optional.ofNullable(background); }

		public void setBackground(@Nullable GuiBackground<?, ?> background) { events.fire(events.dBackgroundSet, new Events.DBackgroundSetParameter(this.background, this.background = background)); }
	}

	////////// Screen Compatibility //////////

	public Screen getScreen() { return screen; }

	@OnlyIn(CLIENT)
	public static class Events extends GuiComponent.Events {
		public final List<Consumer<DBackgroundSetParameter>> dBackgroundSet = new ArrayList<>(INITIAL_CAPACITY_1);

		@OnlyIn(CLIENT)
		public static final class DBackgroundSetParameter {
			@Nullable
			public final GuiBackground<?, ?> previous, current;

			public DBackgroundSetParameter(@Nullable GuiBackground<?, ?> previous, @Nullable GuiBackground<?, ?> current) {
				this.previous = previous;
				this.current = current;
			}
		}
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
		public void blit(int x, int y, int u, int v, int w, int h) { DrawingUtilities.blit(AffineTransforms.getIdentity(), new Rectangle2D.Double(x, y, w, h), new Point2D.Double(u, v), new Dimension2DDouble(256, 256), getBlitOffset()); }

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
	protected class ScreenAdaptedWithContainer extends ScreenAdapted implements IHasContainer<Container> {
		protected ScreenAdaptedWithContainer(ITextComponent title) { super(title); }

		@Override
		public Container getContainer() {
			assert data.container != null;
			return data.container;
		}
	}
}
