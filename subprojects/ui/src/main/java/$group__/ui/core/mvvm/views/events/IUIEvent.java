package $group__.ui.core.mvvm.views.events;

import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

public interface IUIEvent {
	String
			TYPE_SELECT_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "select";
	INamespacePrefixedString
			TYPE_SELECT = new NamespacePrefixedString(TYPE_SELECT_STRING);

	INamespacePrefixedString getType();

	IUIEventTarget getTarget();

	EnumPhase getPhase();

	void advancePhase();

	void reset();

	boolean canBubble();

	boolean isPropagationStopped();

	void stopPropagation();

	boolean isCancelable();

	boolean isDefaultPrevented();

	boolean preventDefault();

	enum EnumPhase {
		NONE,
		CAPTURING_PHASE,
		AT_TARGET,
		BUBBLING_PHASE,
	}
}
