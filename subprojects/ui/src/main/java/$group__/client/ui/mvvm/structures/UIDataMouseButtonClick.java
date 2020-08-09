package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.awt.geom.Point2D;
import java.util.function.Function;

@Immutable
public final class UIDataMouseButtonClick implements IUIDataMouseButtonClick {
	public static final ImmutableList<Function<UIDataMouseButtonClick, Object>> OBJECT_VARIABLES = ImmutableList.of(
			UIDataMouseButtonClick::getCursorPositionView, UIDataMouseButtonClick::getButton);
	public static final ImmutableMap<String, Function<UIDataMouseButtonClick, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("cursor", "button"), OBJECT_VARIABLES));
	protected final Point2D cursorPosition;
	protected final int button;

	public UIDataMouseButtonClick(Point2D cursorPosition) { this(cursorPosition, MOUSE_BUTTON_NULL); }

	public UIDataMouseButtonClick(Point2D cursorPosition, int button) {
		this.cursorPosition = (Point2D) cursorPosition.clone();
		this.button = button;
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

	public static class Tracked {
		protected final IUIComponent component;
		protected final UIDataMouseButtonClick data;

		public Tracked(IUIComponent component, UIDataMouseButtonClick data) {
			this.component = component;
			this.data = data;
		}

		public IUIComponent getComponent() { return component; }

		public UIDataMouseButtonClick getData() { return data; }
	}
}
