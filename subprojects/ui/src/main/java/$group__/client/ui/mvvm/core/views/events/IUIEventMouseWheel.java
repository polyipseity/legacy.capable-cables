package $group__.client.ui.mvvm.core.views.events;

import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;

public interface IUIEventMouseWheel extends IUIEventMouse {
	String
			TYPE_WHEEL_STRING = NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "wheel";
	ResourceLocation
			TYPE_WHEEL = new ResourceLocation(TYPE_WHEEL_STRING);

	double getDelta();
}
