package $group__.utilities.binding.bindings;

import $group__.utilities.*;
import $group__.utilities.binding.core.IBinding;
import $group__.utilities.binding.core.NoSuchBindingTransformerException;
import $group__.utilities.binding.core.bindings.IBindings;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.cache.Cache;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBindings<B extends IBinding<?>>
		implements IBindings<B> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	@Nullable
	public static <T, R> R transform(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers, @Nullable T value, Class<T> from, Class<R> to)
			throws NoSuchBindingTransformerException {
		return getTransformer(transformers, from, to)
				.apply(value);
	}

	protected final INamespacePrefixedString bindingKey;
	protected final Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier;

	protected AbstractBindings(INamespacePrefixedString bindingKey,
	                           Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
		this.bindingKey = bindingKey;
		this.transformersSupplier = transformersSupplier;
	}

	public static <T, R> Function<T, R> getTransformer(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers, Class<T> from, Class<R> to)
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
	public static <T, R> Optional<Function<T, R>> findTransformer(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers, Class<T> from, Class<R> to) {
		return from.equals(to) ? Optional.of(CastUtilities::castUncheckedNullable) :
				Optional.ofNullable(transformers.getIfPresent(from))
						.map(m -> (Function<T, R>) m.getIfPresent(to)); // COMMENT should be of the right type
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.of(bindingKey); }

	protected Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> getTransformers() { return AssertionUtilities.assertNonnull(getTransformersSupplier().get()); }

	protected Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> getTransformersSupplier() { return transformersSupplier; }
}
