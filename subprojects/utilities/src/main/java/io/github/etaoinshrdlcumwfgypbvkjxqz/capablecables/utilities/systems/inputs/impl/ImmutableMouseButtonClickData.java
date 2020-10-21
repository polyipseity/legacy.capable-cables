package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IMouseButtonClickData;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public final class ImmutableMouseButtonClickData implements IMouseButtonClickData {
	private final Point2D cursorPosition;
	private final int button;
	private final long timestamp;

	public ImmutableMouseButtonClickData(Point2D cursorPosition) { this(cursorPosition, StaticHolder.getMouseButtonNull()); }

	public ImmutableMouseButtonClickData(Point2D cursorPosition, int button) { this(cursorPosition, button, System.currentTimeMillis()); }

	private ImmutableMouseButtonClickData(Point2D cursorPosition, int button, long timestamp) {
		this.cursorPosition = (Point2D) cursorPosition.clone();
		this.button = button;
		this.timestamp = timestamp;
	}

	@Override
	public Point2D getCursorPositionView() { return (Point2D) getCursorPosition().clone(); }

	protected Point2D getCursorPosition() { return cursorPosition; }

	@Override
	public int getButton() { return button; }

	@Override
	public long getTimestampMills() { return timestamp; }

	@Override
	public IMouseButtonClickData recreate() { return new ImmutableMouseButtonClickData(getCursorPosition(), getButton()); }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, StaticHolder.getObjectVariables()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, IMouseButtonClickData.class, obj, true, null, StaticHolder.getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }

	@Override
	public ImmutableMouseButtonClickData copy() { return new ImmutableMouseButtonClickData(getCursorPosition(), getButton(), getTimestampMills()); }
}
