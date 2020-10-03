package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputPointerDevice;

import javax.annotation.Nullable;
import java.util.Optional;

public final class ImmutableInputDevices
		implements IInputDevices {
	@Nullable
	private final IInputPointerDevice pointerDevice;

	protected ImmutableInputDevices(@Nullable IInputPointerDevice pointerDevice) {
		this.pointerDevice = pointerDevice;
	}

	private static ImmutableInputDevices of(@Nullable IInputPointerDevice pointerDevice) { return new ImmutableInputDevices(pointerDevice); }

	@Override
	public Optional<? extends IInputPointerDevice> getPointerDevice() {
		return Optional.ofNullable(pointerDevice);
	}

	public static class Builder {
		@Nullable
		private IInputPointerDevice pointerDevice;

		public Builder() {}

		public Builder(IInputDevices source) {
			this.pointerDevice = source.getPointerDevice().orElse(null);
		}

		public Builder strongImmutability() {
			getPointerDevice()
					.map(ImmutableInputPointerDevice::of)
					.ifPresent(this::setPointerDevice);
			return this;
		}

		protected Optional<? extends IInputPointerDevice> getPointerDevice() {
			return Optional.ofNullable(pointerDevice);
		}

		public Builder setPointerDevice(@Nullable IInputPointerDevice pointerDevice) {
			this.pointerDevice = pointerDevice;
			return this;
		}

		public ImmutableInputDevices build() {
			return of(getPointerDevice().orElse(null));
		}
	}
}
