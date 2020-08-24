package $group__.client.ui.core.mvvm.views.events;

import $group__.client.ui.core.structures.IUIDataKeyboardKeyPress;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

public interface IUIEventKeyboard extends IUIEvent {
	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	String
			TYPE_KEY_DOWN_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "keydown",
			TYPE_KEY_UP_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "keyup";
	INamespacePrefixedString
			TYPE_KEY_DOWN = new NamespacePrefixedString(TYPE_KEY_DOWN_STRING),
			TYPE_KEY_UP = new NamespacePrefixedString(TYPE_KEY_UP_STRING);

	IUIDataKeyboardKeyPress getData();
}
