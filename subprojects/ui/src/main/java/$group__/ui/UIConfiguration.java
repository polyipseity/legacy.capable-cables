package $group__.ui;

import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.parsers.adapters.EnumJAXBElementPresetAdapter;
import $group__.ui.parsers.adapters.EnumJAXBObjectPresetAdapter;
import $group__.ui.structures.EnumCursor;
import $group__.utilities.events.EventBusForge;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.GenericEvent;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@OnlyIn(Dist.CLIENT)
public enum UIConfiguration {
	;

	private static final AtomicReference<String> MOD_ID = new AtomicReference<>();
	// TODO centralized logger

	public static String getModId() {
		return Optional.ofNullable(MOD_ID.get())
				.orElseThrow(() -> new IllegalStateException("Setup not done"));
	}

	public static void setup(String modId) {
		if (MOD_ID.getAndSet(modId) != null)
			throw new IllegalStateException("Setup already done");
		// COMMENT event bus
		{
			UIEventBusEntryPoint.setEventBus(EventBusForge.FORGE_EVENT_BUS);
			DisposableObserver<EventSanityCheck<Event>> d = new DisposableObserverAuto<EventSanityCheck<Event>>() {
				@Override
				public void onNext(@NonNull EventSanityCheck<Event> event) { /* TODO write something here */ }
			};
			// COMMENT sanity checks
			UIEventBusEntryPoint.<EventSanityCheck<Event>>getEventBus().subscribe(d);
			UIEventBusEntryPoint.getEventBus().onNext(new EventSanityCheck<>(Event.class));
			d.dispose();
		}
		// COMMENT JAXB adapters
		EnumJAXBElementPresetAdapter.initializeClass();
		EnumJAXBObjectPresetAdapter.initializeClass();
	}

	public static void loadComplete() {
		Minecraft.getInstance().getFramebuffer().enableStencil();
		EnumCursor.initializeClass();
	}

	@OnlyIn(Dist.CLIENT)
	private static final class EventSanityCheck<T>
			extends GenericEvent<T> {
		private EventSanityCheck(Class<T> clazz) { super(clazz); }
	}
}
