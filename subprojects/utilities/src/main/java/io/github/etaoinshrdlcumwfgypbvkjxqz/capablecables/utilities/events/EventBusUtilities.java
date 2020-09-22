package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import io.reactivex.rxjava3.subjects.Subject;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventListenerHelper;
import net.minecraftforge.eventbus.api.IEventBus;

import java.lang.invoke.MethodHandle;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;

public enum EventBusUtilities {
	;

	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	private static final MethodHandle BUS_ID_GETTER_METHOD_HANDLE;

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static void runWithPrePostHooks(Subject<Event> bus, Runnable action, Event pre, Event post) {
		bus.onNext(pre);
		action.run();
		bus.onNext(post);
	}

	static {
		try {
			BUS_ID_GETTER_METHOD_HANDLE = DynamicUtilities.IMPL_LOOKUP.findGetter(EventBus.class, "busID", int.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	public static boolean callWithPrePostHooks(Subject<Event> bus, BooleanSupplier action, Event pre, Event post) {
		if (!pre.hasResult())
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UtilitiesMarkers.getInstance()::getMarkerEvent)
							.addKeyValue("bus", bus).addKeyValue("action", action).addKeyValue("pre", pre).addKeyValue("post", post)
							.addMessages(() -> getResourceBundle().getString("event.call.hooks.result.void"))
							.build()
			);
		bus.onNext(pre);
		boolean r;
		switch (pre.getResult()) {
			case DEFAULT:
				r = action.getAsBoolean();
				break;
			case ALLOW:
				r = true;
				break;
			case DENY:
				r = false;
				break;
			default:
				throw new InternalError();
		}
		bus.onNext(post);
		return r;
	}

	public static <E extends Event> void cleanListenersCache(IEventBus bus, Class<E> eventType) {
		if (UtilitiesConstants.BUILD_TYPE.isDebug()) {
			/* COMMENT
			Only clean up while debugging as
			- Rarely useful in production. The extra memory used is not large enough that it deserves such a fix.
			  Instead, the main purpose for this fix is to help debugging logical memory leaks.
			- EventListenerHelper.getListenerList(Class) is slow.
			- According to https://github.com/MinecraftForge/EventBus/issues/39#issuecomment-664039387, this *can* result in a race condition.
			  The exact nature of this race condition is unknown to us at this time.
			 */
			ThrowableUtilities.getQuietly(() ->
					(int) BUS_ID_GETTER_METHOD_HANDLE.invokeExact((EventBus) bus), Throwable.class, UtilitiesConfiguration.getInstance().getThrowableHandler())
					.ifPresent(id ->
							EventListenerHelper.getListenerList(eventType).getListeners(id));
		}
	}
}
