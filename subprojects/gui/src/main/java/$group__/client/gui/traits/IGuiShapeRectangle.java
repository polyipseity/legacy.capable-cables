package $group__.client.gui.traits;

import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Rectangle2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public interface IGuiShapeRectangle {
	Rectangle2D getRectangleView();
}
