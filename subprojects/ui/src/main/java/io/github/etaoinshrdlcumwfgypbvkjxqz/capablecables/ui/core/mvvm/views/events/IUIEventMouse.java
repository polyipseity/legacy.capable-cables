package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIMouseButtonClickData;

import java.util.Optional;

public interface IUIEventMouse extends IUIEvent {
	IUIMouseButtonClickData getData();

	Optional<? extends IUIEventTarget> getRelatedTarget();
}
