package $group__.$modId__.client.gui.bases;

import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.traits.ISpec;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.IntConsumer;

@SideOnly(Side.CLIENT)
public enum GuiContainerBases {
	/* MARK empty */;


	/* SECTION static methods */

	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public static <T extends GuiContainer & ISpec<? extends Rectangle<?, ?>>> void initGuiBase(final T thisObj,
	                                                                                           IntConsumer xSizeSetter
			, IntConsumer ySizeSetter) {
		thisObj.spec().map(Rectangle::getSize).ifPresent(t -> {
			xSizeSetter.accept(t.getX().intValue());
			ySizeSetter.accept(t.getX().intValue());
		});
	}
}
