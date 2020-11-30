package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;

import java.util.OptionalLong;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentCursorHandleProviderModifier {
	@SuppressWarnings("UnstableApiUsage")
	static OptionalLong handleComponentModifiers(IUIComponentCursorHandleProviderModifier component,
	                                             Iterable<? extends IUIComponentModifier> modifiers,
	                                             IUIComponentContext context) {
		return OptionalUtilities.ofLong(
				IUIComponentModifier.handleComponentModifiers(component,
						Lists.reverse(ImmutableList.copyOf(modifiers)),
						IUIComponentCursorHandleProviderModifier.class,
						modifier -> modifier.getCursorHandle(context),
						cursors -> OptionalUtilities.valueOf(
								Streams.stream(cursors)
										.filter(OptionalLong::isPresent)
										.mapToLong(OptionalLong::getAsLong)
										.findFirst()
						))
						.orElse(null)
		);
	}

	OptionalLong getCursorHandle(IUIComponentContext context);
}
