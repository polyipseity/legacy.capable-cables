package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
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
	@NonNls
	private static final @Immutable Map<String, Function<DelegatingGraphics2D, ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<DelegatingGraphics2D, ?>>builder()
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
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {return getDelegate().drawImage(img, xform, obs);}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {getDelegate().drawImage(img, op, x, y);}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {getDelegate().drawRenderedImage(img, xform);}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {getDelegate().drawRenderableImage(img, xform);}

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
	public void setComposite(Composite comp) {getDelegate().setComposite(comp);}

	@Override
	public Graphics create(int x, int y, int width, int height) {return getDelegate().create(x, y, width, height);}

	@Override
	public void setPaint(Paint paint) {getDelegate().setPaint(paint);}

	@Override
	public Color getColor() {return getDelegate().getColor();}

	@Override
	public void setStroke(Stroke s) {getDelegate().setStroke(s);}

	@Override
	public void setColor(Color c) {getDelegate().setColor(c);}

	@Override
	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {getDelegate().setRenderingHint(hintKey, hintValue);}

	@Override
	public void setPaintMode() {getDelegate().setPaintMode();}

	@Override
	public Object getRenderingHint(RenderingHints.Key hintKey) {return getDelegate().getRenderingHint(hintKey);}

	@Override
	public void setXORMode(Color c1) {getDelegate().setXORMode(c1);}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {getDelegate().setRenderingHints(hints);}

	@Override
	public Font getFont() {return getDelegate().getFont();}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {getDelegate().addRenderingHints(hints);}

	@Override
	public void setFont(Font font) {getDelegate().setFont(font);}

	@Override
	public RenderingHints getRenderingHints() {return getDelegate().getRenderingHints();}

	@Override
	public FontMetrics getFontMetrics() {return getDelegate().getFontMetrics();}

	@Override
	public void translate(int x, int y) {getDelegate().translate(x, y);}

	@Override
	public FontMetrics getFontMetrics(Font f) {return getDelegate().getFontMetrics(f);}

	@Override
	public void translate(double tx, double ty) {getDelegate().translate(tx, ty);}

	@Override
	public Rectangle getClipBounds() {return getDelegate().getClipBounds();}

	@Override
	public void rotate(double theta) {getDelegate().rotate(theta);}

	@Override
	public void clipRect(int x, int y, int width, int height) {getDelegate().clipRect(x, y, width, height);}

	@Override
	public void rotate(double theta, double x, double y) {getDelegate().rotate(theta, x, y);}

	@Override
	public void setClip(int x, int y, int width, int height) {getDelegate().setClip(x, y, width, height);}

	@Override
	public void scale(double sx, double sy) {getDelegate().scale(sx, sy);}

	@Override
	public Shape getClip() {return getDelegate().getClip();}

	@Override
	public void shear(double shx, double shy) {getDelegate().shear(shx, shy);}

	@Override
	public void setClip(Shape clip) {getDelegate().setClip(clip);}

	@Override
	public void transform(AffineTransform Tx) {getDelegate().transform(Tx);}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {getDelegate().copyArea(x, y, width, height, dx, dy);}

	@Override
	public void setTransform(AffineTransform Tx) {getDelegate().setTransform(Tx);}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {getDelegate().drawLine(x1, y1, x2, y2);}

	@Override
	public AffineTransform getTransform() {return getDelegate().getTransform();}

	@Override
	public void fillRect(int x, int y, int width, int height) {getDelegate().fillRect(x, y, width, height);}

	@Override
	public Paint getPaint() {return getDelegate().getPaint();}

	@Override
	public void drawRect(int x, int y, int width, int height) {getDelegate().drawRect(x, y, width, height);}

	@Override
	public Composite getComposite() {return getDelegate().getComposite();}

	@Override
	public void clearRect(int x, int y, int width, int height) {getDelegate().clearRect(x, y, width, height);}

	@Override
	public void setBackground(Color color) {getDelegate().setBackground(color);}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {getDelegate().drawRoundRect(x, y, width, height, arcWidth, arcHeight);}

	@Override
	public Color getBackground() {return getDelegate().getBackground();}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {getDelegate().fillRoundRect(x, y, width, height, arcWidth, arcHeight);}

	@Override
	public Stroke getStroke() {return getDelegate().getStroke();}

	@Override
	public void drawOval(int x, int y, int width, int height) {getDelegate().drawOval(x, y, width, height);}

	@Override
	public void clip(Shape s) {getDelegate().clip(s);}

	@Override
	public void fillOval(int x, int y, int width, int height) {getDelegate().fillOval(x, y, width, height);}

	@Override
	public FontRenderContext getFontRenderContext() {return getDelegate().getFontRenderContext();}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {getDelegate().drawArc(x, y, width, height, startAngle, arcAngle);}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {getDelegate().fillArc(x, y, width, height, startAngle, arcAngle);}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {getDelegate().drawPolyline(xPoints, yPoints, nPoints);}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {getDelegate().drawPolygon(xPoints, yPoints, nPoints);}

	@Override
	public void drawPolygon(Polygon p) {getDelegate().drawPolygon(p);}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {getDelegate().fillPolygon(xPoints, yPoints, nPoints);}

	@Override
	public void fillPolygon(Polygon p) {getDelegate().fillPolygon(p);}

	@Override
	public void drawChars(char[] data, int offset, int length, int x, int y) {getDelegate().drawChars(data, offset, length, x, y);}

	@Override
	public void drawBytes(byte[] data, int offset, int length, int x, int y) {getDelegate().drawBytes(data, offset, length, x, y);}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {return getDelegate().drawImage(img, x, y, observer);}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {return getDelegate().drawImage(img, x, y, width, height, observer);}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {return getDelegate().drawImage(img, x, y, bgcolor, observer);}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {return getDelegate().drawImage(img, x, y, width, height, bgcolor, observer);}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {return getDelegate().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {return getDelegate().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);}

	@Override
	public void dispose() {getDelegate().dispose();}

	@SuppressWarnings({"MethodDoesntCallSuperMethod", "FinalizeDeclaration"})
	@Override
	public void finalize() {getDelegate().finalize();}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static @Immutable Map<String, Function<DelegatingGraphics2D, ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }

	@Override
	public boolean hitClip(int x, int y, int width, int height) {return getDelegate().hitClip(x, y, width, height);}

	@Override
	public Rectangle getClipBounds(Rectangle r) {return getDelegate().getClipBounds(r);}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, getObjectVariableMap().values());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		return ObjectUtilities.equalsImpl(this, o, DelegatingGraphics2D.class, true, getObjectVariableMap().values());
	}


}
