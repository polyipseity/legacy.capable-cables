package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.ISpecified;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiContainerDefault<N extends Number> extends GuiContainer implements ISpecified<Rectangle<N, ?>> {
	/* SECTION constructors */

	public GuiContainerDefault(Container inventorySlotsIn) { super(inventorySlotsIn); }


	/* SECTION methods */

	@Override
	public void setWorldAndResolution(Minecraft game, int width, int height) {
		setGuiSize(width, height);
		super.setWorldAndResolution(game, width, height);
	}

	@Override
	public void setGuiSize(int w, int h) {
		super.setGuiSize(w, h);

		XY<N, ?> s = specification().getSize();
		xSize = s.getX().intValue();
		ySize = s.getY().intValue();
	}
}
