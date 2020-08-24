package $group__.client.ui.mvvm.core.views.events;

import $group__.utilities.interfaces.INamespacePrefixedString;

public interface IUIEventTarget {
	boolean addEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture);

	boolean removeEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture);

	boolean dispatchEvent(IUIEvent event);

	boolean isActive();

	boolean isFocusable();
}
