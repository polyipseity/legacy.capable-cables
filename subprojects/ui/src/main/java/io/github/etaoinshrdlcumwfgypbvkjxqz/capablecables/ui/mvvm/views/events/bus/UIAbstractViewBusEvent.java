package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.AbstractBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.IHookEvent;

import javax.annotation.Nullable;

public abstract class UIAbstractViewBusEvent<T>
		extends AbstractBusEvent<T> {
	private final IUIView<?> view;

	protected UIAbstractViewBusEvent(@Nullable Class<T> genericType, IUIView<?> view) {
		super(genericType);
		this.view = view;
	}

	public IUIView<?> getView() { return view; }

	public static class Render
			extends UIAbstractViewBusEvent<Void>
			implements IHookEvent {
		private final EnumHookStage stage;

		public Render(EnumHookStage stage, IUIView<?> view) {
			super(Void.class, view);
			this.stage = stage;
		}

		@Override
		public boolean isCancelable() {
			return getStage().isPre();
		}

		@Override
		public EnumHookStage getStage() {
			return stage;
		}

		@Override
		public boolean hasResult() {
			return getStage().isPre();
		}
	}
}
