package $group__.ui.core.mvvm.views.events;

import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

import java.util.Optional;

public interface IUIEventFocus extends IUIEvent {
	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	String TYPE_FOCUS_OUT_POST_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "blur",
			TYPE_FOCUS_IN_POST_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "focus",
			TYPE_FOCUS_IN_PRE_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "focusin",
			TYPE_FOCUS_OUT_PRE_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "focusout";
	INamespacePrefixedString TYPE_FOCUS_OUT_POST = new NamespacePrefixedString(TYPE_FOCUS_OUT_POST_STRING),
			TYPE_FOCUS_IN_POST = new NamespacePrefixedString(TYPE_FOCUS_IN_POST_STRING),
			TYPE_FOCUS_IN_PRE = new NamespacePrefixedString(TYPE_FOCUS_IN_PRE_STRING),
			TYPE_FOCUS_OUT_PRE = new NamespacePrefixedString(TYPE_FOCUS_OUT_PRE_STRING);

	Optional<? extends IUIEventTarget> getRelatedTarget();
}
