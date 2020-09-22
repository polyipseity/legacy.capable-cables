package $group__.utilities.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ISubscribeEventSupplier {
	Optional<? extends SubscribeEvent> getSubscribeEvent();
}
