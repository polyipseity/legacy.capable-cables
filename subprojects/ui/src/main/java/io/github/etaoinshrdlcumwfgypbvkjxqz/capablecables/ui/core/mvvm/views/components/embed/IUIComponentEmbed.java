package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.embed;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;

import java.util.List;

public interface IUIComponentEmbed<C extends IUIComponent>
		extends ITypeCapture {
	C getComponent();

	List<? extends IUIComponentEmbed<?>> getChildrenView();

	Runnable getEmbedInitializer();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<C> getTypeToken();
}
