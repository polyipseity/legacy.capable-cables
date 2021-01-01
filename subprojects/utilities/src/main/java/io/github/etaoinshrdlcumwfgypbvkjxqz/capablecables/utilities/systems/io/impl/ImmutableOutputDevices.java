package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IOutputDevices;
import sun.misc.Cleaner;

import javax.annotation.WillClose;
import javax.annotation.WillCloseWhenClosed;
import javax.annotation.WillNotClose;
import java.awt.*;
import java.util.Optional;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public final class ImmutableOutputDevices
		implements IOutputDevices {
	private final @Nullable Graphics2D graphics;

	protected ImmutableOutputDevices(@Nullable @WillCloseWhenClosed Graphics2D graphics) {
		this.graphics = graphics;

		registerGraphicsDisposer(suppressThisEscapedWarning(() -> this), this.graphics);
	}

	protected static void registerGraphicsDisposer(Object referent, @Nullable @WillClose Graphics2D graphics) {
		if (graphics != null)
			Cleaner.create(referent, graphics::dispose);
	}

	private static ImmutableOutputDevices of(@Nullable @WillNotClose Graphics2D graphics) {
		return new ImmutableOutputDevices(
				(Graphics2D) Optional.ofNullable(graphics)
						.map(Graphics::create)
						.orElse(null)
		);
	}

	@Override
	public Optional<? extends Graphics2D> createGraphics() {
		return getGraphics()
				.map(Graphics::create)
				.map(Graphics2D.class::cast);
	}

	protected Optional<? extends Graphics2D> getGraphics() {
		return Optional.ofNullable(graphics);
	}

	public static class Builder {
		private @Nullable Graphics2D graphics;

		public Builder(IOutputDevices source) {
			this();

			this.graphics = source.createGraphics().orElse(null);

			registerGraphicsDisposer(suppressThisEscapedWarning(() -> this), this.graphics);
		}

		public Builder() {}

		public Builder snapshot() {
			// COMMENT graphics cloned
			return this;
		}

		public ImmutableOutputDevices build() {
			return of(
					getGraphics().orElse(null) // COMMENT graphics automatically cloned
			);
		}

		public Builder setGraphics(@Nullable @WillNotClose Graphics2D graphics) {
			getGraphics().ifPresent(Graphics::dispose);
			this.graphics = (Graphics2D) Optional.ofNullable(graphics)
					.map(Graphics::create)
					.orElse(null);
			registerGraphicsDisposer(this, this.graphics);
			return this;
		}


		protected Optional<? extends Graphics2D> getGraphics() {
			return Optional.ofNullable(graphics);
		}
	}
}
