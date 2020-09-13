package $group__.ui;

import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.parsers.adapters.EnumJAXBElementPresetAdapter;
import $group__.ui.parsers.adapters.EnumJAXBObjectPresetAdapter;
import $group__.ui.structures.EnumCursor;
import $group__.utilities.events.EventBusForge;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

	public static void setup(String modID) {
		if (MOD_ID.getAndSet(modID) != null)
			throw new IllegalStateException("Setup already done");
		// COMMENT event bus
		UIEventBusEntryPoint.setEventBus(EventBusForge.FORGE_EVENT_BUS);
		// COMMENT JAXB adapters
		EnumJAXBElementPresetAdapter.initializeClass();
		EnumJAXBObjectPresetAdapter.initializeClass();
	}

	public static void loadComplete() {
		Minecraft.getInstance().getFramebuffer().enableStencil();
		EnumCursor.initializeClass();
	}
}
