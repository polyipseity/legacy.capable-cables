package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import org.jetbrains.annotations.NonNls;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import java.util.function.Function;

public class DelegatingGraphics2D
		extends Graphics2D {
	private static final @NonNls Map<String, Function<@Nonnull DelegatingGraphics2D, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull DelegatingGraphics2D, @Nullable ?>>builder()
					.put("delegate", DelegatingGraphics2D::getDelegate)
					.build();
	private final Graphics2D delegate;

	public DelegatingGraphics2D(Graphics2D delegate) {
		this.delegate = delegate;
	}

	@Override
	public void draw3DRect(int x, int y, int width, int height, boolean raised) {getDelegate().draw3DRect(x, y, width, height, raised);}

	@Override
	public void fill3DRect(int x, int y, int width, int height, boolean raised) {getDelegate().fill3DRect(x, y, width, height, raised);}

	@Override
	public void draw(Shape s) {getDelegate().draw(s);}

	@Override
	public boolean drawImage(Image img, AffineTransform transform, ImageObserver obs) {return getDelegate().drawImage(img, transform, obs);}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {getDelegate().drawImage(img, op, x, y);}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform transform) {getDelegate().drawRenderedImage(img, transform);}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform transform) {getDelegate().drawRenderableImage(img, transform);}

	@Override
	public void drawString(String str, int x, int y) {getDelegate().drawString(str, x, y);}

	@Override
	public void drawString(String str, float x, float y) {getDelegate().drawString(str, x, y);}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {getDelegate().drawString(iterator, x, y);}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {getDelegate().drawString(iterator, x, y);}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {getDelegate().drawGlyphVector(g, x, y);}

	@Override
	public void fill(Shape s) {getDelegate().fill(s);}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {return getDelegate().hit(rect, s, onStroke);}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {return getDelegate().getDeviceConfiguration();}

	protected Graphics2D getDelegate() {
		return delegate;
	}

	@Override
	public Graphics create() {return getDelegate().create();}

	@Override
	public Color getColor() {return getDelegate().getColor();}

	@Override
	public void setComposite(@Nullable Composite comp) {getDelegate().setComposite(comp);}

	@Override
	public Graphics create(int x, int y, int width, int height) {return getDelegate().create(x, y, width, height);}

	@Override
	public void setPaint(@Nullable Paint paint) {getDelegate().setPaint(paint);}

	@Override
	public void setPaintMode() {getDelegate().setPaintMode();}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color backgroundColor, ImageObserver observer) {return getDelegate().drawImage(img, x, y, width, height, backgroundColor, observer);}

	@Override
	public void setStroke(@Nullable Stroke s) {getDelegate().setStroke(s);}

	@Override
	public void setXORMode(Color c1) {getDelegate().setXORMode(c1);}

	@Override
	public Font getFont() {return getDelegate().getFont();}

	@Override
	public void setFont(Font font) {getDelegate().setFont(font);}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color backgroundColor, ImageObserver observer) {return getDelegate().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, backgroundColor, observer);}

	@Override
	public void clip(@Nullable Shape s) {getDelegate().clip(s);}

	@Override
	public FontMetrics getFontMetrics() {return getDelegate().getFontMetrics();}

	@Override
	public FontMetrics getFontMetrics(Font f) {return getDelegate().getFontMetrics(f);}

	@Override
	public Rectangle getClipBounds() {return getDelegate().getClipBounds();}

	@SuppressWarnings({"MethodDoesntCallSuperMethod", "FinalizeDeclaration"})
	@Override
	public void finalize() {getDelegate().finalize();}

	@Override
	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {getDelegate().setRenderingHint(hintKey, hintValue);}

	@Override
	public void clipRect(int x, int y, int width, int height) {getDelegate().clipRect(x, y, width, height);}

	@Override
	public void setClip(int x, int y, int width, int height) {getDelegate().setClip(x, y, width, height);}

	@Override
	public Shape getClip() {return getDelegate().getClip();}

	@Override
	public boolean hitClip(int x, int y, int width, int height) {return getDelegate().hitClip(x, y, width, height);}

	@Override
	public Object getRenderingHint(RenderingHints.Key hintKey) {return getDelegate().getRenderingHint(hintKey);}

	@Override
	public void setColor(@Nullable Color c) {getDelegate().setColor(c);}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {getDelegate().copyArea(x, y, width, height, dx, dy);}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {getDelegate().drawLine(x1, y1, x2, y2);}

	@Override
	public Rectangle getClipBounds(Rectangle r) {return getDelegate().getClipBounds(r);}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {getDelegate().setRenderingHints(hints);}

	@Override
	public void fillRect(int x, int y, int width, int height) {getDelegate().fillRect(x, y, width, height);}

	@Override
	public void drawRect(int x, int y, int width, int height) {getDelegate().drawRect(x, y, width, height);}

	@Override
	public void clearRect(int x, int y, int width, int height) {getDelegate().clearRect(x, y, width, height);}

	@Override
	public void setClip(@Nullable Shape clip) {getDelegate().setClip(clip);}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {getDelegate().addRenderingHints(hints);}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {getDelegate().drawRoundRect(x, y, width, height, arcWidth, arcHeight);}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {getDelegate().fillRoundRect(x, y, width, height, arcWidth, arcHeight);}

	@Override
	public void drawOval(int x, int y, int width, int height) {getDelegate().drawOval(x, y, width, height);}

	public static @Immutable @NonNls Map<String, Function<@Nonnull DelegatingGraphics2D, @Nullable ?>> getObjectVariableMapView() { return ImmutableMap.copyOf(getObjectVariableMap()); }

	@Override
	public RenderingHints getRenderingHints() {return getDelegate().getRenderingHints();}

	@Override
	public void fillOval(int x, int y, int width, int height) {getDelegate().fillOval(x, y, width, height);}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {getDelegate().drawArc(x, y, width, height, startAngle, arcAngle);}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {getDelegate().fillArc(x, y, width, height, startAngle, arcAngle);}

	@Override
	public void translate(int x, int y) {getDelegate().translate(x, y);}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {getDelegate().drawPolyline(xPoints, yPoints, nPoints);}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {getDelegate().drawPolygon(xPoints, yPoints, nPoints);}

	@Override
	public void drawPolygon(Polygon p) {getDelegate().drawPolygon(p);}

	@Override
	public void translate(double tx, double ty) {getDelegate().translate(tx, ty);}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {getDelegate().fillPolygon(xPoints, yPoints, nPoints);}

	@Override
	public void fillPolygon(Polygon p) {getDelegate().fillPolygon(p);}

	@Override
	public void drawChars(char[] data, int offset, int length, int x, int y) {getDelegate().drawChars(data, offset, length, x, y);}

	@Override
	public void rotate(double theta) {getDelegate().rotate(theta);}

	@Override
	public void drawBytes(byte[] data, int offset, int length, int x, int y) {getDelegate().drawBytes(data, offset, length, x, y);}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {return getDelegate().drawImage(img, x, y, observer);}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {return getDelegate().drawImage(img, x, y, width, height, observer);}

	@Override
	public void rotate(double theta, double x, double y) {getDelegate().rotate(theta, x, y);}

	@Override
	public boolean drawImage(Image img, int x, int y, Color backgroundColor, ImageObserver observer) {return getDelegate().drawImage(img, x, y, backgroundColor, observer);}


	@Override
	public void scale(double sx, double sy) {getDelegate().scale(sx, sy);}


	@Override
	public void shear(double shx, double shy) {getDelegate().shear(shx, shy);}


	@Override
	public void transform(AffineTransform Tx) {getDelegate().transform(Tx);}


	@Override
	public void setTransform(AffineTransform Tx) {getDelegate().setTransform(Tx);}


	@Override
	public AffineTransform getTransform() {return getDelegate().getTransform();}


	@Override
	public Paint getPaint() {return getDelegate().getPaint();}


	@Override
	public Composite getComposite() {return getDelegate().getComposite();}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {return getDelegate().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);}

	@Override
	public void setBackground(Color color) {getDelegate().setBackground(color);}

	@Override
	public void dispose() {getDelegate().dispose();}

	@Override
	public Color getBackground() {return getDelegate().getBackground();}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMapView());
	}

	@Override
	public Stroke getStroke() {return getDelegate().getStroke();}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, getObjectVariableMapView().values().iterator());
	}

	private static @NonNls Map<String, Function<@Nonnull DelegatingGraphics2D, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }

	@Override
	public FontRenderContext getFontRenderContext() {return getDelegate().getFontRenderContext();}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		return ObjectUtilities.equalsImpl(this, o, DelegatingGraphics2D.class, true, getObjectVariableMapView().values().iterator());
	}


}
