package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftScreenUtility;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DoubleDimension2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.*;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities.*;

@OnlyIn(Dist.CLIENT)
public enum MinecraftDrawingUtilities {
	;

	public static void drawRectangle(AffineTransform transform, Rectangle2D rectangle, int color, int z) {
		Rectangle2D rF = UIObjectUtilities.floorRectangularShape(rectangle, new Rectangle2D.Double());
		hLine(transform, rF.getX(), rF.getMaxX(), rF.getY(), color, z);
		vLine(transform, rF.getX(), rF.getY(), rF.getMaxY(), color, z);
		hLine(transform, rF.getX(), rF.getMaxX(), rF.getMaxY(), color, z);
		vLine(transform, rF.getMaxX(), rF.getY(), rF.getMaxY(), color, z);
	}

	/**
	 * @see AbstractGui#hLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void hLine(AffineTransform transform, double x1, double x2, double y, int color, int z) { fill(transform, new Rectangle2D.Double(x1, y, Math.abs(x2 - x1), 1), color, z); }

	/**
	 * @see AbstractGui#vLine(int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void vLine(AffineTransform transform, double x, double y1, double y2, int color, int z) { fill(transform, new Rectangle2D.Double(x, y1, 1, Math.abs(y2 - y1)), color, z); }

	/**
	 * @see AbstractGui#fill(Matrix4f, int, int, int, int, int)
	 */
	public static void fill(AffineTransform transform, Rectangle2D rectangle, int color, int z) {
		Rectangle2D rF = UIObjectUtilities.floorRectangularShape(rectangle, new Rectangle2D.Double());
		Matrix4f m = toMatrix(transform);
		if (z != 0) m.translate(new Vector3f(0, 0, z));
		AbstractGui.fill(m, (int) rF.getX(), (int) rF.getY(), (int) rF.getMaxX(), (int) rF.getMaxY(), color);
	}

	/**
	 * @see AbstractGui#fill(int, int, int, int, int)
	 */
	@Deprecated
	public static void fill(Rectangle2D rectangle, int color, int z) { fill(new AffineTransform(), rectangle, color, z); }

	/**
	 * @see AbstractGui#fillGradient(int, int, int, int, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void fillGradient(AffineTransform transform, Rectangle2D rectangle, int colorTop, int colorBottom, int z) {
		Rectangle2D rF = new Rectangle2D.Double();
		UIObjectUtilities.floorRectangularShape(transformRectangularShape(transform, rectangle, rF), rF);
		MinecraftScreenUtility.getInstance()
				.setBlitOffset_(z)
				.fillGradient((int) rF.getX(), (int) rF.getY(), (int) rF.getMaxX(), (int) rF.getMaxY(), colorTop, colorBottom);
	}

	/**
	 * @see AbstractGui#drawCenteredString(FontRenderer, String, int, int, int)
	 */
	public static void drawCenteredString(AffineTransform transform, FontRenderer font, CharSequence string, Point2D p, int color) {
		Point2D xyF = new Point2D.Double();
		UIObjectUtilities.floorPoint(transformPoint(transform, p, xyF), xyF);
		MinecraftScreenUtility.getInstance()
				.drawCenteredString(font, string.toString(), (int) xyF.getX(), (int) xyF.getY(), color);
	}

	/**
	 * @see AbstractGui#drawRightAlignedString(FontRenderer, String, int, int, int)
	 */
	public static void drawRightAlignedString(AffineTransform transform, FontRenderer font, CharSequence string, Point2D p, int color) {
		Point2D xyF = new Point2D.Double();
		UIObjectUtilities.floorPoint(transformPoint(transform, p, xyF), xyF);
		MinecraftScreenUtility.getInstance()
				.drawRightAlignedString(font, string.toString(), (int) xyF.getX(), (int) xyF.getY(), color);
	}

	/**
	 * @see AbstractGui#drawString(FontRenderer, String, int, int, int)
	 */
	public static void drawString(AffineTransform transform, FontRenderer font, CharSequence string, Point2D p, int color) {
		Point2D xyF = new Point2D.Double();
		UIObjectUtilities.floorPoint(transformPoint(transform, p, xyF), xyF);
		MinecraftScreenUtility.getInstance()
				.drawString(font, string.toString(), (int) xyF.getX(), (int) xyF.getY(), color);
	}

