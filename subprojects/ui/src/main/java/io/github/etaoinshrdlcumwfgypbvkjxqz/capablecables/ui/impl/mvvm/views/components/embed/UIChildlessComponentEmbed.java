package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentEmbedArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.embed.IUIComponentEmbed;

import java.util.List;

public class UIChildlessComponentEmbed<C extends IUIComponent>
		extends UIAbstractComponentEmbed<C> {
	public UIChildlessComponentEmbed(Class<C> type, IUIComponent owner, IUIComponentEmbedArguments arguments) {
		super(type, owner, arguments);
	}

	@Override
	public List<? extends IUIComponentEmbed<?>> getChildrenView() {
		return ImmutableList.of();
	}
}
