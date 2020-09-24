package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftImageUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import net.minecraft.client.renderer.texture.NativeImage;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.LongSupplier;

public enum EnumGLFWCursor
		implements ICursor {
	STANDARD_ARROW_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)),
	STANDARD_I_BEAM_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)),
	STANDARD_CROSS_HAIR_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR)),
	STANDARD_HAND_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR)),
	STANDARD_RESIZE_HORIZONTAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR)),
	STANDARD_RESIZE_VERTICAL_CURSOR(() -> GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR)),
	EXTENSION_RESIZE_NW_SE_CURSOR(() -> {
		InputStream input = AssertionUtilities.assertNonnull(EnumGLFWCursor.class.getResourceAsStream(StaticHolder.getExtensionResizeNwSeCursorPath()));
		try {
			try {
				return createCursor(input, new Point(8, 8));
			} catch (IOException e) {
				throw ThrowableUtilities.propagate(e);
			}
		} finally {
			ThrowableUtilities.runQuietly(input::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
		}
	}),
	EXTENSION_RESIZE_NE_SW_CURSOR(() -> {
		InputStream input = AssertionUtilities.assertNonnull(EnumGLFWCursor.class.getResourceAsStream(StaticHolder.getExtensionResizeNeSwCursorPath()));
		try {
			try {
				return createCursor(input, new Point(8, 8));
			} catch (IOException e) {
				throw ThrowableUtilities.propagate(e);
			}
		} finally {
			ThrowableUtilities.runQuietly(input::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
		}
	}),
	;

	protected final long handle;

	EnumGLFWCursor(LongSupplier handle) { this.handle = handle.getAsLong(); }

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	public static long createCursor(InputStream input, Point2D hotspot)
			throws IOException {
		try (NativeImage ni = NativeImage.read(NativeImage.PixelFormat.RGBA, input);
		     GLFWImage i = MinecraftImageUtilities.toGLFWImageCursor(ni)) {
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
