package $group__.ui.core.mvvm.views.events;

import $group__.utilities.structures.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

public interface IUIEventType {
	@NonNls
	String getEventTypeString();

	INamespacePrefixedString getEventType();
}
