package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentCursorHandleProviderModifier {
	@SuppressWarnings({"UnstableApiUsage", "RedundantTypeArguments"})
	static Optional<Long> handleComponentModifiers(IUIComponentCursorHandleProviderModifier component,
	                                               Iterable<? extends IUIComponentModifier> modifiers,
	                                               IUIComponentContext context) {
		return IUIComponentModifier.handleComponentModifiers(component,
				Lists.reverse(ImmutableList.copyOf(modifiers)),
				IUIComponentCursorHandleProviderModifier.class,
				modifier -> modifier.getCursorHandle(context),
				cursors -> Streams.stream(cursors)
						.filter(Optional<Long>::isPresent)
						.map(Optional<Long>::get)
						.findFirst()
						.orElse(null));
	}

	Optional<Long> getCursorHandle(IUIComponentContext context);
}
