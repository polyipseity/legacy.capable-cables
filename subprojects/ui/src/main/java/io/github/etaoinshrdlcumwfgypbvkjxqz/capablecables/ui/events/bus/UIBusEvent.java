package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.HookEvent;

public abstract class UIBusEvent extends HookEvent {
	protected UIBusEvent(EnumHookStage stage) { super(stage); }
}
