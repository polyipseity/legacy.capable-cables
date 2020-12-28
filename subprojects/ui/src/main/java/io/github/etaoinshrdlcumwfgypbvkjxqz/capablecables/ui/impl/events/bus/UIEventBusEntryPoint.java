package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusProcessor;
import net.minecraftforge.eventbus.api.BusBuilder;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public enum UIEventBusEntryPoint {
	;
	private static final Processor<?, ?> DEFAULT_BUS = EventBusProcessor.of(new BusBuilder().build());
	private static volatile Processor<?, ?> bus = getDefaultBus();

	public static <T> Publisher<T> getBusPublisher() {
		return getBus();
	}

	@SuppressWarnings("unchecked")
	public static <T, R> Processor<T, R> getBus() {
		return (Processor<T, R>) bus; // COMMENT we do not care about the event type
	}

	public static void setBus(Processor<?, ?> bus) {
		UIEventBusEntryPoint.bus = bus;
	}

	public static <T> Subscriber<T> getBusSubscriber() {
		return getBus();
	}

	public static Processor<?, ?> getDefaultBus() {
		return DEFAULT_BUS;
	}
}
