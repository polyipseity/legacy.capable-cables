package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ColorUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractGraphics2D
		extends Graphics2D {
	private final AffineTransform internalTransform = new AffineTransform();
	private Paint paint = ColorUtilities.getColorless();
	private Stroke stroke = new BasicStroke();
	private Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F);
	private @Nullable Shape internalClip;

	@Override
	public Color getColor() {
		Paint paint = getPaint();
		if (paint instanceof Color)
			return (Color) paint;
		return null;
	}

	@Override
	public void setColor(@Nullable Color c) {
		setPaint(c);
	}

	@Override
	public void clip(@Nullable Shape shape) {
		if (shape == null) {
			setClip(null);
			return;
		}
		@Nullable Shape clip = getInternalClip();
		if (clip == null) {
			setClip(shape);
			return;
		}
		Area newClip = new Area(clip);
		newClip.intersect(new Area(shape));
		setInternalClip(newClip);
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		clip(new Rectangle(x, y, width, height));
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		setClip(new Rectangle(x, y, width, height));
	}

	@Nullable
	@Override
	public Shape getClip() {
		return Optional.ofNullable(getInternalClip())
				.map(UIObjectUtilities::copyShape)
				.orElse(null);
	}

	@Override
	public void setClip(@Nullable Shape clip) {
		setInternalClip(
				Optional.ofNullable(clip)
						.map(UIObjectUtilities::copyShape)
						.orElse(null)
		);
	}

	@Override
	public void setComposite(@Nullable Composite composite) {
		Objects.requireNonNull(composite);
		this.composite = composite;
	}

	@Nullable
	protected Shape getInternalClip() {
		return internalClip;
	}

	@Override
	public Composite getComposite() {
		return composite;
	}

	protected void setInternalClip(@Nullable Shape internalClip) {
		this.internalClip = internalClip;
	}

	@Override
	public void transform(AffineTransform transform) {
		getInternalTransform().concatenate(transform);
	}

	@Override
	public void setTransform(AffineTransform transform) {
		getInternalTransform().setTransform(transform);
	}

	@Override
	public AffineTransform getTransform() {
		return (AffineTransform) getInternalTransform().clone();
	}

	@Override
	public void setPaint(@Nullable Paint paint) {
		Objects.requireNonNull(paint);
		this.paint = paint;
	}

	@Override
	public void setStroke(@Nullable Stroke stroke) {
		Objects.requireNonNull(stroke);
		this.stroke = stroke;
	}


	@Override
	public Paint getPaint() {
		return paint;
	}

	@Override
	public Stroke getStroke() {
		return stroke;
	}

	protected AffineTransform getInternalTransform() {
		return internalTransform;
	}


}
