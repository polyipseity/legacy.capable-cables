package $group__.client.gui;

import $group__.client.gui.utilities.Backgrounds;
import $group__.client.gui.utilities.GUIs;
import $group__.client.gui.utilities.TextComponents;
import $group__.client.gui.utilities.Tooltips;
import $group__.utilities.helpers.Adapters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static $group__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;

@OnlyIn(Dist.CLIENT)
public class GuiRoot extends GuiContainer {
	public static final int TO_SCREEN_ADAPTER_DEFAULT = Adapters.register(GuiRoot.class, Screen.class, GuiRoot::asScreen);

	protected final Screen screen;
	protected boolean paused;

	public GuiRoot(ITextComponent title) {
		screen = new Screen(title) {
			@Override
			public void render(int mouseX, int mouseY, float partialTicks) { GuiRoot.this.render(mouseX, mouseY, partialTicks); }

			@Override
			public boolean keyPressed(int key, int scanCode, int modifiers) { return GuiRoot.this.keyPressed(key, scanCode, modifiers); }

			@Override
			@Deprecated
			public boolean shouldCloseOnEsc() { return false; }

			@Override
			public void onClose() {
				GuiRoot.this.beforeClose();
				super.onClose();
			}

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
			public List<? extends IGuiEventListener> children() { return GuiRoot.this.children; }

			@Override
			protected void init() { GuiRoot.this.onInitialize(); }

			@Override
			public void tick() { GuiRoot.this.onTick(); }

			@Override
			public void removed() { GuiRoot.this.afterClose(); }

			@Override
			@Deprecated
			public void renderBackground() { Backgrounds.renderBackground(this, blitOffset); }

			@Override
			@Deprecated
			public void renderBackground(int vOffset) { Backgrounds.renderBackground(this, blitOffset, vOffset); }

			@Override
			@Deprecated
			public void renderDirtBackground(int vOffset) { Backgrounds.renderDirtBackground(this, vOffset); }

			@Override
			public boolean isPauseScreen() { return isPaused(); }

			@Override
			public void resize(Minecraft game, int width, int height) {
				super.resize(game, width, height);
				GuiRoot.this.onResize();
			}

			@Override
			public boolean isMouseOver(double mouseX, double mouseY) { return GuiRoot.this.isMouseOver(mouseX, mouseY); }

			@Nullable
			@Override
			public IGuiEventListener getFocused() { return GuiRoot.this.getFocused(); }

			@Override
			public void setFocused(@Nullable IGuiEventListener focused) { GuiRoot.this.setFocused(focused); }

			@Override
			@Deprecated
			protected void hLine(int x1, int x2, int y, int color) { GUIs.hLine(x1, x2, y, color); }

			@Override
			@Deprecated
			protected void vLine(int x, int y1, int y2, int color) { GUIs.vLine(x, y1, y2, color); }

			@Override
			@Deprecated
			protected void fillGradient(int x1, int y1, int x2, int y2, int colorY1, int colorY2) { GUIs.fillGradient(x1, y1, x2, y2, blitOffset, colorY1, colorY2); }

			@Override
			@Deprecated
			public void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { GUIs.drawCenteredString(font, string, x, y, color); }

			@Override
			@Deprecated
			public void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { GUIs.drawRightAlignedString(font, string, x, y, color); }

			@Override
			@Deprecated
			public void drawString(FontRenderer font, String string, int x, int y, int color) { GUIs.drawString(font, string, x, y, color); }

			@Override
			@Deprecated
			public void blit(int x, int y, int u, int v, int xLength, int yLength) { GUIs.blit(x, y, blitOffset, u, v, xLength, yLength, 256, 256); }

			@Override
			public Optional<IGuiEventListener> func_212930_a(double p_212930_1_, double p_212930_3_) { throw rejectUnsupportedOperation(); }

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
			public boolean changeFocus(boolean forward) { return GuiRoot.this.changeFocus(forward); }

			@Override
			public void mouseMoved(double mouseX, double mouseY) { GuiRoot.this.mouseMoved(mouseX, mouseY); }
		};
	}

	public boolean isPaused() { return paused; }

	protected void onInitialize() {

	}

	protected void onResize() {

	}

	protected void onTick() {

	}

	protected void beforeClose() {
	}

	protected void afterClose() {

	}

	public Screen asScreen() { return screen; }
}
