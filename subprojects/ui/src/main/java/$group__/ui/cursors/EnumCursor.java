package $group__.ui.cursors;

import $group__.ui.UIConfiguration;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.minecraft.client.ImageUtilities;
import net.minecraft.client.renderer.texture.NativeImage;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.LongSupplier;

public enum EnumCursor
		implements ICursor {
	STANDARD_ARROW_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)),
	STANDARD_I_BEAM_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)),
	STANDARD_CROSS_HAIR_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR)),
	STANDARD_HAND_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR)),
	STANDARD_RESIZE_HORIZONTAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR)),
	STANDARD_RESIZE_VERTICAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR)),
	EXTENSION_RESIZE_NW_SE_CURSOR(() -> {
		InputStream input = AssertionUtilities.assertNonnull(EnumCursor.class.getResourceAsStream(StaticHolder.getExtensionResizeNwSeCursorPath()));
		try {
			return Try.call(() -> createCursor(
					input,
					new Point(8, 8)), UIConfiguration.getInstance().getLogger()).orElseThrow(ThrowableCatcher::rethrow);
		} finally {
			Try.run(input::close, UIConfiguration.getInstance().getLogger());
		}
	}),
	EXTENSION_RESIZE_NE_SW_CURSOR(() -> {
		InputStream input = AssertionUtilities.assertNonnull(EnumCursor.class.getResourceAsStream(StaticHolder.getExtensionResizeNeSwCursorPath()));
		try {
			return Try.call(() -> createCursor(
					input,
					new Point(8, 8)), UIConfiguration.getInstance().getLogger()).orElseThrow(ThrowableCatcher::rethrow);
		} finally {
			Try.run(input::close, UIConfiguration.getInstance().getLogger());
		}
	}),
	;

	protected final long handle;

	EnumCursor(LongSupplier handle) { this.handle = handle.getAsLong(); }

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	public static long createCursor(InputStream input, Point2D hotspot) throws IOException {
		try (NativeImage ni = NativeImage.read(NativeImage.PixelFormat.RGBA, input);
		     GLFWImage i = ImageUtilities.toGLFWImageCursor(ni)) {
			Point hotspotF = UIObjectUtilities.getPointFloor(hotspot);
			return GLFW.glfwCreateCursor(i, hotspotF.x, hotspotF.y);
		}
	}

	@Override
	public long getHandle() { return handle; }

	@Override
	public void close() { GLFW.glfwDestroyCursor(getHandle()); }

	public enum StaticHolder {
		;

		@SuppressWarnings("HardcodedFileSeparator")
		@NonNls
		private static final String EXTENSION_RESIZE_NW_SE_CURSOR_PATH = "aero_nwse/32x32.png";
		@SuppressWarnings("HardcodedFileSeparator")
		@NonNls
		private static final String EXTENSION_RESIZE_NE_SW_CURSOR_PATH = "aero_nesw/32x32.png";

		public static String getExtensionResizeNwSeCursorPath() { return EXTENSION_RESIZE_NW_SE_CURSOR_PATH; }

		public static String getExtensionResizeNeSwCursorPath() { return EXTENSION_RESIZE_NE_SW_CURSOR_PATH; }
	}
}
