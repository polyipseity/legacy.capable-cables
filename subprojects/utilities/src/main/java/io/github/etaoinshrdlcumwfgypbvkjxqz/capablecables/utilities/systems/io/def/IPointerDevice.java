package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def;

import java.awt.geom.Point2D;

public interface IPointerDevice {
	Point2D getPositionView();

	void setCursor(ICursor cursor);
}
