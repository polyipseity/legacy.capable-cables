package $group__.client.ui;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.events.bus.EventBusForge;
import $group__.client.ui.structures.EnumCursor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
		EventBusEntryPoint.setEventBus(new EventBusForge(Bus.FORGE.bus().get()));
	}

	public static void loadComplete() {
		Minecraft.getInstance().getFramebuffer().enableStencil();
		EnumCursor.preload();
	}
}
