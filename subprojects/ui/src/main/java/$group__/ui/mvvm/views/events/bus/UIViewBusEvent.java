package $group__.ui.mvvm.views.events.bus;

import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.events.bus.UIBusEvent;
import $group__.utilities.events.EnumHookStage;

public abstract class UIViewBusEvent extends UIBusEvent {
	protected final IUIView<?> view;

	protected UIViewBusEvent(EnumHookStage stage, IUIView<?> view) {
		super(stage);
		this.view = view;
	}

	public IUIView<?> getView() { return view; }
}
