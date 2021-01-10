package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IGraphicsDevice;
import sun.misc.Cleaner;

import java.awt.*;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public final class ImmutableGraphicsDevice
		extends AbstractDelegatingObject<IGraphicsDevice>
		implements IGraphicsDevice {
	private final Graphics2D graphics;

	protected ImmutableGraphicsDevice(IGraphicsDevice delegate) {
		super(delegate);
		this.graphics = delegate.createGraphics();
		Cleaner.create(suppressThisEscapedWarning(() -> this), this.graphics::dispose);
	}

	public static ImmutableGraphicsDevice of(IGraphicsDevice delegate) {
		return new ImmutableGraphicsDevice(delegate);
	}

	@Override
	public Graphics2D createGraphics() {
		return (Graphics2D) getGraphics().create();
	}

	protected Graphics2D getGraphics() {
		return graphics;
	}
}
