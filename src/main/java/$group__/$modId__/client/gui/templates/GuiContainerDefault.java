package $group__.$modId__.client.gui.templates;

import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.basic.ISpec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiContainerDefault<N extends Number> extends GuiContainer implements ISpec<Rectangle<N, ?>> {
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

		XY<N, ?> s = spec().getSize();
		xSize = s.getX().intValue();
		ySize = s.getY().intValue();
	}
}