	/**
	 * @see AbstractGui#blit(int, int, int, int, int, TextureAtlasSprite)
	 */
	public static void blit(AffineTransform transform, Rectangle2D gui, TextureAtlasSprite sprite, double z) {
		Rectangle2D gF = new Rectangle2D.Double();
		UIObjectUtilities.floorRectangularShape(transformRectangularShape(transform, gui, gF), gF);
		AbstractGui.blit((int) gF.getX(), (int) gF.getY(), (int) Math.floor(z),
				(int) gF.getWidth(), (int) gF.getHeight(), sprite);
	}

	/**
	 * @see AbstractGui#blit(int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(AffineTransform transform, Rectangle2D gui, Point2D sprite, Dimension2D texture, double z) {
		Rectangle2D gF = new Rectangle2D.Double();
		UIObjectUtilities.floorRectangularShape(transformRectangularShape(transform, gui, gF), gF);
		Dimension2D textureF = UIObjectUtilities.floorDimension(texture, new DoubleDimension2D());
		AbstractGui.blit((int) gF.getX(), (int) gF.getY(), (int) z,
				(float) sprite.getX(), (float) sprite.getY(),
				(int) gF.getWidth(), (int) gF.getHeight(),
				(int) textureF.getWidth(), (int) textureF.getHeight());
	}

	/**
	 * @see AbstractGui#blit(int, int, int, int, float, float, int, int, int, int)
	 */
	public static void blit(AffineTransform transform, Rectangle2D gui, Rectangle2D sprite, Dimension2D texture) {
		Rectangle2D gF = new Rectangle2D.Double();
		UIObjectUtilities.floorRectangularShape(transformRectangularShape(transform, gui, gF), gF);
		Rectangle2D spriteF = UIObjectUtilities.floorRectangularShape(sprite, new Rectangle2D.Double());
		Dimension2D textureF = UIObjectUtilities.floorDimension(texture, new DoubleDimension2D());
		AbstractGui.blit((int) gF.getX(), (int) gF.getY(),
				(int) gF.getWidth(), (int) gF.getHeight(),
				(float) sprite.getX(), (float) sprite.getY(),
				(int) spriteF.getWidth(), (int) spriteF.getHeight(),
				(int) textureF.getWidth(), (int) textureF.getHeight());
	}

	/**
	 * @see AbstractGui#blit(int, int, float, float, int, int, int, int)
	 */
	public static void blit(AffineTransform transform, Rectangle2D gui, Point2D sprite, Dimension2D texture) {
		Rectangle2D gF = new Rectangle2D.Double();
		UIObjectUtilities.floorRectangularShape(transformRectangularShape(transform, gui, gF), gF);
		Dimension2D textureF = UIObjectUtilities.floorDimension(texture, new DoubleDimension2D());
		AbstractGui.blit((int) gF.getX(), (int) gF.getY(),
				(float) sprite.getX(), (float) sprite.getY(),
				(int) gF.getWidth(), (int) gF.getHeight(),
				(int) textureF.getWidth(), (int) textureF.getHeight());
	}

	public static void drawShape(Shape shape, boolean filled, Color color, float z) { drawShape(null, shape, filled, color, z); }

	public static void drawShape(@Nullable AffineTransform transform, Shape shape, boolean filled, Color color, float z) {
		PathIterator path = shape.getPathIterator(transform, 1);
		Point2D start = new Point2D.Double(), current = new Point2D.Double();
		boolean started = false;
		int r = color.getRed(), g = color.getGreen(), b = color.getBlue(), a = color.getAlpha();

		// TODO: Wait for a better system for such
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		// TODO: concave and self-intersecting polygons
		buffer.begin(filled ? GL11.GL_POLYGON : GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		double[] segment = new double[6];
		while (!path.isDone()) {
			switch (path.currentSegment(segment)) {
				case PathIterator.SEG_MOVETO:
					start.setLocation(segment[0], segment[1]);
					current.setLocation(start);
					started = true;
					break;
				case PathIterator.SEG_LINETO:
					if (!started)
						throw new InternalError();
					buffer.pos(current.getX(), current.getY(), z).color(r, g, b, a).endVertex();
					buffer.pos(segment[0], segment[1], z).color(r, g, b, a).endVertex();
					current.setLocation(segment[0], segment[1]);
					break;
				case PathIterator.SEG_CLOSE:
					if (!started)
						throw new InternalError();
					buffer.pos(current.getX(), current.getY(), z).color(r, g, b, a).endVertex();
					buffer.pos(start.getX(), start.getY(), z).color(r, g, b, a).endVertex();
					current.setLocation(start);
					break;
				default:
					throw new InternalError();
			}
			path.next();
		}
		tessellator.draw();

		RenderSystem.shadeModel(GL11.GL_SMOOTH);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}
}
