package $group__.ui.mvvm.views.events.bus;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.events.bus.EventUI;
import $group__.utilities.events.EnumEventHookStage;

public abstract class EventUIComponent extends EventUI {
	protected final IUIComponent component;

	protected EventUIComponent(EnumEventHookStage stage, IUIComponent component) {
		super(stage);
		this.component = component;
	}

	public IUIComponent getComponent() { return component; }

	public static class ShapeDescriptorModify extends EventUIComponent {
		public ShapeDescriptorModify(EnumEventHookStage stage, IUIComponent component) { super(stage, component); }
	}
}
