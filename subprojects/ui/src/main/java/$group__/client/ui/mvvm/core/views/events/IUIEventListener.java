package $group__.client.ui.mvvm.core.views.events;

import java.util.function.Consumer;

public interface IUIEventListener<E extends IUIEvent>
		extends Consumer<E> {
	void markAsRemoved();
}
