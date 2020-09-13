package $group__.ui.core.mvvm.views.events;

import $group__.ui.core.structures.IUIDataMouseButtonClick;

import java.util.Optional;

public interface IUIEventMouse extends IUIEvent {
	IUIDataMouseButtonClick getData();

	Optional<? extends IUIEventTarget> getRelatedTarget();
}
