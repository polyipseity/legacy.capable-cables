package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.inputs;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftInputUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.ICursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IPointerDevice;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class MinecraftPointerDevice
		implements IPointerDevice {
	@Override
	public Point2D getPositionView() { return MinecraftInputUtilities.getScaledCursorPosition(); }

	@Override
	public void setCursor(ICursor cursor) {
		GLFW.glfwSetCursor(MinecraftOpenGLUtilities.getWindowHandle(), cursor.getHandle());
	}
}
