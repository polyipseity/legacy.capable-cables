package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.awt;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftGraphics;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftSurfaceData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.UIMinecraftBackgrounds;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.peer.ComponentPeer;
import java.util.Arrays;

@Deprecated
public class AWTMinecraftScreenAdapter
		extends Screen {
	private final JComponent component;

	protected AWTMinecraftScreenAdapter(ITextComponent titleIn, JComponent component) {
		super(titleIn);
		this.component = component;
		try {
			AWTMinecraftHeadlessHacks.getComponentParentSetter().invokeExact((Component) component, (Container) UninitializedWindow.getInstance());
			TreeUtilities.<Component, Void>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
					IThrowingFunction.executeNow(component1 -> {
						AWTMinecraftHeadlessHacks.getComponentPeerSetter().invokeExact((Component) component1, (ComponentPeer) AWTMinecraftHeadlessHacks.getLightweightPeer());
						return null;
					}),
					component1 -> CastUtilities.castChecked(Container.class, component1)
							.map(Container::getComponents)
							.map(Arrays::asList)
							.orElseGet(ImmutableList::of),
					null, null);
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		UIMinecraftBackgrounds.notifyBackgroundDrawn(this);
		Graphics graphics = MinecraftGraphics.getGraphics(); // COMMENT make sure that initialization takes place in the rendering thread
		MinecraftSurfaceData.getInstance().clear();
		TreeUtilities.<Component, Void>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, getComponent(),
				component -> {
					component.doLayout();
					return null;
				},
				component -> CastUtilities.castChecked(Container.class, component)
						.map(Container::getComponents)
						.map(Arrays::asList)
						.orElseGet(ImmutableList::of),
				null, null);
		getComponent().paint(graphics);
		MinecraftSurfaceData.getInstance().draw();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		getComponent().dispatchEvent(AWTGLFWUtilities.createKeyEventFromGLFW(getComponent(), KeyEvent.KEY_PRESSED, keyCode));
		return true;
	}

	protected JComponent getComponent() {
		return component;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		Component component = AWTGLFWUtilities.findComponentAt(getComponent(), (int) mouseX, (int) mouseY);
		MouseEvent event = AWTGLFWUtilities.createMouseEventFromGLFW(component, MouseEvent.MOUSE_PRESSED, (int) mouseX, (int) mouseY, button);
		component.dispatchEvent(event);
		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		Component component = AWTGLFWUtilities.findComponentAt(getComponent(), (int) mouseX, (int) mouseY);
		MouseEvent event = AWTGLFWUtilities.createMouseEventFromGLFW(component, MouseEvent.MOUSE_RELEASED, (int) mouseX, (int) mouseY, button);
		component.dispatchEvent(event);
		return true;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) {
		Component component = AWTGLFWUtilities.findComponentAt(getComponent(), (int) mouseX, (int) mouseY);
		MouseEvent event = AWTGLFWUtilities.createMouseEventFromGLFW(component, MouseEvent.MOUSE_DRAGGED, (int) mouseX, (int) mouseY, button);
		component.dispatchEvent(event);
		return true;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		Component component = AWTGLFWUtilities.findComponentAt(getComponent(), (int) mouseX, (int) mouseY);
		MouseEvent event = AWTGLFWUtilities.createMouseWheelEventFromGLFW(component, (int) mouseX, (int) mouseY, (int) delta);
		component.dispatchEvent(event);
		return true;
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		getComponent().dispatchEvent(AWTGLFWUtilities.createKeyEventFromGLFW(getComponent(), KeyEvent.KEY_RELEASED, keyCode));
		return true;
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		@SuppressWarnings("UnnecessaryLocalVariable") int keyCode = codePoint;
		getComponent().dispatchEvent(AWTGLFWUtilities.createKeyEventFromGLFW(getComponent(), KeyEvent.KEY_TYPED, keyCode));
		return true;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		Component component = AWTGLFWUtilities.findComponentAt(getComponent(), (int) mouseX, (int) mouseY);
		MouseEvent event = AWTGLFWUtilities.createMouseEventFromGLFW(component, MouseEvent.MOUSE_MOVED, (int) mouseX, (int) mouseY, AWTGLFWUtilities.toAWTMouseButton(MouseEvent.NOBUTTON));
		component.dispatchEvent(event);
	}

	/* COMMENT
	@Override
	public boolean shouldCloseOnEsc() {
		return super.shouldCloseOnEsc();
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
		super.init(p_init_1_, p_init_2_, p_init_3_);
	}

	@Override
	public void setSize(int p_setSize_1_, int p_setSize_2_) {
		super.setSize(p_setSize_1_, p_setSize_2_);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void removed() {
		super.removed();
	}

	@Override
	public boolean isPauseScreen() {
		return super.isPauseScreen();
	}

	@Override
	public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
		super.resize(p_resize_1_, p_resize_2_, p_resize_3_);
	}
	 */
}
