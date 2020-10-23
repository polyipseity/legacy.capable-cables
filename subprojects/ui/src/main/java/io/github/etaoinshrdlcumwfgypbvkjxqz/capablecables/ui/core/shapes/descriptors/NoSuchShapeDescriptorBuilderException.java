package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors;

import javax.annotation.Nullable;
import java.util.Optional;

public class NoSuchShapeDescriptorBuilderException
		extends IllegalArgumentException {
	private static final long serialVersionUID = -2996369735165885614L;

	public NoSuchShapeDescriptorBuilderException() {}

	public NoSuchShapeDescriptorBuilderException(@Nullable CharSequence message) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null));
	}
}
