package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;

import java.util.function.Function;

public interface IUIComponentEmbedArguments
		extends IUIComponentArguments {
	Function<@Nonnull ? super IUIComponentArguments, @Nonnull ? extends IUIComponent> getConstructor();
}
