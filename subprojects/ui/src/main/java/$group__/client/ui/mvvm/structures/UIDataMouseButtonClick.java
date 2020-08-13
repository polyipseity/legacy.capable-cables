package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;
import $group__.utilities.ObjectUtilities;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.awt.geom.Point2D;

@Immutable
public final class UIDataMouseButtonClick implements IUIDataMouseButtonClick {
	protected final Point2D cursorPosition;
	protected final int button;
	protected final long timestamp;

	public UIDataMouseButtonClick(Point2D cursorPosition) { this(cursorPosition, MOUSE_BUTTON_NULL); }

	public UIDataMouseButtonClick(Point2D cursorPosition, int button) { this(cursorPosition, button, System.currentTimeMillis()); }

	protected UIDataMouseButtonClick(Point2D cursorPosition, int button, long timestamp) {
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
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	@Override
	public long getTimestampMills() { return timestamp; }

	@Override
	public IUIDataMouseButtonClick recreate() { return new UIDataMouseButtonClick(getCursorPosition(), getButton()); }

	@Override
	public UIDataMouseButtonClick copy() { return new UIDataMouseButtonClick(getCursorPosition(), getButton(), getTimestampMills()); }
}
