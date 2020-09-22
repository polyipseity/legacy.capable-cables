package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

public interface IUIEventType {
	@NonNls
	String getEventTypeString();

	INamespacePrefixedString getEventType();
}
