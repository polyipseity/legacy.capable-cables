package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBinding;

public interface IUIRenderer<C>
		extends IHasBinding, IHasBindingMap, ITypeCapture {
	void onRendererAdded(C container);

	void onRendererRemoved();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<? extends C> getTypeToken();
}
