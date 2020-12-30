package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ComponentTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Renderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.IJAXBUIComponentThemeAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentBasedAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentThemeAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.contexts.JAXBUIImmutableComponentThemeAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers.JAXBUIDefaultComponentThemeAdapterRendererHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UILambdaTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

public class JAXBUIDefaultComponentThemeAdapter
		extends JAXBUIAbstractComponentBasedAdapter<ComponentTheme, IUITheme>
		implements IJAXBUIComponentThemeAdapter<ComponentTheme, IUITheme> {
	public static <T extends JAXBUIDefaultComponentThemeAdapter> T makeParserStandard(T instance) {
		instance.addObjectHandler(Renderer.class, new JAXBUIDefaultComponentThemeAdapterRendererHandler());
		return instance;
	}

	@Override
	@Deprecated
	@SuppressWarnings({"unchecked"})
	public @Nonnull IUITheme leftToRight(@Nonnull ComponentTheme left) {
		return getThreadLocalContext()
				.map(context -> {
					try {
						IJAXBUIComponentThemeAdapterContext subContext = new JAXBUIImmutableComponentThemeAdapterContext(JAXBUIComponentUtilities.adaptUsingFromJAXB(left.getUsing().iterator()),
								getObjectHandlers(),
								getElementHandlers(),
								new UILambdaTheme.Builder());

						Iterables.concat(
								left.getRenderer()
						).forEach(element ->
								IJAXBUIComponentBasedAdapterContext.findHandler(subContext, element)
										.ifPresent(handler ->
												handler.accept(context.withData(
														MapUtilities.concatMaps(context.getDataView(),
																ImmutableMap.of(IJAXBUIComponentThemeAdapterContext.class, subContext))
														),
														CastUtilities.castUnchecked(element)))
						);

						return subContext.getBuilder().build();
					} catch (ClassNotFoundException e) {
						throw ThrowableUtilities.propagate(e);
					}
				})
				.orElseThrow(IllegalStateException::new);
	}

	@Override
	@Deprecated
	public @Nonnull ComponentTheme rightToLeft(@Nonnull IUITheme right) {
		throw new UnsupportedOperationException(); // TODO implement
	}
}
