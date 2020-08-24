package $group__.client.ui.core.mvvm.views.events;

import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

public interface IUIEventChar extends IUIEvent {
	String
			TYPE_CHAR_TYPED_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "char_typed";
	INamespacePrefixedString
			TYPE_CHAR_TYPED = new NamespacePrefixedString(TYPE_CHAR_TYPED_STRING);

	char getCodePoint();

	int getModifiers();
}
