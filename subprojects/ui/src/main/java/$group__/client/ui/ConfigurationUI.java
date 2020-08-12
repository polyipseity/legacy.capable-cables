package $group__.client.ui;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.events.bus.IUIEventBus;
import $group__.client.ui.mvvm.structures.EnumCursor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public enum ConfigurationUI {
	;

	@Nullable
	private static String modId;

	public static String getModId() {
		if (modId == null)
			throw new IllegalStateException("Setup not done");
		return modId;
	}

	public static void setup(String modId) {
		if (ConfigurationUI.modId != null)
			throw new IllegalStateException("Setup already done");
		ConfigurationUI.modId = modId;
		EventBusEntryPoint.INSTANCE.setDelegated(new UIEventBusForge(Bus.FORGE.bus().get()));
	}

	public static void loadComplete() {
		Minecraft.getInstance().getFramebuffer().enableStencil();
		EnumCursor.preload();
	}

	public static class UIEventBusForge
			implements IUIEventBus<Event, Object> {
		protected final IEventBus delegated;

		public UIEventBusForge(IEventBus delegated) { this.delegated = delegated; }

		@Override
		public boolean register(Object target) {
			getDelegated().register(target);
			return true;
		}

		@Override
		public boolean unregister(Object target) {
			getDelegated().unregister(target);
			return true;
		}

		@Override
		public boolean post(Event event) { return getDelegated().post(event); }

		@Override
		public Class<Event> getEventClass() { return Event.class; }

		@Override
		public Class<Object> getTargetClass() { return Object.class; }

		protected IEventBus getDelegated() { return delegated; }
	}
}
