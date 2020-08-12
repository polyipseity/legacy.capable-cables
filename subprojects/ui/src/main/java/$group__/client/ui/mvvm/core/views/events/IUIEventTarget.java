package $group__.client.ui.mvvm.core.views.events;

import net.minecraft.util.ResourceLocation;

public interface IUIEventTarget {
	boolean addEventListener(ResourceLocation type, IUIEventListener<?> listener, boolean useCapture);

	boolean removeEventListener(ResourceLocation type, IUIEventListener<?> listener, boolean useCapture);

	boolean dispatchEvent(IUIEvent event);

	boolean isActive();

	boolean isFocusable();
}
