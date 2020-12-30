package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.def.IMouseButtonClickData;

import java.util.Optional;

public interface IUIEventMouse extends IUIEvent {
	IMouseButtonClickData getData();

	Optional<? extends IUIEventTarget> getRelatedTarget();
}
