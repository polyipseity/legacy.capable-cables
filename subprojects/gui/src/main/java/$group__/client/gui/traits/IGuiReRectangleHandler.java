package $group__.client.gui.traits;

import $group__.client.gui.components.GuiComponent;

import java.awt.geom.Rectangle2D;

public interface IGuiReRectangleHandler {
	void reRectangle(GuiComponent invoker);

	void reRectangle(GuiComponent invoker, Rectangle2D rectangle);
}
