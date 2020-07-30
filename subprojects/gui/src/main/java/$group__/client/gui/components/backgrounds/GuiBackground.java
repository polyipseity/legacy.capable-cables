package $group__.client.gui.components.backgrounds;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiBackground<D extends GuiComponent.Data<E>, E extends GuiComponent.Events> extends GuiComponent<D> {
	public GuiBackground(D data) { super(getShapePlaceholder(), data); }

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) { renderBackground(stack, CacheKey.ROOT.get(this).getScreen(), mouse, partialTicks); }

	public void renderBackground(AffineTransformStack stack, Screen screen, Point2D mouse, float partialTicks) { GuiUtilities.GuiBackgrounds.drawnBackground(screen); }

	@Override
	public void onAdded(GuiContainer<?> parentCurrent, int index) {
		if (!(parentCurrent instanceof GuiRoot)) throw BecauseOf.illegalArgument("parentCurrent", parentCurrent);
		if (parentCurrent.getChildrenView().indexOf(this) != 0)
			throw new IllegalStateException("Illegal background GUI component index");
		data.anchors.add(data.anchors.getAnchorsToMatch(this, parentCurrent));
		super.onAdded(parentCurrent, index);
	}
}
