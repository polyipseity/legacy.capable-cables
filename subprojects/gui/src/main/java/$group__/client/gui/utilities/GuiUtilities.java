package $group__.client.gui.utilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static $group__.utilities.helpers.specific.Colors.*;
import static org.lwjgl.opengl.GL11.*;

@OnlyIn(Dist.CLIENT)
public enum GuiUtilities {
	;

	/**
	 * @see AbstractGui#hLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void hLine(int x1, int x2, int y, int color) {
		if (x2 < x1) {
			int i = x1;
			x1 = x2;
			x2 = i;
		}

		fill(x1, y, x2 + 1, y + 1, color);
	}

	/**
	 * @see AbstractGui#vLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void vLine(int x, int y1, int y2, int color) {
		if (y2 < y1) {
			int i = y1;
			y1 = y2;
			y2 = i;
		}

		fill(x, y1 + 1, x + 1, y2, color);
	}

	/**
	 * @see AbstractGui#fill(int, int, int, int, int)
	 */
	public static void fill(int x1, int y1, int x2, int y2, int color) { fill(TransformationMatrix.identity().getMatrix(), x1, y1, x2, y2, color); }

	/**
	 * @see AbstractGui#fill(Matrix4f, int, int, int, int, int)
	 */
	public static void fill(Matrix4f matrix, int x1, int y1, int x2, int y2, int color) {
		if (x1 < x2) {
			int i = x1;
			x1 = x2;
			x2 = i;
		}

		if (y1 < y2) {
			int j = y1;
			y1 = y2;
			y2 = j;
		}

		float a = (float) (color >> S_RGB_ALPHA_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float r = (float) (color >> S_RGB_RED_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float g = (float) (color >> S_RGB_GREEN_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float b = (float) (color >> S_RGB_BLUE_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(matrix, x1, y2, 0.0F).color(r, g, b, a).endVertex();
		buffer.pos(matrix, x2, y2, 0.0F).color(r, g, b, a).endVertex();
		buffer.pos(matrix, x2, y1, 0.0F).color(r, g, b, a).endVertex();
		buffer.pos(matrix, x1, y1, 0.0F).color(r, g, b, a).endVertex();
		buffer.finishDrawing();
		WorldVertexBufferUploader.draw(buffer);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

	/**
	 * @see AbstractGui#fillGradient(int, int, int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void fillGradient(int x1, int y1, int x2, int y2, int z, int colorY1, int colorY2) {
		float aY1 = (float) (colorY1 >> S_RGB_ALPHA_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float rY1 = (float) (colorY1 >> S_RGB_RED_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float gY1 = (float) (colorY1 >> S_RGB_GREEN_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float bY1 = (float) (colorY1 >> S_RGB_BLUE_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float aY2 = (float) (colorY2 >> S_RGB_ALPHA_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float rY2 = (float) (colorY2 >> S_RGB_RED_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float gY2 = (float) (colorY2 >> S_RGB_GREEN_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		float bY2 = (float) (colorY2 >> S_RGB_BLUE_LSB_BIT & S_RGB_COMPONENT_MAX) / S_RGB_COMPONENT_MAX;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(GL_SMOOTH);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(x2, y1, z).color(rY1, gY1, bY1, aY1).endVertex();
		buffer.pos(x1, y1, z).color(rY1, gY1, bY1, aY1).endVertex();
		buffer.pos(x1, y2, z).color(rY2, gY2, bY2, aY2).endVertex();
		buffer.pos(x2, y2, z).color(rY2, gY2, bY2, aY2).endVertex();
		tessellator.draw();
		RenderSystem.shadeModel(GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	/**
	 * @see AbstractGui#drawCenteredString(FontRenderer, String, int, int, int)
	 */
	public static void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { font.drawStringWithShadow(string, (float) (x - font.getStringWidth(string) / 2), (float) y, color); }

	/**
	 * @see AbstractGui#drawRightAlignedString(FontRenderer, String, int, int, int)
	 */
	public static void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { font.drawStringWithShadow(string, (float) (x - font.getStringWidth(string)), (float) y, color); }

	/**
	 * @see AbstractGui#drawString(FontRenderer, String, int, int, int)
	 */
	public static void drawString(FontRenderer font, String string, int x, int y, int color) { font.drawStringWithShadow(string, (float) x, (float) y, color); }

	/**
	 * @see AbstractGui#blit(int, int, int, int, int, TextureAtlasSprite)
	 */
	public static void blit(int x, int y, int z, int xLength, int yLength, TextureAtlasSprite sprite) { innerBlit(x, x + xLength, y, y + yLength, z, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV()); }

	/**
	 * @see AbstractGui#blit(int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(int x, int y, int z, float u, float v, int xLength, int yLength, int uSize, int vSize) { innerBlit(x, x + xLength, y, y + yLength, z, xLength, yLength, u, v, uSize, vSize); }

	/**
	 * @see AbstractGui#blit(int, int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(int x, int y, int xLength, int yLength, float u, float v, int uLength, int vLength, int uSize, int vSize) { innerBlit(x, x + xLength, y, y + yLength, 0, uLength, vLength, u, v, uSize, vSize); }

	/**
	 * @see AbstractGui#blit(int, int, float, float, int, int, int, int)
	 */
	public static void blit(int x, int y, float u, float v, int xLength, int yLength, int uSize, int vSize) { blit(x, y, xLength, yLength, u, v, xLength, yLength, uSize, vSize); }

	/**
	 * @see AbstractGui#innerBlit(int, int, int, int, int, int, int, float, float, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	private static void innerBlit(int x1, int x2, int y1, int y2, int z, int uLength, int vLength, float u, float v, int uSize, int vSize) { innerBlit(x1, x2, y1, y2, z, (u + 0.0F) / (float) uSize, (u + (float) uLength) / (float) uSize, (v + 0.0F) / (float) vSize, (v + (float) vLength) / (float) vSize); }

	/**
	 * @see AbstractGui#innerBlit(int, int, int, int, int, float, float, float, float)
	 */
	@SuppressWarnings("JavadocReference")
	private static void innerBlit(int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x1, y2, z).tex(u1, v2).endVertex();
		buffer.pos(x2, y2, z).tex(u2, v2).endVertex();
		buffer.pos(x2, y1, z).tex(u2, v1).endVertex();
		buffer.pos(x1, y1, z).tex(u1, v1).endVertex();
		buffer.finishDrawing();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(buffer);
	}
}
