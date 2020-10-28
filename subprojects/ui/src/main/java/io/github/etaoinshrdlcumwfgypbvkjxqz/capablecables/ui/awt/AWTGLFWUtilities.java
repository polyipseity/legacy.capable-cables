package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.awt;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.stream.IntStream;

@Deprecated
public enum AWTGLFWUtilities {
	;
	private static final MethodHandle MOUSE_EVENT_BUTTON_SETTER;
	private static final MethodHandle MOUSE_EVENT_SHOULD_EXCLUDE_BUTTON_FROM_EXT_MODIFIERS_SETTER;

	static {
		try {
			InvokeUtilities.getImplLookup().findStaticSetter(MouseEvent.class, "cachedNumberOfButtons", int.class).invokeExact(toAWTMouseButton(GLFW.GLFW_MOUSE_BUTTON_LAST));
			MOUSE_EVENT_BUTTON_SETTER = InvokeUtilities.getImplLookup().findSetter(MouseEvent.class, "button", int.class);
			MOUSE_EVENT_SHOULD_EXCLUDE_BUTTON_FROM_EXT_MODIFIERS_SETTER = InvokeUtilities.getImplLookup().findSetter(MouseEvent.class
					, "shouldExcludeButtonFromExtModifiers", boolean.class);
		} catch (Throwable e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	private static int toGLFWMouseButton(int button) {
		return button - 1;
	}

	@SuppressWarnings("MagicConstant")
	public static KeyEvent createKeyEventFromGLFW(Component component, int id, int keyCode) {
		return new KeyEvent(
				component,
				id,
				System.currentTimeMillis(),
				getAWTModifiers(),
				toAWTKeyCode(id, keyCode),
				toAWTKeyChar(keyCode),
				toAWTKeyLocation(id, keyCode)
		);
	}

	public static int getAWTModifiers() {
		int result = 0;
		if (ImmutableSet.of(GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT).stream().unordered()
				.mapToInt(key -> GLFW.glfwGetKey(MinecraftOpenGLUtilities.getWindowHandle(), key))
				.filter(status -> status == GLFW.GLFW_PRESS)
				.findAny()
				.isPresent())
			result |= InputEvent.SHIFT_DOWN_MASK;
		if (ImmutableSet.of(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL).stream().unordered()
				.mapToInt(key -> GLFW.glfwGetKey(MinecraftOpenGLUtilities.getWindowHandle(), key))
				.filter(status -> status == GLFW.GLFW_PRESS)
				.findAny()
				.isPresent())
			result |= InputEvent.CTRL_DOWN_MASK;
		if (ImmutableSet.of(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT).stream().unordered()
				.mapToInt(key -> GLFW.glfwGetKey(MinecraftOpenGLUtilities.getWindowHandle(), key))
				.filter(status -> status == GLFW.GLFW_PRESS)
				.findAny()
				.isPresent())
			result |= InputEvent.ALT_DOWN_MASK;
		if (ImmutableSet.of(GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER).stream().unordered()
				.mapToInt(key -> GLFW.glfwGetKey(MinecraftOpenGLUtilities.getWindowHandle(), key))
				.filter(status -> status == GLFW.GLFW_PRESS)
				.findAny()
				.isPresent())
			result |= InputEvent.META_DOWN_MASK; // COMMENT not sure if super == meta
		return IntStream.rangeClosed(GLFW.GLFW_MOUSE_BUTTON_1, GLFW.GLFW_MOUSE_BUTTON_LAST)
				.filter(button -> GLFW.glfwGetMouseButton(MinecraftOpenGLUtilities.getWindowHandle(), button) == GLFW.GLFW_PRESS)
				.map(AWTGLFWUtilities::toAWTMouseButton)
				.map(InputEvent::getMaskForButton)
				.reduce(result, (a, b) -> a | b);
	}

	public static int toAWTKeyCode(int id, int key) {
		if (id == KeyEvent.KEY_TYPED)
			return KeyEvent.VK_UNDEFINED;
		// COMMENT Special cases?
		return KeyEvent.getExtendedKeyCodeForChar(toAWTKeyChar(key));
	}

	public static char toAWTKeyChar(int key) {
		// COMMENT Special cases?
		return (char) key;
	}

	public static int toAWTKeyLocation(int id, int key) {
		if (id == KeyEvent.KEY_TYPED)
			return KeyEvent.KEY_LOCATION_UNKNOWN;
		else if (ImmutableSet.of(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_LEFT_SUPER).contains(key))
			return KeyEvent.KEY_LOCATION_LEFT;
		else if (ImmutableSet.of(GLFW.GLFW_KEY_RIGHT_ALT, GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_RIGHT_SHIFT, GLFW.GLFW_KEY_RIGHT_SUPER).contains(key))
			return KeyEvent.KEY_LOCATION_RIGHT;
		// COMMENT Number pad?
		return KeyEvent.KEY_LOCATION_STANDARD;
	}

	public static int toAWTMouseButton(int button) {
		return button + 1;
	}

	public static Component findComponentAt(Container component, int mouseX, int mouseY) {
		return Optional.ofNullable(component.findComponentAt(mouseX, mouseY))
				.orElse(component);
	}

	public static MouseEvent createMouseEventFromGLFW(Component component, int id, int mouseX, int mouseY, int button) {
		Point relativeMouse = new Point(mouseX, mouseY);
		SwingUtilities.convertPointFromScreen(relativeMouse, component);
		int actualMouseButton = toAWTMouseButton(button);
		@SuppressWarnings("MagicConstant") MouseEvent result = new MouseEvent(
				component,
				id,
				System.currentTimeMillis(),
				getAWTModifiers(),
				(int) relativeMouse.getX(),
				(int) relativeMouse.getY(),
				mouseX,
				mouseY,
				1,
				false,
				actualMouseButton > MouseEvent.BUTTON3 ? MouseEvent.NOBUTTON : actualMouseButton
		);
		if (result.getButton() != actualMouseButton) {
			try {
				if (result.getModifiersEx() != 0 && (id == MouseEvent.MOUSE_RELEASED || id == MouseEvent.MOUSE_CLICKED))
					getMouseEventShouldExcludeButtonFromExtModifiersSetter().invokeExact(result, true);
				getMouseEventButtonSetter().invokeExact(result, actualMouseButton);
			} catch (Throwable throwable) {
				throw ThrowableUtilities.propagate(throwable);
			}
		}
		return result;
	}

	private static MethodHandle getMouseEventShouldExcludeButtonFromExtModifiersSetter() {
		return MOUSE_EVENT_SHOULD_EXCLUDE_BUTTON_FROM_EXT_MODIFIERS_SETTER;
	}

	private static MethodHandle getMouseEventButtonSetter() {
		return MOUSE_EVENT_BUTTON_SETTER;
	}

	@SuppressWarnings("MagicConstant")
	public static MouseWheelEvent createMouseWheelEventFromGLFW(Component component, int mouseX, int mouseY, int delta) {
		component = CastUtilities.castChecked(Container.class, component)
				.map(container -> container.findComponentAt(mouseX, mouseY))
				.orElse(component);
		Point relativeMouse = new Point(mouseX, mouseY);
		SwingUtilities.convertPointFromScreen(relativeMouse, component);
		return new MouseWheelEvent(
				component,
				MouseEvent.MOUSE_WHEEL,
				System.currentTimeMillis(),
				getAWTModifiers(),
				(int) relativeMouse.getX(),
				(int) relativeMouse.getY(),
				mouseX,
				mouseY,
				1,
				false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL,
				delta,
				1
		);
	}
}
