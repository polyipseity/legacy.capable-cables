package $group__.ui.cursors;

import $group__.ui.UIConfiguration;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.client.minecraft.ImageUtilities;
import net.minecraft.client.renderer.texture.NativeImage;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public enum EnumCursor {
	STANDARD_ARROW_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)),
	STANDARD_I_BEAM_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)),
	STANDARD_CROSS_HAIR_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR)),
	STANDARD_HAND_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR)),
	STANDARD_RESIZE_HORIZONTAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR)),
	STANDARD_RESIZE_VERTICAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR)),
	@SuppressWarnings({"SpellCheckingInspection"})
	EXTENSION_RESIZE_NW_SE_CURSOR(() -> {
		Logger logger = UIConfiguration.INSTANCE.getLogger();
		InputStream input = AssertionUtilities.assertNonnull(EnumCursor.class.getResourceAsStream("aero_nwse/32x32.png"));
		try {
			return Try.call(() -> createCursor(
					input,
					new Point(8, 8)), UIConfiguration.INSTANCE.getLogger()).orElseThrow(ThrowableCatcher::rethrow);
		} finally {
			Try.run(input::close, logger);
		}
	}),
	@SuppressWarnings({"SpellCheckingInspection"})
	EXTENSION_RESIZE_NE_SW_CURSOR(() -> {
		Logger logger = UIConfiguration.INSTANCE.getLogger();
		InputStream input = AssertionUtilities.assertNonnull(EnumCursor.class.getResourceAsStream("aero_nesw/32x32.png"));
		try {
			return Try.call(() -> createCursor(
					input,
					new Point(8, 8)), UIConfiguration.INSTANCE.getLogger()).orElseThrow(ThrowableCatcher::rethrow);
		} finally {
			Try.run(input::close, logger);
		}
	}),
	;

	protected final long handle;

	EnumCursor(Supplier<Long> handle) { this.handle = handle.get(); }

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	public static long createCursor(InputStream input, Point2D hotspot) throws IOException {
		try (NativeImage ni = NativeImage.read(NativeImage.PixelFormat.RGBA, input);
		     GLFWImage i = ImageUtilities.toGLFWImageCursor(ni)) {
			Point hotspotF = UIObjectUtilities.getPointFloor(hotspot);
			return GLFW.glfwCreateCursor(i, hotspotF.x, hotspotF.y);
		}
	}

	public long getHandle() { return handle; }
}
