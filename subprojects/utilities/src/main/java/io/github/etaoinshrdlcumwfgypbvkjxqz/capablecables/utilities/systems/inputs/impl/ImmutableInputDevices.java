package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import java.util.Optional;

public final class ImmutableInputDevices
		implements IInputDevices {
	private final ITicker ticker;
	private final @Nullable IInputPointerDevice pointerDevice;

	protected ImmutableInputDevices(ITicker ticker,
	                                @Nullable IInputPointerDevice pointerDevice) {
		this.ticker = ticker;
		this.pointerDevice = pointerDevice;
	}

	private static ImmutableInputDevices of(ITicker ticker,
	                                        @Nullable IInputPointerDevice pointerDevice) {
		return new ImmutableInputDevices(ticker, pointerDevice);
	}

	@Override
	public ITicker getTicker() {
		return ticker;
	}

	@Override
	public @Immutable Optional<? extends IInputPointerDevice> getPointerDevice() {
		return Optional.ofNullable(pointerDevice);
	}

	public static class Builder {
		private final ITicker ticker;
		private @Nullable IInputPointerDevice pointerDevice;

		public Builder(IInputDevices source) {
			this(source.getTicker());
			this.pointerDevice = source.getPointerDevice().orElse(null);
		}

		public Builder(ITicker ticker) {
			this.ticker = ticker;
		}

		public Builder snapshot() {
			// COMMENT ticker is immutable by design
			getPointerDevice()
					.map(ImmutableInputPointerDevice::of)
					.ifPresent(this::setPointerDevice);
			return this;
		}

		protected Optional<? extends IInputPointerDevice> getPointerDevice() {
			return Optional.ofNullable(pointerDevice);
		}

		public ImmutableInputDevices build() {
			return of(getTicker(), getPointerDevice().orElse(null));
		}

		public Builder setPointerDevice(@Nullable IInputPointerDevice pointerDevice) {
			this.pointerDevice = pointerDevice;
			return this;
		}

		protected ITicker getTicker() {
			return ticker;
		}
	}
}
