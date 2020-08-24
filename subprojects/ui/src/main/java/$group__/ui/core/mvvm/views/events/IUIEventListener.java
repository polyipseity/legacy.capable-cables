package $group__.ui.core.mvvm.views.events;

import java.util.function.Consumer;

public interface IUIEventListener<E extends IUIEvent>
		extends Consumer<E> {
	void markAsRemoved();
}
