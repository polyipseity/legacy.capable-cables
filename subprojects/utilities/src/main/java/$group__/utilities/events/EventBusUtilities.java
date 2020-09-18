package $group__.utilities.events;

import $group__.utilities.ConstantsUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.UtilitiesConfiguration;
import io.reactivex.rxjava3.subjects.Subject;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventListenerHelper;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Supplier;

public enum EventBusUtilities {
	;

	public static boolean callWithPrePostHooks(Subject<Event> bus, Supplier<Boolean> action, Event pre, Event post) {
		if (!pre.hasResult())
			throw BecauseOf.illegalArgument("Event does not have a result", "pre", pre);
		bus.onNext(pre);
		boolean r;
		switch (pre.getResult()) {
			case DEFAULT:
				r = action.get();
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

	public static void runWithPrePostHooks(Subject<Event> bus, Runnable action, Event pre, Event post) {
		bus.onNext(pre);
		action.run();
		bus.onNext(post);
	}

	public static <E extends Event> void cleanListenersCache(IEventBus bus, Class<E> eventType) {
		if (ConstantsUtilities.BUILD_TYPE.isDebug()) {
			/* COMMENT
			Only clean up when debugging as
			- Rarely useful in production. The extra memory used is not large enough that it deserves such a fix.
			  Instead, the main purpose for this fix is to help debugging logical memory leaks.
			- EventListenerHelper.getListenerList(Class) is slow.
			- According to https://github.com/MinecraftForge/EventBus/issues/39#issuecomment-664039387, this *can* result in a race condition.
			  The exact nature of this race condition is unknown to us at this time.
			 */
			ThrowableUtilities.Try.call(() ->
					(int) DynamicUtilities.IMPL_LOOKUP.findGetter(EventBus.class, "busID", int.class).invokeExact((EventBus) bus), UtilitiesConfiguration.INSTANCE.getLogger())
					.ifPresent(id ->
							EventListenerHelper.getListenerList(eventType).getListeners(id));
		}
	}
}
