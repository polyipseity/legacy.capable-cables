package $group__.client.ui.mvvm.core.views.events;

import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;

public interface IUIEventChar extends IUIEvent {
	String
			TYPE_CHAR_TYPED_STRING = NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "char_typed";
	ResourceLocation
			TYPE_CHAR_TYPED = new ResourceLocation(TYPE_CHAR_TYPED_STRING);

	char getCodePoint();

	int getModifiers();
}
