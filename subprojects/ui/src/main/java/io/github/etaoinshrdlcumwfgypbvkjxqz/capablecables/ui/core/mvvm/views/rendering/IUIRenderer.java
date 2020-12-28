package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;

public interface IUIRenderer<C>
		extends IHasBinding, IHasBindingMap, ITypeCapture {
	void onRendererAdded(C container);

	void onRendererRemoved();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<? extends C> getTypeToken();
}
