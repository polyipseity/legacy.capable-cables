package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftScreenUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;

@OnlyIn(Dist.CLIENT)
public enum UIMinecraftBackgrounds {
	;

	public static void notifyBackgroundDrawn(Screen screen) {
		UIEventBusEntryPoint.getBusSubscriber().onNext(new GuiScreenEvent.BackgroundDrawnEvent(screen));
	}

	/**
	 * @see Screen#renderBackground()
	 */
	public static void renderDefaultBackgroundAndNotify(@Nullable Minecraft client, int width, int height) {
		MinecraftScreenUtility.getInstance()
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground();
	}

	/**
	 * @see Screen#renderBackground(int)
	 */
	public static void renderDefaultBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		MinecraftScreenUtility.getInstance()
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground(blitOffset);
	}

	/**
	 * @see Screen#renderDirtBackground(int)
	 */
	public static void renderDirtBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		MinecraftScreenUtility.getInstance()
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderDirtBackground(blitOffset);
	}
}
