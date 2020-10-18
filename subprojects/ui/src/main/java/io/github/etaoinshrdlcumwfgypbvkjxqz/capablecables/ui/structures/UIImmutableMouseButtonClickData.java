package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public final class UIImmutableMouseButtonClickData implements IUIMouseButtonClickData {
	private final Point2D cursorPosition;
	private final int button;
	private final long timestamp;

	public UIImmutableMouseButtonClickData(Point2D cursorPosition) { this(cursorPosition, StaticHolder.getMouseButtonNull()); }

	public UIImmutableMouseButtonClickData(Point2D cursorPosition, int button) { this(cursorPosition, button, System.currentTimeMillis()); }

	private UIImmutableMouseButtonClickData(Point2D cursorPosition, int button, long timestamp) {
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
	public int hashCode() { return ObjectUtilities.hashCode(this, null, StaticHolder.getObjectVariables()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, StaticHolder.getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }

	@Override
	public long getTimestampMills() { return timestamp; }

	@Override
	public IUIMouseButtonClickData recreate() { return new UIImmutableMouseButtonClickData(getCursorPosition(), getButton()); }

	@Override
	public UIImmutableMouseButtonClickData copy() { return new UIImmutableMouseButtonClickData(getCursorPosition(), getButton(), getTimestampMills()); }
}
