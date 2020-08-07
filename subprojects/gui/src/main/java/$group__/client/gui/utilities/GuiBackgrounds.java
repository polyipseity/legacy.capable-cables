package $group__.client.gui.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public enum GuiBackgrounds {
	;

	public static void notifyBackgroundDrawn(Screen screen) { Mod.EventBusSubscriber.Bus.FORGE.bus().get().post(new GuiScreenEvent.BackgroundDrawnEvent(screen)); }

	/**
	 * @see Screen#renderBackground()
	 */
	public static void renderBackgroundAndNotify(@Nullable Minecraft client, int width, int height) {
		GuiScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground();
	}

	/**
	 * @see Screen#renderBackground(int)
	 */
	public static void renderBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		GuiScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground(blitOffset);
	}

	/**
	 * @see Screen#renderDirtBackground(int)
	 */
	public static void renderDirtBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		GuiScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderDirtBackground(blitOffset);
	}
}
