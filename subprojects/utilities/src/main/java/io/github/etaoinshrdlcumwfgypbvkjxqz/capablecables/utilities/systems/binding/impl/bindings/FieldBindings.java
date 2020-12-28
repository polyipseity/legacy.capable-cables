package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings;

import com.google.common.cache.Cache;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.ReactiveUtilities;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import sun.misc.Cleaner;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

public class FieldBindings
		extends AbstractBindings<IBindingField<?>> {
	private final Map<IBindingField<?>, Disposable> fields =
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacityTiny()).makeMap();
	private final AtomicBoolean isSource = new AtomicBoolean(true);

	public FieldBindings(IIdentifier bindingKey,
	                     Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> transformersSupplier) {
		super(bindingKey, transformersSupplier);
		@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingField<?>, Disposable> fieldsRef = fields;
		Cleaner.create(suppressThisEscapedWarning(() -> this), () ->
				fieldsRef.values().stream().unordered().forEach(Disposable::dispose));
	}

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public boolean add(Iterable<? extends IBindingField<?>> fields)
			throws NoSuchBindingTransformerException {
		return stripBool(
				Streams.stream(fields) // COMMENT sequential, field binding order matters
						.filter(FunctionUtilities.notPredicate(getFields()::containsKey))
						.mapToInt(field -> {
							// COMMENT the code below is probably not needed, 'BehaviorProcessor'-like notifier for 'IObservableField' expected
							/* CODE
							getFields().keySet().stream().unordered()
									.findAny()
									.ifPresent(IThrowingConsumer.executeNow(fc ->
											field.setValue(CastUtilities.castUnchecked( // COMMENT should be of the right type
													transform(getTransformers(),
															CastUtilities.castUnchecked(fc.getValue()), // COMMENT should be always safe
															fc.getTypeToken().getRawType(),
															field.getTypeToken().getRawType()
													)
											))
									));
							 */
							DisposableSubscriber<?> d = FieldSynchronizationSubscriber.ofDecorated(
									getTransformers(),
									field,
									getFields().keySet(),
									getIsSource(),
									UtilitiesConfiguration.getInstance().getLogger()
							);
							getFields().put(field, d);
							field.getField().getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
							return tBool();
						})
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Map<IBindingField<?>, Disposable> getFields() { return fields; }

	public AtomicBoolean getIsSource() { return isSource; }

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public boolean remove(Iterable<? extends IBindingField<?>> fields) {
		return stripBool(
				Streams.stream(fields).unordered()
						.map(getFields()::remove)
						.filter(Objects::nonNull)
						.peek(Disposable::dispose)
						.mapToInt(disposable -> tBool())
						.reduce(fBool(), PaddedBool::orBool)
		);
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

	public static class FieldSynchronizationSubscriber<T>
			extends DelegatingSubscriber<T> {
		private final Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformersRef;
		private final IBindingField<T> from;
		private final Iterable<? extends IBindingField<?>> toRef;
		private final AtomicBoolean isSourceRef;

		protected FieldSynchronizationSubscriber(Subscriber<? super T> delegate,
		                                         Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformersRef,
		                                         IBindingField<T> from,
		                                         Iterable<? extends IBindingField<?>> toRef,
		                                         AtomicBoolean isSourceRef) {
			super(delegate);
			this.transformersRef = transformersRef;
			this.from = from;
			this.toRef = toRef;
			this.isSourceRef = isSourceRef;
		}

		public static <T> DisposableSubscriber<T> ofDecorated(Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>> transformersRef,
		                                                      IBindingField<T> from,
		                                                      Iterable<? extends IBindingField<?>> toRef,
		                                                      AtomicBoolean isSourceRef,
		                                                      Logger logger) {
			return ReactiveUtilities.decorateAsListener(delegate -> new FieldSynchronizationSubscriber<>(delegate, transformersRef, from, toRef, isSourceRef), logger);
		}

		@SuppressWarnings("UnstableApiUsage")
		@Override
		public void onNext(@Nonnull T t) {
			super.onNext(t);
			if (getIsSourceRef().getAndSet(false)) {
				try {
					Streams.stream(getToRef()).unordered()
							.filter(Predicate.isEqual(getFrom()).negate())
							.forEach(IThrowingConsumer.executeNow(destination ->
									destination.setValue(
											CastUtilities.castUnchecked( // COMMENT should be of the correct type
													transform(getTransformersRef(),
															t,
															getFrom().getTypeToken().getRawType(),
															destination.getTypeToken().getRawType()
													)
											)
									)
							));
				} catch (NoSuchBindingTransformerException e) {
					cancel();
					onError(e);
				} finally {
					getIsSourceRef().set(true);
				}
			}
		}

		protected AtomicBoolean getIsSourceRef() {
			return isSourceRef;
		}

		protected Iterable<? extends IBindingField<?>> getToRef() {
			return toRef;
		}

		protected IBindingField<T> getFrom() {
			return from;
		}

		protected Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>> getTransformersRef() {
			return transformersRef;
		}
	}
}
