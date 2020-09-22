package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataMouseButtonClick;

import java.util.Optional;

public interface IUIEventMouse extends IUIEvent {
	IUIDataMouseButtonClick getData();

	Optional<? extends IUIEventTarget> getRelatedTarget();
}
