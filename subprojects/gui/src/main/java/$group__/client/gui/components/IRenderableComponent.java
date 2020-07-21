package $group__.client.gui.components;

import $group__.annotations.OverridingStatus;
import $group__.client.gui.ConstantsGui;
import $group__.client.gui.structures.AffineTransformStack;
import net.minecraft.client.gui.IRenderable;

import javax.annotation.meta.When;
import java.awt.geom.Point2D;

@SuppressWarnings("SpellCheckingInspection")
public interface IRenderableComponent extends IRenderable {
	void render(AffineTransformStack stack, Point2D mouse, float partialTicks);

	////////// Deprecated //////////

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default void render(int mouseX, int mouseY, float partialTicks) { render(new AffineTransformStack(), new Point2D.Double(mouseX, mouseY), partialTicks); }
}
