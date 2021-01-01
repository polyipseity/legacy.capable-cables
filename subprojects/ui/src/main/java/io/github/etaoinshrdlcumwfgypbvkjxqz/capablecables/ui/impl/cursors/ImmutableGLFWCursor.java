package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl.AbstractCursor;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public final class ImmutableGLFWCursor
		extends AbstractCursor {
	private final long handle;
	private final Runnable destroyer;

	private ImmutableGLFWCursor(long handle) {
		this.handle = handle;
		this.destroyer = new OneUseRunnable(() -> {
			long handle1 = getHandle();
			if (handle1 != MemoryUtil.NULL)
				GLFW.glfwDestroyCursor(handle1);
		});
	}

	@Override
	public long getHandle() {
		return handle;
	}

	@Override
	public void close() {
		getDestroyer().run();
	}

	protected Runnable getDestroyer() {
		return destroyer;
	}

	public static ImmutableGLFWCursor of(long handle) {
		return new ImmutableGLFWCursor(handle);
	}
}
