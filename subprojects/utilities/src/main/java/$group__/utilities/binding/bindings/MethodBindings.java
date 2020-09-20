package $group__.utilities.binding.bindings;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.binding.core.BindingTransformerNotFoundException;
import $group__.utilities.binding.core.methods.IBindingMethod;
import $group__.utilities.binding.core.methods.IBindingMethodDestination;
import $group__.utilities.binding.core.methods.IBindingMethodSource;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.INamespacePrefixedString;
import com.google.common.cache.Cache;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import sun.misc.Cleaner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodBindings
		extends AbstractBindings<IBindingMethod<?>> {
	protected final Map<IBindingMethodSource<?>, Disposable> sources =
			MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_TINY).makeMap();
	protected final Set<IBindingMethodDestination<?>> destinations =
			Collections.newSetFromMap(MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_TINY).makeMap());
	protected final Object cleanerRef = new Object();

	public MethodBindings(INamespacePrefixedString bindingKey,
	                      Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
		super(bindingKey, transformersSupplier);
		@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingMethodSource<?>, Disposable> sourcesRef = sources;
		Cleaner.create(getCleanerRef(), () ->
				sourcesRef.values().stream().unordered().forEach(Disposable::dispose));
	}

	protected final Object getCleanerRef() { return cleanerRef; }

	@Override
	@SuppressWarnings({"SuspiciousMethodCalls", "UnstableApiUsage"})
	public boolean add(Iterable<? extends IBindingMethod<?>> methods) {
		return Streams.stream(methods).unordered()
				.reduce(false, (r, m) -> {
					switch (m.getMethodType()) {
						case SOURCE:
							if (!getSources().containsKey(m)) {
								IBindingMethodSource<?> s = (IBindingMethodSource<?>) m;
								DisposableObserver<?> d = createDelegatingObserver(s, getDestinations(), getTransformers());
								s.getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
								getSources().put(s, d);
								return true;
							}
							return false;
						case DESTINATION:
							return getDestinations().add((IBindingMethodDestination<?>) m);
						default:
							throw new InternalError();
					}
				}, Boolean::logicalOr);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<IBindingMethodSource<?>, Disposable> getSources() { return sources; }

	public static <T> DisposableObserver<T> createDelegatingObserver(IBindingMethodSource<T> source,
	                                                                 Iterable<? extends IBindingMethodDestination<?>> destination,
	                                                                 Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> transformers) {
		return new DisposableObserverAuto<T>() {
			@Override
			public void onNext(@Nonnull T t) {
				for (IBindingMethodDestination<?> d : destination) {
					try {
						d.accept(CastUtilities.castUncheckedNullable(
								transform(transformers, t, source.getGenericClass(), d.getGenericClass()))); // COMMENT should be of the correct type
					} catch (BindingTransformerNotFoundException ex) {
						onError(ex);
						break;
					}
				}
			}
		};
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IBindingMethodDestination<?>> getDestinations() { return destinations; }

	@Override
	@SuppressWarnings({"SuspiciousMethodCalls", "UnstableApiUsage"})
	public boolean remove(Iterable<? extends IBindingMethod<?>> methods) {
		return Streams.stream(methods).unordered()
				.reduce(false, (r, m) -> {
					switch (m.getMethodType()) {
						case SOURCE:
							@Nullable Disposable d = getSources().remove(m);
							if (d != null) {
								d.dispose();
								return true;
							}
							return false;
						case DESTINATION:
							return getDestinations().remove(m);
						default:
							throw new InternalError();
					}
				}, Boolean::logicalOr);
	}

	@Override
	public boolean removeAll() {
		if (isEmpty())
			return false;
		getSources().values().stream().unordered().forEach(Disposable::dispose);
		getSources().clear();
		getDestinations().clear();
		return true;
	}

	@Override
	public boolean isEmpty() { return getSources().isEmpty() && getDestinations().isEmpty(); }
}
