package $group__.client.ui.mvvm.views.core.events;

import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface IUIEventFocus extends IUIEvent {
	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	String TYPE_FOCUS_OUT_POST_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "blur",
			TYPE_FOCUS_IN_POST_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "focus",
			TYPE_FOCUS_IN_PRE_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "focusin",
			TYPE_FOCUS_OUT_PRE_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "focusout";
	ResourceLocation TYPE_FOCUS_OUT_POST = new ResourceLocation(TYPE_FOCUS_OUT_POST_STRING),
			TYPE_FOCUS_IN_POST = new ResourceLocation(TYPE_FOCUS_IN_POST_STRING),
			TYPE_FOCUS_IN_PRE = new ResourceLocation(TYPE_FOCUS_IN_PRE_STRING),
			TYPE_FOCUS_OUT_PRE = new ResourceLocation(TYPE_FOCUS_OUT_PRE_STRING);

	Optional<IUIEventTarget> getRelatedTarget();
}
