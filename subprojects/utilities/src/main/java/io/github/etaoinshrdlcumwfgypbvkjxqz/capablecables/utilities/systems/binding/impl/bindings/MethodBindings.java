package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings;

import com.google.common.cache.Cache;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.slf4j.Logger;
import sun.misc.Cleaner;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

public class MethodBindings
		extends AbstractBindings<IBindingMethod<?>> {
	private final Map<IBindingMethodSource<?>, Disposable> sources =
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacityTiny()).makeMap();
	private final Set<IBindingMethodDestination<?>> destinations =
			Collections.newSetFromMap(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacityTiny()).makeMap());

	public MethodBindings(INamespacePrefixedString bindingKey,
	                      Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> transformersSupplier) {
		super(bindingKey, transformersSupplier);
		@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingMethodSource<?>, Disposable> sourcesRef = sources;
		Cleaner.create(suppressThisEscapedWarning(() -> this), () ->
				sourcesRef.values().stream().unordered().forEach(Disposable::dispose));
	}

	@Override
	@SuppressWarnings({"SuspiciousMethodCalls", "UnstableApiUsage"})
	public boolean add(Iterable<? extends IBindingMethod<?>> methods) {
		return stripBool(
				Streams.stream(methods).unordered()
						.mapToInt(method -> {
							boolean ret;
							switch (method.getMethodType()) {
								case SOURCE:
									if (!getSources().containsKey(method)) {
										IBindingMethodSource<?> s = (IBindingMethodSource<?>) method;
										DisposableObserver<?> d = new MethodDelegatingDisposableObserver<>(getTransformers(),
												s,
												getDestinations(),
												UtilitiesConfiguration.getInstance().getLogger());
										s.getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
										getSources().put(s, d);
										ret = true;
										break;
									}
									ret = false;
									break;
								case DESTINATION:
									ret = getDestinations().add((IBindingMethodDestination<?>) method);
									break;
								default:
									throw new AssertionError();
							}
							return padBool(ret);
						})
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<IBindingMethodSource<?>, Disposable> getSources() { return sources; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IBindingMethodDestination<?>> getDestinations() { return destinations; }

	@Override
	@SuppressWarnings({"SuspiciousMethodCalls", "UnstableApiUsage"})
	public boolean remove(Iterable<? extends IBindingMethod<?>> methods) {
		return stripBool(
				Streams.stream(methods).unordered()
						.mapToInt(method -> {
							boolean ret;
							switch (method.getMethodType()) {
								case SOURCE:
									Optional<Disposable> disposable = Optional.ofNullable(getSources().remove(method));
									disposable.ifPresent(Disposable::dispose);
									ret = disposable.isPresent();
									break;
								case DESTINATION:
									ret = getDestinations().remove(method);
									break;
								default:
									throw new AssertionError();
							}
							return padBool(ret);
						})
						.reduce(fBool(), PaddedBool::orBool)
		);
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

	public static class MethodDelegatingDisposableObserver<T>
			extends LoggingDisposableObserver<T> {
		private final Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformersRef;
		private final IBindingMethodSource<T> source;
		private final Iterable<? extends IBindingMethodDestination<?>> destinationsRef;

		public MethodDelegatingDisposableObserver(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformersRef,
		                                          IBindingMethodSource<T> source,
		                                          Iterable<? extends IBindingMethodDestination<?>> destinationsRef,
		                                          Logger logger) {
			super(logger);
			this.transformersRef = transformersRef;
			this.source = source;
			this.destinationsRef = destinationsRef;
		}

		@SuppressWarnings("UnstableApiUsage")
		@Override
		public void onNext(@Nonnull T t) {
			super.onNext(t);
			try {
				getDestinationsRef().forEach(IThrowingConsumer.executeNow(destination -> {
					AssertionUtilities.assertNonnull(destination).accept(CastUtilities.castUnchecked(
							transform(getTransformersRef(),
									t,
									getSource().getTypeToken().getRawType(),
									destination.getTypeToken().getRawType()))); // COMMENT should be of the correct type
				}));
			} catch (NoSuchBindingTransformerException ex) {
				onError(ex);
			}
		}

		protected Iterable<? extends IBindingMethodDestination<?>> getDestinationsRef() {
			return destinationsRef;
		}

		protected Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> getTransformersRef() {
			return transformersRef;
		}

		protected IBindingMethodSource<T> getSource() {
			return source;
		}
	}
}
