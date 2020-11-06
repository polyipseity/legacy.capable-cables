package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.function.Consumer;

public interface IUIEventListener<E extends IUIEvent>
		extends Consumer<@Nonnull E> {
	void markAsRemoved();

	@Override
	void accept(@Nonnull E e);
}
