package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;

public abstract class UIViewBusEvent extends UIBusEvent {
	protected final IUIView<?> view;

	protected UIViewBusEvent(EnumHookStage stage, IUIView<?> view) {
		super(stage);
		this.view = view;
	}

	public IUIView<?> getView() { return view; }
}
