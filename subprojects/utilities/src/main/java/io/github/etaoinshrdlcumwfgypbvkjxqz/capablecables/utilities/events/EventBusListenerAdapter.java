package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.ClassUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import net.jodah.typetools.TypeResolver;
import net.minecraftforge.eventbus.api.*;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EventBusListenerAdapter<T extends Event, O>
		extends AbstractDelegatingObject<O>
		implements Consumer<T> {
	// TODO Should a PR be created to fix 'EventBus' not checking whether methods with the 'SubscribeEvent' annotation is a bridge method? (Since bridge methods also have the annotation, methods that have a bridge method in runtime will have the bridge method registered along side with the original method, which is likely undesirable. The bridge method will have its parameter's type erased, meaning the parameter type will become the upper bound type. This means, in our case, for 'Observer', the argument type is 'Object', which causes a crash. However, for super methods that have its parameter's generic type erased to Event, it can cause subtle bugs, such as unexpected ClassCastExceptions caused by dispatching Event and its subtypes to the bridge method, which calls the original method.)

	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private final Class<T> eventType;
	private final EventPriority priority;
	private final boolean receiveCancelled;
	@Nullable
	private final Class<?> genericClassFilter;
	private final MethodHandle methodHandle;

	@SuppressWarnings({"unchecked"})
	public EventBusListenerAdapter(O delegated, Class<? super O> superClass, @NonNls CharSequence methodName)
			throws NoSuchMethodException {
		super(delegated);
		Class<?> delegatedClazz = delegated.getClass();

		Optional<Class<?>> et = DynamicUtilities.Extensions.wrapTypeResolverResult(TypeResolver.resolveRawArgument(superClass, delegatedClazz));
		if (!et.isPresent())
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UtilitiesMarkers.getInstance()::getMarkerEvent)
							.addKeyValue("delegated", delegated).addKeyValue("superClass", superClass).addKeyValue("methodName", methodName)
							.addMessages(() -> getResourceBundle().getString("construct.delegated.generics.unresolvable"))
							.build()
			);
		this.eventType = (Class<T>) et.get(); // COMMENT should be of the right type

		Method m;
		{
			Optional<Method> methodTemporary = ClassUtilities.getAnyMethod(delegatedClazz, methodName, eventType); // COMMENT quick search
			if (!methodTemporary.isPresent()) {
				// COMMENT extensive search
				methodTemporary = ClassUtilities.getThisAndSuperclassesAndInterfaces(eventType).stream().sequential()
						.flatMap(Collection::stream)
						.map(otherEventType -> ClassUtilities.getAnyMethod(delegatedClazz, methodName, otherEventType))
						.filter(Optional::isPresent)
						.map(Optional::get)
						.findFirst();
			}
			m = methodTemporary
					.orElseThrow(NoSuchMethodException::new);
		}
		try {
			// TODO Java 9 - use LambdaMetaFactory
			this.methodHandle = InvokeUtilities.getImplLookup().unreflect(m);
		} catch (IllegalAccessException e) {
			throw ThrowableUtilities.propagate(e);
		}

		Optional<? extends SubscribeEvent> se = Optional.ofNullable(m.getAnnotation(SubscribeEvent.class));
		if (!se.isPresent() && delegated instanceof ISubscribeEventProvider)
			se = ((ISubscribeEventProvider) delegated).getSubscribeEvent();
		this.priority = se.map(SubscribeEvent::priority).orElse(EventPriority.NORMAL);
		this.receiveCancelled = se.filter(SubscribeEvent::receiveCanceled).isPresent();

		@Nullable Class<?> genericClassFilter1;
		if (IGenericEvent.class.isAssignableFrom(this.eventType)) {
			try {
				genericClassFilter1 = DynamicUtilities.Extensions.wrapTypeResolverResult(
						TypeResolver.resolveRawArgument(
								m.getGenericParameterTypes()[0],
								IGenericEvent.class))
						.orElse(Object.class);
			} catch (IllegalArgumentException e) {
				// COMMENT this means that the event does not care about the generic type
				// CODE extends IGenericEvent<Void>
				genericClassFilter1 = null;
			}
		} else
			genericClassFilter1 = null;
		this.genericClassFilter = genericClassFilter1;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public void accept(T t) {
		try {
			getMethodHandle().invoke(getDelegate(), t);
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}

	protected MethodHandle getMethodHandle() { return methodHandle; }

	public void register(IEventBus bus) {
		if (!getGenericClassFilter().filter(gcf -> {
			bus.addGenericListener(
					CastUtilities.castUnchecked(gcf),
					getPriority(), isReceiveCancelled(),
					CastUtilities.castUnchecked(getEventType()),
					CastUtilities.castUnchecked(this));
			return true;
		}).isPresent()) {
			bus.addListener(getPriority(), isReceiveCancelled(),
					getEventType(),
					this);
		}
	}

	protected Optional<Class<?>> getGenericClassFilter() { return Optional.ofNullable(genericClassFilter); }

	protected EventPriority getPriority() { return priority; }

	protected boolean isReceiveCancelled() { return receiveCancelled; }

	protected Class<T> getEventType() { return eventType; }
}
