package $group__.client.gui.components.roots;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.traits.IGuiLifecycleHandler;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.client.gui.utilities.Backgrounds;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.client.gui.utilities.TextComponents;
import $group__.client.gui.utilities.Tooltips;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

@OnlyIn(CLIENT)
public abstract class GuiRoot<C extends Container> extends GuiContainer implements IGuiLifecycleHandler, IGuiReRectangleHandler {
	@Nullable
	protected final C container;
	protected final Lazy<Screen> screen;

	protected GuiRoot(ITextComponent title) { this(title, null); }

	protected GuiRoot(ITextComponent title, @Nullable C container) {
		super(new Rectangle2D.Double());
		this.container = container;
		screen = Lazy.of(() -> container == null ? new ScreenAdapted(title) : new ScreenAdaptedWithContainer(title));
	}

	public boolean isPaused() { return false; }

	public int getCloseKey() { return GLFW_KEY_ESCAPE; }

	@Override
	public void reRectangle(GuiComponent invoker) { reRectangle(invoker, getRectangle()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void reRectangle(GuiComponent invoker, Rectangle2D rectangle) { setRectangle(this, invoker, rectangle); }

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
		reRectangle(this, new Rectangle(0, 0, getScreen().width, getScreen().height));
	}

	@Override
	public void onClose(IGuiLifecycleHandler handler, GuiComponent invoker) {
		super.onClose(handler, invoker);
		getScreen().getMinecraft().displayGuiScreen(null);
	}

	@Override
	@Deprecated
	public final void onAdded(GuiContainer parent) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

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

	////////// Screen Compatibility //////////

	public Screen getScreen() { return screen.get(); }

	@SuppressWarnings("unchecked")
	public <T extends Screen & IHasContainer<C>> T getContainerScreen() {
		if (container == null) throw BecauseOf.unsupportedOperation();
		return (T) screen.get();
	}

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
			GuiRoot.this.reRectangle(GuiRoot.this, new Rectangle(0, 0, width, height));
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
		protected void hLine(int x1, int x2, int y, int color) { GuiUtilities.hLine(x1, x2, y, color); }

		@Override
		@Deprecated
		protected void vLine(int x, int y1, int y2, int color) { GuiUtilities.vLine(x, y1, y2, color); }

		@Override
		@Deprecated
		protected void fillGradient(int x1, int y1, int x2, int y2, int colorY1, int colorY2) { GuiUtilities.fillGradient(x1, y1, x2, y2, getBlitOffset(), colorY1, colorY2); }

		@Override
		@Deprecated
		public void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { GuiUtilities.drawCenteredString(font, string, x, y, color); }

		@Override
		@Deprecated
		public void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { GuiUtilities.drawRightAlignedString(font, string, x, y, color); }

		@Override
		@Deprecated
		public void drawString(FontRenderer font, String string, int x, int y, int color) { GuiUtilities.drawString(font, string, x, y, color); }

		@SuppressWarnings("MagicNumber")
		@Override
		@Deprecated
		public void blit(int x, int y, int u, int v, int xLength, int yLength) { GuiUtilities.blit(x, y, getBlitOffset(), u, v, xLength, yLength, 256, 256); }

		@Override
		@Deprecated
		public void renderBackground() { Backgrounds.renderBackground(getMinecraft(), width, height); }

		@Override
		@Deprecated
		public void renderBackground(int blitOffset) { Backgrounds.renderBackground(getMinecraft(), width, height, blitOffset); }

		@Override
		@Deprecated
		public void renderDirtBackground(int blitOffset) { Backgrounds.renderDirtBackground(getMinecraft(), width, height, blitOffset); }
	}

	protected class ScreenAdaptedWithContainer extends ScreenAdapted implements IHasContainer<C> {
		protected ScreenAdaptedWithContainer(ITextComponent title) { super(title); }

		@Override
		public C getContainer() {
			assert container != null;
			return container;
		}
	}
}
