package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.core.IHookEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.AbstractBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;

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
			return getStage() == EnumHookStage.PRE;
		}

		@Override
		public EnumHookStage getStage() {
			return stage;
		}

		@Override
		public boolean hasResult() {
			return getStage() == EnumHookStage.PRE;
		}
	}
}
