package $group__.ui.core.mvvm.views.events;

import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

public interface IUIEventMouseWheel extends IUIEventMouse {
	String
			TYPE_WHEEL_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "wheel";
	INamespacePrefixedString
			TYPE_WHEEL = new NamespacePrefixedString(TYPE_WHEEL_STRING);

	double getDelta();
}
