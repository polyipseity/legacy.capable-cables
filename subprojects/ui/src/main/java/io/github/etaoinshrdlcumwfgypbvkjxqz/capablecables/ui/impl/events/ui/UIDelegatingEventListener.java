package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;

public class UIDelegatingEventListener<E extends IUIEvent>
		extends AbstractDelegatingObject<IUIEventListener<? super E>>
		implements IUIEventListener<E> {
	public UIDelegatingEventListener(IUIEventListener<? super E> delegate) {
		super(delegate);
	}

	@Override
	public void markAsRemoved() {
		getDelegate().markAsRemoved();
	}

	@Override
	public void accept(@Nonnull E e) {
		getDelegate().accept(e);
	}
}
