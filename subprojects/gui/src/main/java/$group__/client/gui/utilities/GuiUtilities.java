package $group__.client.gui.utilities;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import static $group__.client.gui.utilities.Matrices.*;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum GuiUtilities {
	;

	public static void drawRectangle(Matrix4f matrix, Point2D p1, Point2D p2, int color) {
		hLine(matrix, p1.getX(), p2.getX(), p1.getY(), color);
		vLine(matrix, p1.getX(), p1.getY(), p2.getY(), color);
		hLine(matrix, p1.getX(), p2.getX(), p2.getY(), color);
		vLine(matrix, p2.getX(), p1.getY(), p2.getY(), color);
	}

	/**
	 * @see AbstractGui#hLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void hLine(Matrix4f matrix, double x1, double x2, double y, int color) {
		ScreenUtility.INSTANCE
				.hLine((int) transformX(x1, matrix), (int) transformX(x2, matrix), (int) transformY(y, matrix), color);
	}

	/**
	 * @see AbstractGui#vLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void vLine(Matrix4f matrix, double x, double y1, double y2, int color) {
		ScreenUtility.INSTANCE
				.vLine((int) transformX(x, matrix), (int) transformY(y1, matrix), (int) transformY(y2, matrix), color);
	}

	/**
	 * @see AbstractGui#fill(int, int, int, int, int)
	 */
	@Deprecated
	public static void fill(Point2D p1, Point2D p2, int color) { AbstractGui.fill((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY(), color); }

	/**
	 * @see AbstractGui#fill(Matrix4f, int, int, int, int, int)
	 */
	public static void fill(Matrix4f matrix, Point2D p1, Point2D p2, int color) { AbstractGui.fill(matrix, (int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY(), color); }

	/**
	 * @see AbstractGui#fillGradient(int, int, int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void fillGradient(Matrix4f matrix, Point2D p1, Point2D p2, int p1Color, int p2Color, int blitOffset) {
		Point2D xy1 = (Point2D) p1.clone(),
				xy2 = (Point2D) p2.clone();
		transformPoint(xy1, matrix);
		transformPoint(xy2, matrix);
		ScreenUtility.INSTANCE
				.setBlitOffset_(blitOffset)
				.fillGradient((int) xy1.getX(), (int) xy1.getY(), (int) xy2.getX(), (int) xy2.getY(), p1Color, p2Color);
	}

	/**
	 * @see AbstractGui#drawCenteredString(FontRenderer, String, int, int, int)
	 */
	public static void drawCenteredString(Matrix4f matrix, FontRenderer font, String string, Point2D p, int color) {
		Point2D xy = (Point2D) p.clone();
		transformPoint(xy, matrix);
		ScreenUtility.INSTANCE
				.drawCenteredString(font, string, (int) xy.getX(), (int) xy.getY(), color);
	}

	/**
	 * @see AbstractGui#drawRightAlignedString(FontRenderer, String, int, int, int)
	 */
	public static void drawRightAlignedString(Matrix4f matrix, FontRenderer font, String string, Point2D p, int color) {
		Point2D xy = (Point2D) p.clone();
		transformPoint(xy, matrix);
		ScreenUtility.INSTANCE
				.drawRightAlignedString(font, string, (int) xy.getX(), (int) xy.getY(), color);
	}

	/**
	 * @see AbstractGui#drawString(FontRenderer, String, int, int, int)
	 */
	public static void drawString(Matrix4f matrix, FontRenderer font, String string, Point2D p, int color) {
		Point2D xy = (Point2D) p.clone();
		transformPoint(xy, matrix);
		ScreenUtility.INSTANCE
				.drawString(font, string, (int) xy.getX(), (int) xy.getY(), color);
	}

	/**
	 * @see AbstractGui#blit(int, int, int, int, int, TextureAtlasSprite)
	 */
	public static void blit(Matrix4f matrix, Vector3f position, Dimension2D dimension, TextureAtlasSprite sprite) {
		Vector3f vec = position.copy();
		Dimension2D dim = (Dimension2D) dimension.clone();
		transformVector3f(vec, matrix);
		transformDimension(dim, matrix);
		AbstractGui.blit((int) vec.getX(), (int) vec.getY(), (int) vec.getZ(),
				(int) dim.getWidth(), (int) dim.getHeight(), sprite);
	}

	/**
	 * @see AbstractGui#blit(int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(Matrix4f matrix, Vector3f position, Point2D spritePosition, Dimension2D dimension, Dimension2D textureDimension) {
		Vector3f vec = position.copy();
		Dimension2D dim = (Dimension2D) dimension.clone();
		transformVector3f(vec, matrix);
		transformDimension(dim, matrix);
		AbstractGui.blit((int) vec.getX(), (int) vec.getY(), (int) vec.getZ(),
				(float) spritePosition.getX(), (float) spritePosition.getY(),
				(int) dim.getWidth(), (int) dim.getHeight(),
				(int) textureDimension.getWidth(), (int) textureDimension.getHeight());
	}

	/**
	 * @see AbstractGui#blit(int, int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(Matrix4f matrix, Point2D position, Dimension2D dimension, Point2D spritePosition, Dimension2D spriteDimension, Dimension2D textureDimension) {
		Point2D p = (Point2D) position.clone();
		Dimension2D dim = (Dimension2D) dimension.clone();
		transformPoint(p, matrix);
		transformDimension(dim, matrix);
		AbstractGui.blit((int) p.getX(), (int) p.getY(),
				(int) dim.getWidth(), (int) dim.getHeight(),
				(float) spritePosition.getX(), (float) spritePosition.getY(),
				(int) spriteDimension.getWidth(), (int) spriteDimension.getHeight(),
				(int) textureDimension.getWidth(), (int) textureDimension.getHeight());
	}

	/**
	 * @see AbstractGui#blit(int, int, float, float, int, int, int, int)
	 */
	public static void blit(Matrix4f matrix, Point2D position, Point2D spritePosition, Dimension2D dimension, Dimension2D textureDimension) {
		Point2D p = (Point2D) position.clone();
		Dimension2D dim = (Dimension2D) dimension.clone();
		transformPoint(p, matrix);
		transformDimension(dim, matrix);
		AbstractGui.blit((int) p.getX(), (int) p.getY(),
				(float) spritePosition.getX(), (float) spritePosition.getY(),
				(int) dim.getWidth(), (int) dim.getHeight(),
				(int) textureDimension.getWidth(), (int) textureDimension.getHeight());
	}
}
