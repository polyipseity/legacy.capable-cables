package $group__.client.gui.components;

import $group__.client.gui.utilities.Backgrounds;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.client.gui.utilities.TextComponents;
import $group__.client.gui.utilities.Tooltips;
import $group__.utilities.helpers.Adapters;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiRoot extends GuiContainer {
	public static final int TO_SCREEN_ADAPTER_DEFAULT = Adapters.register(GuiRoot.class, Screen.class, GuiRoot::asScreen);

	protected final ScreenAdapted screen;

	public GuiRoot(ITextComponent title) { screen = new ScreenAdapted(title); }

	protected GuiRoot(ScreenAdapted screen) { this.screen = screen; }

	@Override
	@Deprecated
	protected void onAdded(GuiContainer parent) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

	@Override
	protected void onInitialize() {
		super.onInitialize();
		onResize();
	}

	@Override
	protected void onResize() {
		rectangle.setRect(0, 0, screen.width, screen.height);
		super.onResize();
	}

	@Override
	@Deprecated
	protected void onRemoved(GuiContainer parent) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

	@Override
	protected void onClose() {
		super.onClose();
		screen.getMinecraft().displayGuiScreen(null);
	}

	public boolean isPaused() { return false; }

	////////// Screen Compatibility //////////

	protected ScreenAdapted asScreen() { return screen; }

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
		protected void init() { GuiRoot.this.onInitialize(); }

		@Override
		public void resize(Minecraft client, int width, int height) {
			super.resize(client, width, height);
			//GuiRoot.this.onResize(); // resize calls init
		}

		@Override
		@Deprecated
		public void setSize(int width, int height) {
			super.setSize(width, height);
			rectangle.setRect(rectangle.getX(), rectangle.getY(), width, height);
		}

		@Override
		public void tick() { GuiRoot.this.onTick(); }

		@Override
		public void onClose() { GuiRoot.this.onClose(); }

		@Override
		public void removed() { GuiRoot.this.onDestroy(); }

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
		public boolean shouldCloseOnEsc() { return false; }

		@Override
		@Deprecated
		protected void renderTooltip(ItemStack item, int mouseX, int mouseY) { Tooltips.renderTooltip(getMinecraft(), item, mouseX, mouseY, width, height, font); }

		@Override
		@Deprecated
		public List<String> getTooltipFromItem(ItemStack item) { return Tooltips.getTooltipFromItem(getMinecraft(), item); }

		@Override
		@Deprecated
		public void renderTooltip(String tooltip, int mouseX, int mouseY) { Tooltips.renderTooltip(tooltip, mouseX, mouseY, width, height, font); }

		@Override
		@Deprecated
		public void renderTooltip(List<String> tooltip, int mouseX, int mouseY) { Tooltips.renderTooltip(tooltip, mouseX, mouseY, width, height, font); }

		@Override
		@Deprecated
		public void renderTooltip(List<String> tooltip, int mouseX, int mouseY, FontRenderer font) { Tooltips.renderTooltip(tooltip, mouseX, mouseY, width, height, font); }

		@Override
		@Deprecated
		protected void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { TextComponents.renderComponentHoverEffect(getMinecraft(), component, mouseX, mouseY, width, height, font); }

		@Override
		@Deprecated
		public boolean handleComponentClicked(ITextComponent component) { return TextComponents.handleComponentClicked(this, this::insertText, this::sendMessage, component); }

		@Override
		@Deprecated
		protected void hLine(int x1, int x2, int y, int color) { GuiUtilities.hLine(x1, x2, y, color); }

		@Override
		@Deprecated
		protected void vLine(int x, int y1, int y2, int color) { GuiUtilities.vLine(x, y1, y2, color); }

		@Override
		@Deprecated
		protected void fillGradient(int x1, int y1, int x2, int y2, int colorY1, int colorY2) { GuiUtilities.fillGradient(x1, y1, x2, y2, blitOffset, colorY1, colorY2); }

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
		public void blit(int x, int y, int u, int v, int xLength, int yLength) { GuiUtilities.blit(x, y, blitOffset, u, v, xLength, yLength, 256, 256); }

		@Override
		@Deprecated
		public void renderBackground() { Backgrounds.renderBackground(this, blitOffset); }

		@Override
		@Deprecated
		public void renderBackground(int vOffset) { Backgrounds.renderBackground(this, blitOffset, vOffset); }

		@Override
		@Deprecated
		public void renderDirtBackground(int vOffset) { Backgrounds.renderDirtBackground(this, vOffset); }
	}
}
