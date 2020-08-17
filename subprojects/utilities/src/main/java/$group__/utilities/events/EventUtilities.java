package $group__.utilities.events;

import $group__.utilities.ThrowableUtilities.BecauseOf;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.function.Supplier;

public enum EventUtilities {
	;

	public static boolean callWithPrePostHooks(Supplier<Boolean> action, Event pre, Event post) {
		if (!pre.hasResult())
			throw BecauseOf.illegalArgument("Event does not have a result", "pre", pre);
		Bus.FORGE.bus().get().post(pre);
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
		Bus.FORGE.bus().get().post(post);
		return r;
	}
}
