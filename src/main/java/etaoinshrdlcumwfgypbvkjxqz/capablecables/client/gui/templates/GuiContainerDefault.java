package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.ISpecified;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiContainerDefault<N extends Number> extends GuiContainer implements ISpecified<Rectangle<N, ?>> {
	public GuiContainerDefault(Container inventorySlotsIn) { super(inventorySlotsIn); }

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		setGuiSize(width, height);
		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	public void setGuiSize(int w, int h) {
		super.setGuiSize(w, h);

		XY<N, ?> size = specification().getSize();
		xSize = size.getX().intValue();
		ySize = size.getY().intValue();
	}
}
