package $group__.client.gui.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nullable;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum Backgrounds {
	;

	public static void drawnBackground(Screen screen) { Bus.FORGE.bus().get().post(new BackgroundDrawnEvent(screen)); }

	/**
	 * @see Screen#renderBackground()
	 */
	public static void renderBackground(@Nullable Minecraft client, int width, int height) { renderBackground(client, width, height, 0); }

	/**
	 * @see Screen#renderBackground(int)
	 */
	public static void renderBackground(@Nullable Minecraft client, int width, int height, int blitOffset) {
		ScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground(blitOffset);
	}

	/**
	 * @see Screen#renderDirtBackground(int)
	 */
	public static void renderDirtBackground(@Nullable Minecraft client, int width, int height, int blitOffset) {
		ScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderDirtBackground(blitOffset);
	}
}
