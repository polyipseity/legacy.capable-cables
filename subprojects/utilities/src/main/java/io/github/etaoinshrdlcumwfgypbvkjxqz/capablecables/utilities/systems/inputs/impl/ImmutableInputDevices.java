package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IKeyboardDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import java.util.Optional;

public final class ImmutableInputDevices
		implements IInputDevices {
	private final ITicker ticker;
	private final @Nullable IPointerDevice pointerDevice;
	private final @Nullable IKeyboardDevice keyboardDevice;

	protected ImmutableInputDevices(ITicker ticker,
	                                @Nullable IPointerDevice pointerDevice,
	                                @Nullable IKeyboardDevice keyboardDevice) {
		this.ticker = ticker;
		this.pointerDevice = pointerDevice;
		this.keyboardDevice = keyboardDevice;
	}

	private static ImmutableInputDevices of(ITicker ticker,
	                                        @Nullable IPointerDevice pointerDevice,
	                                        @Nullable IKeyboardDevice keyboardDevice) {
		return new ImmutableInputDevices(ticker, pointerDevice, keyboardDevice);
	}

	@Override
	public ITicker getTicker() {
		return ticker;
	}

	@Override
	public Optional<@Immutable ? extends IPointerDevice> getPointerDevice() {
		return Optional.ofNullable(pointerDevice);
	}

	@Override
	public Optional<@Immutable ? extends IKeyboardDevice> getKeyboardDevice() {
		return Optional.ofNullable(keyboardDevice);
	}

	public static class Builder {
		private final ITicker ticker;
		private @Nullable IPointerDevice pointerDevice;
		private @Nullable IKeyboardDevice keyboardDevice;

		public Builder(IInputDevices source) {
			this(source.getTicker());
			this.pointerDevice = source.getPointerDevice().orElse(null);
			this.keyboardDevice = source.getKeyboardDevice().orElse(null);
		}

		public Builder(ITicker ticker) {
			this.ticker = ticker;
		}

		public Builder snapshot() {
			// COMMENT ticker is immutable by design
			getPointerDevice()
					.map(ImmutablePointerDevice::of)
					.ifPresent(this::setPointerDevice);
			getKeyboardDevice()
					.map(ImmutableKeyboardDevice::of)
					.ifPresent(this::setKeyboardDevice);
			return this;
		}

		protected Optional<? extends IPointerDevice> getPointerDevice() {
			return Optional.ofNullable(pointerDevice);
		}

		public Builder setPointerDevice(@Nullable IPointerDevice pointerDevice) {
			this.pointerDevice = pointerDevice;
			return this;
		}

		protected Optional<? extends IKeyboardDevice> getKeyboardDevice() {
			return Optional.ofNullable(keyboardDevice);
		}

		public Builder setKeyboardDevice(@Nullable IKeyboardDevice keyboardDevice) {
			this.keyboardDevice = keyboardDevice;
			return this;
		}

		public ImmutableInputDevices build() {
			return of(getTicker(), getPointerDevice().orElse(null), getKeyboardDevice().orElse(null));
		}

		protected ITicker getTicker() {
			return ticker;
		}
	}
}
