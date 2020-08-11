package $group__.client.ui.mvvm.views.events.bus;

import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.utilities.events.EnumEventHookStage;

public abstract class EventUIComponent extends EventUI {
	protected final IUIComponent component;

	protected EventUIComponent(EnumEventHookStage stage, IUIComponent component) {
		super(stage);
		this.component = component;
	}

	public IUIComponent getComponent() { return component; }

	public static abstract class Inbound extends EventUIComponent {
		protected Inbound(EnumEventHookStage stage, IUIComponent component) { super(stage, component); }
	}

	public static abstract class Outbound extends EventUIComponent {
		protected Outbound(EnumEventHookStage stage, IUIComponent component) { super(stage, component); }
	}
}
