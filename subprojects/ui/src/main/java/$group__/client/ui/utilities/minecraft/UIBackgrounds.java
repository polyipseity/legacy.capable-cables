package $group__.client.ui.utilities.minecraft;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public enum UIBackgrounds {
	;

	public static void notifyBackgroundDrawn(Screen screen) { EventBusEntryPoint.getEventBus().onNext(new GuiScreenEvent.BackgroundDrawnEvent(screen)); }

	/**
	 * @see Screen#renderBackground()
	 */
	public static void renderBackgroundAndNotify(@Nullable Minecraft client, int width, int height) {
		UIScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground();
	}

	/**
	 * @see Screen#renderBackground(int)
	 */
	public static void renderBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		UIScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground(blitOffset);
	}

	/**
	 * @see Screen#renderDirtBackground(int)
	 */
	public static void renderDirtBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		UIScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderDirtBackground(blitOffset);
	}
}
