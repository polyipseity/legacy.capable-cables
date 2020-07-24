package $group__.client.gui.structures;

import $group__.client.gui.components.GuiComponent;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public final class GuiDragInfo {
	public final GuiComponent dragged;
	public final Point2D start;
	public final int button;

	public GuiDragInfo(GuiComponent dragged, Point2D start, int button) {
		this.dragged = dragged;
		this.start = start;
		this.button = button;
	}
}
