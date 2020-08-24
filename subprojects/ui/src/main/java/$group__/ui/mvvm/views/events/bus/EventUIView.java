package $group__.ui.mvvm.views.events.bus;

import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.events.bus.EventUI;
import $group__.utilities.events.EnumEventHookStage;

public abstract class EventUIView extends EventUI {
	protected final IUIView<?> view;

	protected EventUIView(EnumEventHookStage stage, IUIView<?> view) {
		super(stage);
		this.view = view;
	}

	public IUIView<?> getView() { return view; }
}
