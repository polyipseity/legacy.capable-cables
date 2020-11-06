package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings;

import com.google.common.cache.Cache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.bindings.IBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBindings<B extends IBinding<?>>
		implements IBindings<B> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	private final Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> transformersSupplier;

	private final INamespacePrefixedString bindingKey;

	protected AbstractBindings(INamespacePrefixedString bindingKey,
	                           Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> transformersSupplier) {
		this.bindingKey = bindingKey;
		this.transformersSupplier = transformersSupplier;
	}

	public static <T, R> R transform(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformers, T value, Class<T> from, Class<R> to)
			throws NoSuchBindingTransformerException {
		return getTransformer(transformers, from, to)
				.apply(value);
	}

	public static <T, R> Function<@Nonnull T, @Nonnull R> getTransformer(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformers, Class<T> from, Class<R> to)
			throws NoSuchBindingTransformerException {
		return findTransformer(transformers, from, to)
				.orElseThrow(() -> new NoSuchBindingTransformerException(
								new LogMessageBuilder()
										.addMarkers(UtilitiesMarkers.getInstance()::getMarkerBinding)
										.addKeyValue("transformers", transformers).addKeyValue("from", from).addKeyValue("to", to)
										.addMessages(() -> getResourceBundle().getString("transformer.find.missing"))
										.build()
						)
				);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> Optional<Function<@Nonnull T, @Nonnull R>> findTransformer(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformers, Class<T> from, Class<R> to) {
		return from.equals(to) ? Optional.of(CastUtilities::castUncheckedNullable) :
				Optional.ofNullable(transformers.getIfPresent(from))
						.map(m -> (Function<@Nonnull T, @Nonnull R>) m.getIfPresent(to)); // COMMENT should be of the right type
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.of(bindingKey); }

	protected Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> getTransformers() { return getTransformersSupplier().get(); }

	protected Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> getTransformersSupplier() { return transformersSupplier; }
}
