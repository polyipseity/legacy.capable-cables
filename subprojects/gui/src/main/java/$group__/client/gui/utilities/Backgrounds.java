package $group__.client.gui.utilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.common.MinecraftForge;

import static net.minecraft.client.gui.AbstractGui.BACKGROUND_LOCATION;

@OnlyIn(Dist.CLIENT)
public enum Backgrounds {
	;

	public static void drawnBackground(Screen screen) { MinecraftForge.EVENT_BUS.post(new BackgroundDrawnEvent(screen)); }

	public static void renderBackground(Screen screen) { renderBackground(screen, 0); }

	/**
	 * @see Screen#renderBackground(int)
	 */
	@SuppressWarnings("MagicNumber")
	public static void renderBackground(Screen screen, int vOffset) {
		if (screen.getMinecraft().world != null) {
			GuiUtilities.fillGradient(0, 0, screen.width, screen.height, screen.getBlitOffset(), 0xc0101010, 0xd0101010);
			drawnBackground(screen);
		} else renderDirtBackground(screen, vOffset);
	}

	/**
	 * @see Screen#renderDirtBackground(int)
	 */
	@SuppressWarnings({"MagicNumber", "deprecation"})
	public static void renderDirtBackground(Screen screen, int vOffset) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		screen.getMinecraft().getTextureManager().bindTexture(BACKGROUND_LOCATION);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buffer.pos(0.0D, screen.height, 0.0D).tex(0.0F, (float) screen.height / 32.0F + (float) vOffset).color(64, 64, 64, 255).endVertex();
		buffer.pos(screen.width, screen.height, 0.0D).tex((float) screen.width / 32.0F, (float) screen.height / 32.0F + (float) vOffset).color(64, 64, 64, 255).endVertex();
		buffer.pos(screen.width, 0.0D, 0.0D).tex((float) screen.width / 32.0F, (float) vOffset).color(64, 64, 64, 255).endVertex();
		buffer.pos(0.0D, 0.0D, 0.0D).tex(0.0F, (float) vOffset).color(64, 64, 64, 255).endVertex();
		tessellator.draw();
		drawnBackground(screen);
	}
}
