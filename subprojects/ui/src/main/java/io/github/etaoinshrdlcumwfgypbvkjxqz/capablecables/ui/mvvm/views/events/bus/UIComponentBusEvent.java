package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIBusHookEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;

public abstract class UIComponentBusEvent extends UIBusHookEvent {
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
