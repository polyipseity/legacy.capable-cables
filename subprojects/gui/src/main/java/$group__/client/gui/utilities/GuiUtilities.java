package $group__.client.gui.utilities;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum GuiUtilities {
	;

	/**
	 * @see AbstractGui#hLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void hLine(int x1, int x2, int y, int color) {
		ScreenUtility.INSTANCE
				.hLine(x1, x2, y, color);
	}

	/**
	 * @see AbstractGui#vLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void vLine(int x, int y1, int y2, int color) {
		ScreenUtility.INSTANCE
				.vLine(x, y1, y2, color);
	}

	/**
	 * @see AbstractGui#fill(int, int, int, int, int)
	 */
	public static void fill(int x1, int y1, int x2, int y2, int color) { AbstractGui.fill(x1, y1, x2, y2, color); }

	/**
	 * @see AbstractGui#fill(Matrix4f, int, int, int, int, int)
	 */
	public static void fill(Matrix4f matrix, int x1, int y1, int x2, int y2, int color) { AbstractGui.fill(matrix, x1, y1, x2, y2, color); }

	/**
	 * @see AbstractGui#fillGradient(int, int, int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void fillGradient(int x1, int y1, int x2, int y2, int colorY1, int colorY2, int blitOffset) {
		ScreenUtility.INSTANCE
				.setBlitOffset_(blitOffset)
				.fillGradient(x1, y1, x2, y2, colorY1, colorY2);
	}

	/**
	 * @see AbstractGui#drawCenteredString(FontRenderer, String, int, int, int)
	 */
	public static void drawCenteredString(FontRenderer font, String string, int x, int y, int color) {
		ScreenUtility.INSTANCE
				.drawCenteredString(font, string, x, y, color);
	}

	/**
	 * @see AbstractGui#drawRightAlignedString(FontRenderer, String, int, int, int)
	 */
	@SuppressWarnings("SuspiciousNameCombination")
	public static void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) {
		ScreenUtility.INSTANCE
				.drawRightAlignedString(font, string, x, y, color);
	}

	/**
	 * @see AbstractGui#drawString(FontRenderer, String, int, int, int)
	 */
	public static void drawString(FontRenderer font, String string, int x, int y, int color) {
		ScreenUtility.INSTANCE
				.drawString(font, string, x, y, color);
	}

	/**
	 * @see AbstractGui#blit(int, int, int, int, int, TextureAtlasSprite)
	 */
	public static void blit(int x, int y, int z, int xLength, int yLength, TextureAtlasSprite sprite) { AbstractGui.blit(x, y, z, xLength, yLength, sprite); }

	/**
	 * @see AbstractGui#blit(int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(int x, int y, int blitOffset, float u, float v, int xSize, int ySize, int textureWidth, int textureHeight) { AbstractGui.blit(x, y, blitOffset, u, v, xSize, ySize, textureWidth, textureHeight); }

	/**
	 * @see AbstractGui#blit(int, int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(int x, int y, int xSize, int ySize, float u, float v, int uSize, int vSize, int textureWidth, int textureHeight) { AbstractGui.blit(x, y, xSize, ySize, uSize, vSize, textureWidth, textureHeight); }

	/**
	 * @see AbstractGui#blit(int, int, float, float, int, int, int, int)
	 */
	public static void blit(int x, int y, float u, float v, int xLength, int yLength, int uSize, int vSize) { AbstractGui.blit(x, y, u, v, xLength, yLength, uSize, vSize); }
}
