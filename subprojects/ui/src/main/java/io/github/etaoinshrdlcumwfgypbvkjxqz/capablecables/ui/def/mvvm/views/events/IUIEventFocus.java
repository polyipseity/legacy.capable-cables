package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events;

import java.util.Optional;

public interface IUIEventFocus extends IUIEvent {
	Optional<? extends IUIEventTarget> getRelatedTarget();
}
