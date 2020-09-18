package $group__.utilities.events;

import $group__.utilities.CastUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.UtilitiesConfiguration;
import $group__.utilities.interfaces.IDelegating;
import net.jodah.typetools.TypeResolver;
import net.minecraftforge.eventbus.api.*;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;

public class EventBusBridgeMethodFixConsumer<T extends Event, O>
		extends IDelegating.Impl<O>
		implements Consumer<T> {
	// TODO Should a PR be created to fix 'EventBus' not checking whether methods with the 'SubscribeEvent' annotation is a bridge method? (Since bridge methods also have the annotation, methods that have a bridge method in runtime will have the bridge method registered along side with the original method, which is likely undesirable. The bridge method will have its parameter's type erased, meaning the parameter type will become the upper bound type. This means, in our case, for 'Observer', the argument type is 'Object', which causes a crash. However, for super methods that have its parameter's generic type erased to Event, it can cause subtle bugs, such as unexpected ClassCastExceptions caused by dispatching Event and its subtypes to the bridge method, which calls the original method.)

	protected final Class<T> eventType;
	protected final EventPriority priority;
	protected final boolean receiveCancelled;
	@Nullable
	protected final Class<?> genericClassFilter;
	protected final MethodHandle methodHandle;

	@SuppressWarnings("unchecked")
	public EventBusBridgeMethodFixConsumer(O delegated, Class<? super O> superClass, String methodName)
			throws Throwable {
		super(delegated);

		Optional<Class<?>> et = DynamicUtilities.Extensions.wrapTypeResolverResult(TypeResolver.resolveRawArgument(superClass, delegated.getClass()));
		if (!et.isPresent())
			throw ThrowableUtilities.BecauseOf.illegalArgument("Cannot resolve generic type",
					"delegated", this.delegated);
		this.eventType = (Class<T>) et.get(); // COMMENT should be of the right type

		Method m = Try.call(() ->
				this.delegated.getClass().getDeclaredMethod(methodName, this.eventType), UtilitiesConfiguration.INSTANCE.getLogger())
				.orElseThrow(ThrowableCatcher::rethrow);
		// TODO Java 9 - use LambdaMetaFactory
		this.methodHandle = DynamicUtilities.IMPL_LOOKUP.unreflect(m);

		Optional<SubscribeEvent> se = Optional.ofNullable(m.getAnnotation(SubscribeEvent.class));
		this.priority = se.map(SubscribeEvent::priority).orElse(EventPriority.NORMAL);
		this.receiveCancelled = se.map(SubscribeEvent::receiveCanceled).orElse(false);

		if (IGenericEvent.class.isAssignableFrom(this.eventType)) {
			genericClassFilter = DynamicUtilities.Extensions.wrapTypeResolverResult(
					TypeResolver.resolveRawArgument(
							m.getGenericParameterTypes()[0],
							IGenericEvent.class))
					.orElse(Object.class);
		} else
			genericClassFilter = null;
	}

	@Override
	public void accept(T t) {
		Try.run(() -> getMethodHandle().invoke(getDelegated(), t), UtilitiesConfiguration.INSTANCE.getLogger());
		ThrowableCatcher.rethrow(true);
	}

	protected MethodHandle getMethodHandle() { return methodHandle; }

	public void register(IEventBus bus) {
		if (!getGenericClassFilter().filter(gcf -> {
			bus.addGenericListener(
					CastUtilities.castUnchecked(gcf),
					getPriority(), shouldReceiveCancelled(),
					CastUtilities.castUnchecked(getEventType()),
					CastUtilities.castUnchecked(this));
			return true;
		}).isPresent()) {
			bus.addListener(getPriority(), shouldReceiveCancelled(),
					getEventType(),
					this);
		}
	}

	protected Optional<Class<?>> getGenericClassFilter() { return Optional.ofNullable(genericClassFilter); }

	protected EventPriority getPriority() { return priority; }

	protected boolean shouldReceiveCancelled() { return receiveCancelled; }

	protected Class<T> getEventType() { return eventType; }
}
