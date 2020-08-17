package $group__.client.ui.structures;

import $group__.client.ui.ConfigurationUI;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.client.minecraft.specific.ImageUtilities;
import $group__.utilities.specific.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.specific.ThrowableUtilities.Try;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.function.Supplier;

public enum EnumCursor {
	STANDARD_ARROW_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)),
	STANDARD_I_BEAM_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)),
	STANDARD_CROSS_HAIR_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR)),
	STANDARD_HAND_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR)),
	STANDARD_RESIZE_HORIZONTAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR)),
	STANDARD_RESIZE_VERTICAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR)),
	@SuppressWarnings({"SpellCheckingInspection", "HardcodedFileSeparator"})
	EXTENSION_RESIZE_NW_SE_CURSOR(() -> {
		return Try.call(() -> createCursor(
				new ResourceLocation(ConfigurationUI.getModId(), "textures/cursors/aero_nwse/32x32.png"),
				new Point(8, 8)), LoggerHolder.LOGGER).orElseThrow(ThrowableCatcher::rethrow);
	}),
	@SuppressWarnings({"SpellCheckingInspection", "HardcodedFileSeparator"})
	EXTENSION_RESIZE_NE_SW_CURSOR(() -> {
		return Try.call(() -> createCursor(
				new ResourceLocation(ConfigurationUI.getModId(), "textures/cursors/aero_nesw/32x32.png"),
				new Point(8, 8)), LoggerHolder.LOGGER).orElseThrow(ThrowableCatcher::rethrow);
	}),
	;

	protected final long handle;

	EnumCursor(Supplier<Long> handle) { this.handle = handle.get(); }

	@SuppressWarnings("EmptyMethod")
	public static void preload() {}

	public static long createCursor(ResourceLocation location, Point2D hotspot) throws IOException {
		try (GLFWImage i = ImageUtilities.toGLFWImageCursor(NativeImage.read(NativeImage.PixelFormat.RGBA,
				ResourceUtilities.getResource(location).getInputStream()))) {
			Point hotspotF = UIObjectUtilities.getPointFloor(hotspot);
			return GLFW.glfwCreateCursor(i, hotspotF.x, hotspotF.y);
		}
	}

	public long getHandle() { return handle; }

	protected enum LoggerHolder {
		;

		private static final Logger LOGGER = LogManager.getLogger();
	}
}
