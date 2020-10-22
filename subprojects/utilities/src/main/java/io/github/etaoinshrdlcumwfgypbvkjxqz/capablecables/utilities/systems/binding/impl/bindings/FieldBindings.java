package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings;

import com.google.common.cache.Cache;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import sun.misc.Cleaner;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FieldBindings
		extends AbstractBindings<IBindingField<?>> {
	private final Map<IBindingField<?>, Disposable> fields =
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacityTiny()).makeMap();
	private final AtomicBoolean isSource = new AtomicBoolean(true);

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
		return Streams.stream(fields) // COMMENT sequential, field binding order matters
				.filter(key -> !getFields().containsKey(key))
				.reduce(false, IThrowingBiFunction.executeNow((r, f) -> {
					assert f != null;
					getFields().keySet().stream().unordered()
							.findAny()
							.ifPresent(IThrowingConsumer.executeNow(fc -> {
								assert fc != null;
								f.setValue(CastUtilities.castUncheckedNullable( // COMMENT should be of the right type
										transform(getTransformers(),
												CastUtilities.castUncheckedNullable(fc.getValue().orElse(null)), // COMMENT should be always safe
												fc.getGenericClass(),
												f.getGenericClass())));
							}));
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
			@SuppressWarnings("UnstableApiUsage")
			@Override
			public void onNext(@Nonnull IValue<T> t) {
				if (isSource.getAndSet(false)) {
					try {
						Streams.stream(to).unordered()
								.filter(FunctionUtilities.notPredicate(Predicate.isEqual(from)))
								.forEach(IThrowingConsumer.executeNow(destination -> {
									AssertionUtilities.assertNonnull(destination).setValue(CastUtilities.castUncheckedNullable(
											transform(transformers, t.getValue().orElse(null), from.getGenericClass(), destination.getGenericClass()))); // COMMENT should be of the correct type
								}));
					} catch (NoSuchBindingTransformerException e) {
						isSource.set(true);
						onError(e);
						return;
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
