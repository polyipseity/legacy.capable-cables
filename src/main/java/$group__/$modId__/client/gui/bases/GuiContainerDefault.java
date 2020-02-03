package $group__.$modId__.client.gui.bases;

import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.basic.ISpec;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiContainerDefault<N extends Number> extends GuiContainer implements ISpec<Rectangle<N, ?>> {
	/* SECTION constructors */

	public GuiContainerDefault(Container inventorySlotsIn) { super(inventorySlotsIn); }


	/* SECTION methods */

	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	@Override
	public void initGui() {
		spec().map(Rectangle::getSize).ifPresent(t -> {
			xSize = t.getX().intValue();
			ySize = t.getY().intValue();
		});
		super.initGui();
	}
}
