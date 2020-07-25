package $group__.client.gui.utilities;

import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static $group__.client.gui.utilities.Transforms.AffineTransforms.*;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum GuiUtilities {
	;

	@OnlyIn(CLIENT)
	public enum DrawingUtilities {
		;

		public static void drawRectangle(AffineTransform transform, Rectangle2D rectangle, int color) {
			hLine(transform, rectangle.getX(), rectangle.getMaxX(), rectangle.getY(), color);
			vLine(transform, rectangle.getX(), rectangle.getY(), rectangle.getMaxY(), color);
			hLine(transform, rectangle.getX(), rectangle.getMaxX(), rectangle.getMaxY(), color);
			vLine(transform, rectangle.getMaxX(), rectangle.getY(), rectangle.getMaxY(), color);
		}

		/**
		 * @see AbstractGui#hLine(int, int, int, int)
		 */
		@SuppressWarnings("JavadocReference")
		public static void hLine(AffineTransform transform, double x1, double x2, double y, int color) {
			GuiScreenUtility.INSTANCE
					.hLine((int) transformX(x1, transform), (int) transformX(x2, transform), (int) transformY(y, transform), color);
		}

		/**
		 * @see AbstractGui#vLine(int, int, int, int)
		 */
		@SuppressWarnings("JavadocReference")
		public static void vLine(AffineTransform transform, double x, double y1, double y2, int color) {
			GuiScreenUtility.INSTANCE
					.vLine((int) transformX(x, transform), (int) transformY(y1, transform), (int) transformY(y2, transform), color);
		}

		/**
		 * @see AbstractGui#fill(int, int, int, int, int)
		 */
		@Deprecated
		public static void fill(Rectangle2D rectangle, int color) { AbstractGui.fill((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getMaxX(), (int) rectangle.getMaxY(), color); }

		/**
		 * @see AbstractGui#fill(Matrix4f, int, int, int, int, int)
		 */
		public static void fill(AffineTransform transform, Rectangle2D rectangle, int color) { AbstractGui.fill(toMatrix(transform), (int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getMaxX(), (int) rectangle.getMaxY(), color); }

		/**
		 * @see AbstractGui#fillGradient(int, int, int, int, int, int)
		 */
		@SuppressWarnings("JavadocReference")
		public static void fillGradient(AffineTransform transform, Rectangle2D rectangle, int colorTop, int colorBottom, int blitOffset) {
			Rectangle2D r = (Rectangle2D) rectangle.clone();
			transformRectangle(rectangle, transform);
			GuiScreenUtility.INSTANCE
					.setBlitOffset_(blitOffset)
					.fillGradient((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getMaxX(), (int) rectangle.getMaxY(), colorTop, colorBottom);
		}

		/**
		 * @see AbstractGui#drawCenteredString(FontRenderer, String, int, int, int)
		 */
		public static void drawCenteredString(AffineTransform transform, FontRenderer font, String string, Point2D p, int color) {
			Point2D xy = (Point2D) p.clone();
			transformPoint(xy, transform);
			GuiScreenUtility.INSTANCE
					.drawCenteredString(font, string, (int) xy.getX(), (int) xy.getY(), color);
		}

		/**
		 * @see AbstractGui#drawRightAlignedString(FontRenderer, String, int, int, int)
		 */
		public static void drawRightAlignedString(AffineTransform transform, FontRenderer font, String string, Point2D p, int color) {
			Point2D xy = (Point2D) p.clone();
			transformPoint(xy, transform);
			GuiScreenUtility.INSTANCE
					.drawRightAlignedString(font, string, (int) xy.getX(), (int) xy.getY(), color);
		}

		/**
		 * @see AbstractGui#drawString(FontRenderer, String, int, int, int)
		 */
		public static void drawString(AffineTransform transform, FontRenderer font, String string, Point2D p, int color) {
			Point2D xy = (Point2D) p.clone();
			transformPoint(xy, transform);
			GuiScreenUtility.INSTANCE
					.drawString(font, string, (int) xy.getX(), (int) xy.getY(), color);
		}

		/**
		 * @see AbstractGui#blit(int, int, int, int, int, TextureAtlasSprite)
		 */
		public static void blit(AffineTransform transform, Rectangle2D gui, TextureAtlasSprite sprite, double z) {
			Rectangle2D g = (Rectangle2D) gui.clone();
			transformRectangle(g, transform);
			AbstractGui.blit((int) g.getX(), (int) g.getY(), (int) z,
					(int) g.getWidth(), (int) g.getHeight(), sprite);
		}

		/**
		 * @see AbstractGui#blit(int, int, int, float, float, int, int, int, int)
		 */
		public static void blit(AffineTransform transform, Rectangle2D gui, Point2D sprite, Dimension2D texture, double z) {
			Rectangle2D g = (Rectangle2D) gui.clone();
			transformRectangle(g, transform);
			AbstractGui.blit((int) g.getX(), (int) g.getY(), (int) z,
					(float) sprite.getX(), (float) sprite.getY(),
					(int) g.getWidth(), (int) g.getHeight(),
					(int) texture.getWidth(), (int) texture.getHeight());
		}

		/**
		 * @see AbstractGui#blit(int, int, int, int, float, float, int, int, int, int)
		 */
		public static void blit(AffineTransform transform, Rectangle2D gui, Rectangle2D sprite, Dimension2D texture) {
			Rectangle2D g = (Rectangle2D) gui.clone();
			transformRectangle(g, transform);
			AbstractGui.blit((int) g.getX(), (int) g.getY(),
					(int) g.getWidth(), (int) g.getHeight(),
					(float) sprite.getX(), (float) sprite.getY(),
					(int) sprite.getWidth(), (int) sprite.getHeight(),
					(int) texture.getWidth(), (int) texture.getHeight());
		}

		/**
		 * @see AbstractGui#blit(int, int, float, float, int, int, int, int)
		 */
		public static void blit(AffineTransform transform, Rectangle2D gui, Point2D sprite, Dimension2D texture) {
			Rectangle2D g = (Rectangle2D) gui.clone();
			transformRectangle(g, transform);
			AbstractGui.blit((int) g.getX(), (int) g.getY(),
					(float) sprite.getX(), (float) sprite.getY(),
					(int) g.getWidth(), (int) g.getHeight(),
					(int) texture.getWidth(), (int) texture.getHeight());
		}
	}

	@OnlyIn(CLIENT)
	public enum ObjectUtilities {
		;

		public static Rectangle2D getRectangleFromDiagonal(Point2D p1, Point2D p2) {
			Rectangle2D ret = new Rectangle2D.Double();
			ret.setFrameFromDiagonal(p1, p2);
			return ret;
		}

		public static void drawShape(Shape shape, boolean filled, Color color, float z) {
			PathIterator path = shape.getPathIterator(null, 1);
			@Nullable Point2D start = null, current = null;
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
			while (!path.isDone()) {
				double[] segment = new double[6];
				switch (path.currentSegment(segment)) {
					case PathIterator.SEG_MOVETO:
						start = new Point2D.Double(segment[0], segment[1]);
						current = start;
						break;
					case PathIterator.SEG_LINETO:
						if (current == null) throw BecauseOf.unexpected();
						buffer.pos(current.getX(), current.getY(), z).color(r, g, b, a).endVertex();
						buffer.pos(segment[0], segment[1], z).color(r, g, b, a).endVertex();
						current = new Point2D.Double(segment[0], segment[1]);
						break;
					case PathIterator.SEG_CLOSE:
						if (current == null) throw BecauseOf.unexpected();
						buffer.pos(current.getX(), current.getY(), z).color(r, g, b, a).endVertex();
						buffer.pos(start.getX(), start.getY(), z).color(r, g, b, a).endVertex();
						current = start;
						break;
					default:
						throw BecauseOf.unexpected();
				}
				path.next();
			}
			tessellator.draw();

			// TODO: Wait for a better system for such
			RenderSystem.shadeModel(GL11.GL_SMOOTH);
			RenderSystem.disableBlend();
			RenderSystem.enableAlphaTest();
			RenderSystem.enableTexture();
		}
	}

	@OnlyIn(CLIENT)
	public enum UnboxingUtilities {
		;

		public static <T> T applyPoint(Point2D point, BiFunction<Double, Double, T> user) { return user.apply(point.getX(), point.getY()); }

		public static <T> void acceptPoint(Point2D point, BiConsumer<Double, Double> user) { user.accept(point.getX(), point.getY()); }

		public static <T> T applyDimension(Dimension2D dimension, BiFunction<Double, Double, T> user) { return user.apply(dimension.getWidth(), dimension.getHeight()); }

		public static <T> void acceptDimension(Dimension2D dimension, BiConsumer<Double, Double> user) { user.accept(dimension.getWidth(), dimension.getHeight()); }

		public static <T> T applyRectangle(Rectangle2D rectangle, BiFunction<Double, Double, BiFunction<Double, Double, T>> user) { return user.apply(rectangle.getX(), rectangle.getY()).apply(rectangle.getWidth(), rectangle.getHeight()); }

		public static <T> void acceptRectangle(Rectangle2D rectangle, BiFunction<Double, Double, BiConsumer<Double, Double>> user) { user.apply(rectangle.getX(), rectangle.getY()).accept(rectangle.getWidth(), rectangle.getHeight()); }
	}

	@OnlyIn(CLIENT)
	public enum GuiBackgrounds {
		;

		public static void drawnBackground(Screen screen) { Mod.EventBusSubscriber.Bus.FORGE.bus().get().post(new GuiScreenEvent.BackgroundDrawnEvent(screen)); }

		/**
		 * @see Screen#renderBackground()
		 */
		public static void renderBackground(@Nullable Minecraft client, int width, int height) {
			GuiScreenUtility.INSTANCE
					.setClient_(client)
					.setWidth_(width)
					.setHeight_(height)
					.renderBackground();
		}

		/**
		 * @see Screen#renderBackground(int)
		 */
		public static void renderBackground(@Nullable Minecraft client, int width, int height, int blitOffset) {
			GuiScreenUtility.INSTANCE
					.setClient_(client)
					.setWidth_(width)
					.setHeight_(height)
					.renderBackground(blitOffset);
		}

		/**
		 * @see Screen#renderDirtBackground(int)
		 */
		public static void renderDirtBackground(@Nullable Minecraft client, int width, int height, int blitOffset) {
			GuiScreenUtility.INSTANCE
					.setClient_(client)
					.setWidth_(width)
					.setHeight_(height)
					.renderDirtBackground(blitOffset);
		}
	}
}
