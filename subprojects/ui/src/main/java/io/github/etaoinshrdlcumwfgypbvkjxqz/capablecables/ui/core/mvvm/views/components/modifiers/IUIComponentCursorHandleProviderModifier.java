package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentCursorHandleProviderModifier {
	Optional<Long> getCursorHandle(IUIComponentContext context);

	enum StaticHolder {
		;

		@SuppressWarnings({"UnstableApiUsage", "RedundantTypeArguments"})
		public static Optional<Long> handleComponentModifiers(IUIComponentCursorHandleProviderModifier component,
		                                                      Iterable<? extends IUIComponentModifier> modifiers,
		                                                      IUIComponentContext context) {
			return IUIComponentModifier.StaticHolder.handleComponentModifiers(component,
					Lists.reverse(ImmutableList.copyOf(modifiers)),
					IUIComponentCursorHandleProviderModifier.class,
					modifier -> modifier.getCursorHandle(context),
					cursors -> Streams.stream(cursors).sequential()
							.filter(Optional<Long>::isPresent)
							.map(Optional<Long>::get)
							.findFirst()
							.orElse(null));
		}
	}
}
