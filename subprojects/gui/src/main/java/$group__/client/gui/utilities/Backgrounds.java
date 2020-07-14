package $group__.client.gui.utilities;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.common.MinecraftForge;

public enum Backgrounds {
	;

	public static void drawnBackground(Screen screen) { MinecraftForge.EVENT_BUS.post(new BackgroundDrawnEvent(screen)); }

	public static void renderBackground(Screen screen, int z) { renderBackground(screen, z, 0); }

	@SuppressWarnings("MagicNumber")
	public static void renderBackground(Screen screen, int z, int vOffset) {
		if (screen.getMinecraft().world != null) {
			GUIs.fillGradient(0, 0, screen.width, screen.height, z, 0xc0101010, 0xd0101010);
			drawnBackground(screen);
		} else renderDirtBackground(screen, vOffset);
	}

	@SuppressWarnings("MagicNumber")
	public static void renderDirtBackground(Screen screen, int vOffset) {
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		screen.getMinecraft().getTextureManager().bindTexture(AbstractGui.BACKGROUND_LOCATION);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buffer.pos(0.0D, screen.height, 0.0D).tex(0.0D, (float) screen.height / 32.0F + (float) vOffset).color(64, 64, 64, 255).endVertex();
		buffer.pos(screen.width, screen.height, 0.0D).tex((float) screen.width / 32.0F, (float) screen.height / 32.0F + (float) vOffset).color(64, 64, 64, 255).endVertex();
		buffer.pos(screen.width, 0.0D, 0.0D).tex((float) screen.width / 32.0F, vOffset).color(64, 64, 64, 255).endVertex();
		buffer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, vOffset).color(64, 64, 64, 255).endVertex();
		tessellator.draw();
		drawnBackground(screen);
	}
}
