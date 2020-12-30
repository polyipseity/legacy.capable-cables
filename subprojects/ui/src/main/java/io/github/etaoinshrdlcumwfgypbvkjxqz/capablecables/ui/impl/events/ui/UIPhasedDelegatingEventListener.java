package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventListener;

import java.util.Set;

public class UIPhasedDelegatingEventListener<E extends IUIEvent>
		extends UIDelegatingEventListener<E> {
	private final @Immutable Set<IUIEvent.EnumPhase> phases;

	protected UIPhasedDelegatingEventListener(IUIEventListener<? super E> delegate,
	                                          Iterable<IUIEvent.EnumPhase> phases) {
		super(delegate);
		this.phases = Sets.immutableEnumSet(phases);
	}

	public static <E extends IUIEvent> UIPhasedDelegatingEventListener<E> of(IUIEvent.EnumPhase phase,
	                                                                         IUIEventListener<? super E> delegate) {
		return of(ImmutableSet.of(phase), delegate);
	}

	public static <E extends IUIEvent> UIPhasedDelegatingEventListener<E> of(Iterable<IUIEvent.EnumPhase> phases,
	                                                                         IUIEventListener<? super E> delegate) {
		return new UIPhasedDelegatingEventListener<>(delegate, phases);
	}

	@Override
	public void accept(@Nonnull E e) {
		if (getPhases().contains(e.getPhase()))
			super.accept(e);
	}

	protected @Immutable Set<IUIEvent.EnumPhase> getPhases() {
		return phases;
	}
}
