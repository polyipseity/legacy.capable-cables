package $group__.client.gui.components;

import $group__.client.gui.structures.GuiAnchors;
import $group__.client.gui.structures.GuiConstraints;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;

@OnlyIn(Dist.CLIENT)
public class GuiComponent implements IRenderable, IGuiEventListener {
	@Nullable
	protected WeakReference<GuiRoot> root;
	@Nullable
	protected WeakReference<GuiContainer> parent;
	protected final Rectangle2D rectangle = new Rectangle();
	protected final GuiAnchors anchors = new GuiAnchors();
	protected final GuiConstraints constraints = GuiConstraints.CONSTRAINTS_NONE;

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {}

	public Rectangle2D getRectangleView() { return (Rectangle2D) rectangle.clone(); }

	protected void onInitialize() {}

	@OverridingMethodsMustInvokeSuper
	protected void onResize() {
		anchors.accept(rectangle);
		constraints.accept(rectangle);
	}

	protected void onTick() {}

	protected void onClose() {}

	protected void onRemoved() {}
}
