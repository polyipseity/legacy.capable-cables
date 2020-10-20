package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.core.IHookEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;

public class UIComponentModifyShapeDescriptorBusEvent
		extends UIAbstractComponentBusEvent<Void>
		implements IHookEvent {
	private final EnumHookStage stage;

	public UIComponentModifyShapeDescriptorBusEvent(EnumHookStage stage, IUIComponent component) {
		super(Void.class, component);
		this.stage = stage;
	}

	@Override
	public boolean isCancelable() { return getStage().isPre(); }

	@Override
	public EnumHookStage getStage() { return stage; }

	@Override
	public boolean hasResult() { return getStage().isPre(); }
}
