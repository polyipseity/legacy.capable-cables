package $group__.utilities.binding.bindings;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.binding.core.BindingTransformerNotFoundException;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.IValue;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.INamespacePrefixedString;
import com.google.common.cache.Cache;
import com.google.common.collect.Streams;
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
			MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_TINY).makeMap();
	protected final AtomicBoolean isSource = new AtomicBoolean(true);
	protected final Object cleanerRef = new Object();

	public FieldBindings(INamespacePrefixedString bindingKey,
	                     Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
		super(bindingKey, transformersSupplier);
		@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingField<?>, Disposable> fieldsRef = fields;
		Cleaner.create(getCleanerRef(), () ->
				fieldsRef.values().stream().unordered().forEach(Disposable::dispose));
	}

	protected final Object getCleanerRef() { return cleanerRef; }

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public boolean add(Iterable<? extends IBindingField<?>> fields)
			throws BindingTransformerNotFoundException {
		return Streams.stream(fields).sequential() // COMMENT sequential, field binding order matters
				.filter(key -> !getFields().containsKey(key))
				.reduce(false, (r, f) -> {
					getFields().keySet().stream().unordered()
							.findAny()
							.ifPresent(fc ->
									f.setValue(CastUtilities.castUncheckedNullable( // COMMENT should be of the right type
											transform(getTransformers(),
													CastUtilities.castUncheckedNullable(fc.getValue().orElse(null)), // COMMENT should be always safe
													fc.getGenericClass(),
													f.getGenericClass()))));
					DisposableObserver<? extends IValue<?>> d = createSynchronizationObserver(f, getFields().keySet(), getTransformers(), getIsSource());
					getFields().put(f, d);
					f.getField().getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
					return true;
				}, Boolean::logicalOr);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Map<IBindingField<?>, Disposable> getFields() { return fields; }

	public static <T> DisposableObserver<IValue<T>> createSynchronizationObserver(IBindingField<T> from,
	                                                                              Iterable<? extends IBindingField<?>> to,
	                                                                              Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers,
	                                                                              AtomicBoolean isSource) {
		return new DisposableObserverAuto<IValue<T>>() {
			@Override
			public void onNext(@Nonnull IValue<T> t) {
				if (isSource.getAndSet(false)) {
					for (IBindingField<?> k : to) {
						if (!k.equals(from)) {
							try {
								k.setValue(CastUtilities.castUncheckedNullable(
										transform(transformers, t.getValue().orElse(null), from.getGenericClass(), k.getGenericClass()))); // COMMENT should be of the correct type
							} catch (BindingTransformerNotFoundException ex) {
								onError(ex);
								isSource.set(true);
								break;
							}
						}
					}
					isSource.set(true);
				}
			}
		};
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
