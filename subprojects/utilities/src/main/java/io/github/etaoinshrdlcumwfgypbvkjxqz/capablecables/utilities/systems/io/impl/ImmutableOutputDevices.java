package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IGraphicsDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IOutputDevices;

import java.util.Optional;

public final class ImmutableOutputDevices
		implements IOutputDevices {
	private final @Nullable IGraphicsDevice graphicsDevice;

	protected ImmutableOutputDevices(@Nullable IGraphicsDevice graphicsDevice) {
		this.graphicsDevice = graphicsDevice;
	}

	private static ImmutableOutputDevices of(@Nullable IGraphicsDevice graphicsDevice) {
		return new ImmutableOutputDevices(graphicsDevice);
	}

	@Override
	public Optional<? extends IGraphicsDevice> getGraphicsDevice() {
		return Optional.ofNullable(graphicsDevice);
	}

	public static class Builder {
		private @Nullable IGraphicsDevice graphicsDevice;

		public Builder(IOutputDevices source) {
			this();
			this.graphicsDevice = source.getGraphicsDevice().orElse(null);
		}

		public Builder() {}

		public Builder snapshot() {
			getGraphicsDevice()
					.map(ImmutableGraphicsDevice::of)
					.ifPresent(this::setGraphicsDevice);
			return this;
		}

		protected Optional<? extends IGraphicsDevice> getGraphicsDevice() {
			return Optional.ofNullable(graphicsDevice);
		}

		public Builder setGraphicsDevice(@Nullable IGraphicsDevice graphicsDevice) {
			this.graphicsDevice = graphicsDevice;
			return this;
		}

		public ImmutableOutputDevices build() {
			return of(
					getGraphicsDevice().orElse(null)
			);
		}
	}
}
