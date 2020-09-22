package $group__.ui.mvvm.views.events.bus;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.events.bus.UIBusEvent;
import $group__.utilities.events.EnumHookStage;

public abstract class UIComponentBusEvent extends UIBusEvent {
	protected final IUIComponent component;

	protected UIComponentBusEvent(EnumHookStage stage, IUIComponent component) {
		super(stage);
		this.component = component;
	}

	public IUIComponent getComponent() { return component; }

	public static class ModifyShapeDescriptor extends UIComponentBusEvent {
		public ModifyShapeDescriptor(EnumHookStage stage, IUIComponent component) { super(stage, component); }

		@Override
		public boolean isCancelable() { return getStage().isPre(); }

		@Override
		public boolean hasResult() { return getStage().isPre(); }
	}
}
