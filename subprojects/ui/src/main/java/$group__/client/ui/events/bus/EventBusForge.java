package $group__.client.ui.events.bus;

import $group__.utilities.CastUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
import net.jodah.typetools.TypeResolver;
import net.minecraftforge.eventbus.api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class EventBusForge
		extends Subject<Event> {
	protected final IEventBus delegated;

	public EventBusForge(IEventBus delegated) { this.delegated = delegated; }

	@Override
	public boolean hasObservers() { return true; }

	@Override
	public boolean hasThrowable() { return false; }

	@Override
	public boolean hasComplete() { return false; }

	@Override
	@Nullable
	public Throwable getThrowable() { return null; }

	@Override
	protected void subscribeActual(@Nonnull Observer<? super Event> observer) {
		@SuppressWarnings("unchecked") Observer<? extends Event> oa =
				(Observer<? extends Event>) observer; // COMMENT consider parameter 'observer' 'Observer<? extends Event>' instead, this works due to type erasure
		ObserverConsumer<? extends Event> oc;
		try {
			oc = new ObserverConsumer<>(oa);
		} catch (Throwable t) {
			onError(t);
			return;
		}
		if (!oc.getGenericClassFilter().filter(gcf -> {
			getDelegated().addGenericListener(
					CastUtilities.castUnchecked(gcf),
					oc.getPriority(), oc.shouldReceiveCancelled(),
					CastUtilities.castUnchecked(oc.getEventType()),
					CastUtilities.castUnchecked(oc));
			return true;
		}).isPresent()) {
			getDelegated().addListener(oc.getPriority(), oc.shouldReceiveCancelled(),
					CastUtilities.castUnchecked(oc.getEventType()),
					oc);
		}
		observer.onSubscribe(new EventBusForgeDisposable(getDelegated(), oc));
	}

	protected IEventBus getDelegated() { return delegated; }

	@Override
	public void onSubscribe(@Nonnull Disposable d) { throw new UnsupportedOperationException(); }

	@Override
	public void onNext(@Nonnull Event o) { getDelegated().post(o); }

	@Override
	public void onError(@Nonnull Throwable e) { throw ThrowableUtilities.propagate(e); }

	@Override
	public void onComplete() { throw new UnsupportedOperationException(); }

	protected static class EventBusForgeDisposable
			implements Disposable {
		protected final AtomicBoolean disposed = new AtomicBoolean();
		protected final IEventBus delegated;
		protected final Consumer<? extends Event> downstream;

		public EventBusForgeDisposable(IEventBus delegated, Consumer<? extends Event> downstream) {
			this.delegated = delegated;
			this.downstream = downstream;
		}

		@Override
		public void dispose() {
			if (!getDisposed().getAndSet(true))
				getDelegated().unregister(getDownstream());
		}

		@Override
		public boolean isDisposed() { return getDisposed().get(); }

		protected AtomicBoolean getDisposed() { return disposed; }

		public IEventBus getDelegated() { return delegated; }

		protected Consumer<? extends Event> getDownstream() { return downstream; }
	}

	protected static class ObserverConsumer<T extends Event>
			implements Consumer<T> {
		// TODO Should a PR be created to fix 'EventBus' not checking whether methods with the 'SubscribeEvent' annotation is a bridge method? (Since bridge methods also have the annotation, methods that have a bridge method in runtime will have the bridge method registered along side with the original method, which is likely undesirable. The bridge method will have its parameter's type erased, meaning the parameter type will become the upper bound type. This means, in our case, for 'Observer', the argument type is 'Object', which causes a crash. However, for super methods that have its parameter's generic type erased to Event, it can cause subtle bugs, such as unexpected ClassCastExceptions caused by dispatching Event and its subtypes to the bridge method, which calls the original method.)
		private static final Logger LOGGER = LogManager.getLogger();

		protected final Observer<T> delegated;
		protected final Class<T> eventType;
		protected final EventPriority priority;
		protected final boolean receiveCancelled;
		@Nullable
		protected final Class<?> genericClassFilter;

		@SuppressWarnings({"unchecked", "RedundantThrows"})
		public ObserverConsumer(Observer<T> delegated)
				throws Throwable {
			this.delegated = delegated;

			Optional<Class<?>> et = DynamicUtilities.Extensions.wrapTypeResolverResult(TypeResolver.resolveRawArgument(Observer.class, delegated.getClass()));
			if (!et.isPresent())
				throw BecauseOf.illegalArgument("Cannot resolve generic type",
						"delegated", delegated);
			this.eventType = (Class<T>) et.get(); // COMMENT should be of the right type

			Method onNext = Try.call(() ->
					delegated.getClass().getDeclaredMethod("onNext", eventType), LOGGER)
					.orElseThrow(ThrowableCatcher::rethrow);

			Optional<SubscribeEvent> se = Optional.ofNullable(onNext.getAnnotation(SubscribeEvent.class));
			this.priority = se.map(SubscribeEvent::priority).orElse(EventPriority.NORMAL);
			this.receiveCancelled = se.map(SubscribeEvent::receiveCanceled).orElse(false);

			if (IGenericEvent.class.isAssignableFrom(eventType)) {
				genericClassFilter = DynamicUtilities.Extensions.wrapTypeResolverResult(
						TypeResolver.resolveRawArgument(
								onNext.getGenericParameterTypes()[0],
								IGenericEvent.class))
						.orElse(Object.class);
			} else
				genericClassFilter = null;
		}

		@Override
		public void accept(T t) { getDelegated().onNext(t); }

		protected Observer<T> getDelegated() { return delegated; }

		protected Optional<Class<?>> getGenericClassFilter() { return Optional.ofNullable(genericClassFilter); }

		protected EventPriority getPriority() { return priority; }

		protected boolean shouldReceiveCancelled() { return receiveCancelled; }

		protected Class<T> getEventType() { return eventType; }
	}
}
