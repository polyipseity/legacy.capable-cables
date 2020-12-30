package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;

public class NoSuchShapeDescriptorBuilderException
		extends IllegalArgumentException {
	private static final long serialVersionUID = -2996369735165885614L;

	public NoSuchShapeDescriptorBuilderException() {}

	public NoSuchShapeDescriptorBuilderException(@Nullable CharSequence message) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null));
	}
}
