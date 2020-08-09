package $group__.client.ui.mvvm.views.core.events;

import java.util.function.Consumer;

public interface IUIEventListener<E extends IUIEvent> extends Consumer<E> {
	void markAsRemoved();
}
