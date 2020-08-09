package $group__.client.ui.mvvm.core.structures;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.awt.geom.Point2D;

@Immutable
public interface IUIDataMouseButtonClick extends Cloneable {
	int MOUSE_BUTTON_NULL = -1;

	Point2D getCursorPositionView();

	int getButton();
}
