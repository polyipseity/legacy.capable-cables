package $group__.utilities.binding.bindings;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.binding.core.BindingTransformerNotFoundException;
import $group__.utilities.binding.core.IBinding;
import $group__.utilities.binding.core.bindings.IBindings;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.cache.Cache;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBindings<B extends IBinding<?>>
		implements IBindings<B> {
	protected final INamespacePrefixedString bindingKey;
	protected final Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier;

	protected AbstractBindings(INamespacePrefixedString bindingKey,
	                           Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
		this.bindingKey = bindingKey;
		this.transformersSupplier = transformersSupplier;
	}

	@Nullable
	public static <T, R> R transform(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers, @Nullable T value, Class<T> from, Class<R> to)
			throws BindingTransformerNotFoundException {
		return getTransformer(transformers, from, to)
				.orElseThrow(() -> new BindingTransformerNotFoundException(
						"Cannot find transformer for '" + from + "' -> '" + to + "' in transformers:" + System.lineSeparator()
								+ transformers))
				.apply(value);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> Optional<Function<T, R>> getTransformer(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers, Class<T> from, Class<R> to) {
		return from.equals(to) ? Optional.of(CastUtilities::castUncheckedNullable) :
				Optional.ofNullable(transformers.getIfPresent(from))
						.map(m -> (Function<T, R>) m.getIfPresent(to)); // COMMENT should be of the right type
	}

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.of(bindingKey); }

	protected Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> getTransformers() { return AssertionUtilities.assertNonnull(getTransformersSupplier().get()); }

	protected Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> getTransformersSupplier() { return transformersSupplier; }
}
