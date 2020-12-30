package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

public interface IUIEvent {
	IIdentifier getType();

	IUIEventTarget getTarget();

	EnumPhase getPhase();

	void advancePhase()
			throws IllegalStateException;

	void reset();

	boolean canBubble();

	boolean isPropagationStopped();

	void stopPropagation();

	boolean isCancelable();

	boolean isDefaultPrevented();

	@SuppressWarnings("UnusedReturnValue")
	boolean preventDefault();

	@Immutable
	IUIViewContext getViewContext();

	enum EnumPhase {
		NONE {
			@Override
			public EnumPhase getNextPhase() { return CAPTURING_PHASE; }
		},
		CAPTURING_PHASE {
			@Override
			public EnumPhase getNextPhase() { return AT_TARGET; }
		},
		AT_TARGET {
			@Override
			public EnumPhase getNextPhase() { return BUBBLING_PHASE; }
		},
		BUBBLING_PHASE {
			@Override
			public EnumPhase getNextPhase()
					throws IllegalStateException { throw new IllegalStateException(); }
		},
		;

		public abstract EnumPhase getNextPhase()
				throws IllegalStateException;
	}
}
