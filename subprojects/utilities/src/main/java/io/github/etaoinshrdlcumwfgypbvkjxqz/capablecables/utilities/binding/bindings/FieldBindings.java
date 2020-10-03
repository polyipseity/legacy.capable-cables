package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.bindings;

import com.google.common.cache.Cache;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import sun.misc.Cleaner;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

public class FieldBindings
		extends AbstractBindings<IBindingField<?>> {
	protected final Map<IBindingField<?>, Disposable> fields =
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_TINY).makeMap();
	protected final AtomicBoolean isSource = new AtomicBoolean(true);

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public FieldBindings(INamespacePrefixedString bindingKey,
	                     Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
		super(bindingKey, transformersSupplier);
		@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingField<?>, Disposable> fieldsRef = fields;
		Cleaner.create(CleanerUtilities.getCleanerReferent(this), () ->
				fieldsRef.values().stream().unordered().forEach(Disposable::dispose));
	}

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public boolean add(Iterable<? extends IBindingField<?>> fields)
			throws NoSuchBindingTransformerException {
		return Streams.stream(fields).sequential() // COMMENT sequential, field binding order matters
				.filter(key -> !getFields().containsKey(key))
				.reduce(false, IThrowingBiFunction.executeNow((r, f) -> {
					getFields().keySet().stream().unordered()
							.findAny()
							.ifPresent(IThrowingConsumer.executeNow(fc ->
									f.setValue(CastUtilities.castUncheckedNullable( // COMMENT should be of the right type
											transform(getTransformers(),
													CastUtilities.castUncheckedNullable(fc.getValue().orElse(null)), // COMMENT should be always safe
													fc.getGenericClass(),
													f.getGenericClass())))));
					DisposableObserver<? extends IValue<?>> d = createSynchronizationObserver(f, getFields().keySet(), getTransformers(), getIsSource());
					getFields().put(f, d);
					f.getField().getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
					return true;
				}), Boolean::logicalOr);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Map<IBindingField<?>, Disposable> getFields() { return fields; }

	public static <T> DisposableObserver<IValue<T>> createSynchronizationObserver(IBindingField<T> from,
	                                                                              Iterable<? extends IBindingField<?>> to,
	                                                                              Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers,
	                                                                              AtomicBoolean isSource) {
		return new LoggingDisposableObserver<>(new DefaultDisposableObserver<IValue<T>>() {
			@Override
			public void onNext(@Nonnull IValue<T> t) {
				if (isSource.getAndSet(false)) {
					for (IBindingField<?> k : to) {
						if (!k.equals(from)) {
							try {
								k.setValue(CastUtilities.castUncheckedNullable(
										transform(transformers, t.getValue().orElse(null), from.getGenericClass(), k.getGenericClass()))); // COMMENT should be of the correct type
							} catch (NoSuchBindingTransformerException ex) {
								isSource.set(true);
								onError(ex);
								break;
							}
						}
					}
					isSource.set(true);
				}
			}
		}, UtilitiesConfiguration.getInstance().getLogger());
	}

	public AtomicBoolean getIsSource() { return isSource; }

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public boolean remove(Iterable<? extends IBindingField<?>> fields) {
		return Streams.stream(fields).unordered()
				.filter(getFields()::containsKey)
				.reduce(false, (r, f) -> {
					Disposable d = AssertionUtilities.assertNonnull(getFields().remove(f));
					d.dispose();
					return true;
				}, Boolean::logicalOr);
	}

	@Override
	public boolean removeAll() {
		if (isEmpty())
			return false;
		getFields().values().stream().unordered().forEach(Disposable::dispose);
		getFields().clear();
		return true;
	}

	@Override
	public boolean isEmpty() { return getFields().isEmpty(); }
}
