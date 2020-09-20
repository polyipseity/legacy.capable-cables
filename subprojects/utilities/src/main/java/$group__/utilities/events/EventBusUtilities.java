package $group__.utilities.events;

import $group__.utilities.*;
import $group__.utilities.templates.CommonConfigurationTemplate;
import io.reactivex.rxjava3.subjects.Subject;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventListenerHelper;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;

public enum EventBusUtilities {
	;

	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static boolean callWithPrePostHooks(Subject<Event> bus, BooleanSupplier action, Event pre, Event post) {
		if (!pre.hasResult())
			throw ThrowableUtilities.logAndThrow(
					new IllegalArgumentException(
							new LogMessageBuilder()
									.addMarkers(UtilitiesMarkers.getInstance()::getMarkerEvent)
									.addKeyValue("bus", bus).addKeyValue("action", action).addKeyValue("pre", pre).addKeyValue("post", post)
									.addMessages(() -> getResourceBundle().getString("event.call.hooks.result.void"))
									.build()
					),
					UtilitiesConfiguration.getInstance().getLogger()
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

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static void runWithPrePostHooks(Subject<Event> bus, Runnable action, Event pre, Event post) {
		bus.onNext(pre);
		action.run();
		bus.onNext(post);
	}

	public static <E extends Event> void cleanListenersCache(IEventBus bus, Class<E> eventType) {
		if (UtilitiesConstants.BUILD_TYPE.isDebug()) {
			/* COMMENT
			Only clean up when debugging as
			- Rarely useful in production. The extra memory used is not large enough that it deserves such a fix.
			  Instead, the main purpose for this fix is to help debugging logical memory leaks.
			- EventListenerHelper.getListenerList(Class) is slow.
			- According to https://github.com/MinecraftForge/EventBus/issues/39#issuecomment-664039387, this *can* result in a race condition.
			  The exact nature of this race condition is unknown to us at this time.
			 */
			ThrowableUtilities.Try.call(() ->
					(int) DynamicUtilities.IMPL_LOOKUP.findGetter(EventBus.class, "busID", int.class).invokeExact((EventBus) bus), UtilitiesConfiguration.getInstance().getLogger())
					.ifPresent(id ->
							EventListenerHelper.getListenerList(eventType).getListeners(id));
		}
	}
}
