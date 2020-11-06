package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;

public class UIAlreadyInfiniteAnimationTimelineException
		extends IllegalStateException {
	private static final long serialVersionUID = -2624098572570732061L;

	public UIAlreadyInfiniteAnimationTimelineException() {}

	public UIAlreadyInfiniteAnimationTimelineException(@Nullable CharSequence message) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null));
	}

	public UIAlreadyInfiniteAnimationTimelineException(@Nullable CharSequence message, @Nullable Throwable cause) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null), cause);
	}

	public UIAlreadyInfiniteAnimationTimelineException(@Nullable Throwable cause) {
		super(cause);
	}
}
