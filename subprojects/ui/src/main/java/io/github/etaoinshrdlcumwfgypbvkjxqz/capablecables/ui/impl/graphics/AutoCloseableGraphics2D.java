package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import java.awt.*;

public class AutoCloseableGraphics2D
		extends DelegatingGraphics2D
		implements AutoCloseable {
	private AutoCloseableGraphics2D(Graphics2D delegate) {
		super(delegate);
	}

	public static AutoCloseableGraphics2D of(Graphics2D delegate) {
		if (delegate instanceof AutoCloseableGraphics2D)
			return (AutoCloseableGraphics2D) delegate;
		return new AutoCloseableGraphics2D(delegate);
	}

	@Override
	public void close() {
		dispose();
	}
}
