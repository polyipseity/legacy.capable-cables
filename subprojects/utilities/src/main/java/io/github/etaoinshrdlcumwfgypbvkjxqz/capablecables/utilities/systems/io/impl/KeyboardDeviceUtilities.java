package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IKeyboardDevice;
import org.lwjgl.glfw.GLFW;

public enum KeyboardDeviceUtilities {
	;

	public static boolean isShiftModifierActive(IKeyboardDevice instance) {
		return instance.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || instance.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
	}
}
