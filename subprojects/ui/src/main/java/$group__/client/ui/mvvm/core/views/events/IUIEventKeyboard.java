package $group__.client.ui.mvvm.core.views.events;

import $group__.client.ui.core.structures.IUIDataKeyboardKeyPress;
import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;

public interface IUIEventKeyboard extends IUIEvent {
	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	String
			TYPE_KEY_DOWN_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "keydown",
			TYPE_KEY_UP_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "keyup";
	ResourceLocation
			TYPE_KEY_DOWN = new ResourceLocation(TYPE_KEY_DOWN_STRING),
			TYPE_KEY_UP = new ResourceLocation(TYPE_KEY_UP_STRING);

	IUIDataKeyboardKeyPress getData();
}
