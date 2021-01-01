package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.ICursor;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentCursorHandleProviderModifier {
	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	static Optional<? extends ICursor> handleComponentModifiers(IUIComponentCursorHandleProviderModifier component,
	                                                            Iterable<? extends IUIComponentModifier> modifiers,
	                                                            IUIComponentContext context) {
		return IUIComponentModifier.handleComponentModifiers(component,
				ImmutableList.copyOf(modifiers).reverse(),
				IUIComponentCursorHandleProviderModifier.class,
				modifier -> modifier.getCursorHandle(context),
				cursors -> Streams.stream(cursors)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.findFirst()
						.orElse(null)
		);
	}

	Optional<? extends ICursor> getCursorHandle(IUIComponentContext context);
}
