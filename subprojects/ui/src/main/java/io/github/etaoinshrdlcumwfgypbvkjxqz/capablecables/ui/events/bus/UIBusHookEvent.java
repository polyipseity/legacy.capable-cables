package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.HookEvent;

public abstract class UIBusHookEvent
		extends HookEvent {
	protected UIBusHookEvent(EnumHookStage stage) { super(stage); }
}
