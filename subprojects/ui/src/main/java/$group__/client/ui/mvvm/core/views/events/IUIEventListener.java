package $group__.client.ui.mvvm.core.views.events;

import $group__.utilities.interfaces.ICloneable;

import java.util.function.Consumer;

public interface IUIEventListener<E extends IUIEvent>
		extends Consumer<E>, ICloneable {
	void markAsRemoved();
}
