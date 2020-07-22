package $group__.client.gui.components.backgrounds;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiBackground extends GuiComponent {
	public GuiBackground() { super(getShapePlaceholder()); }

	public void renderBackground(AffineTransformStack stack, Screen screen, Point2D mouse, float partialTicks) { GuiUtilities.GuiBackgrounds.drawnBackground(screen); }

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumState.READY.isReachedBy(getState())) {
			GuiRoot<?> root = GuiCache.Keys.ROOT.get(this);
			renderBackground(stack, root.getScreen(), mouse, partialTicks);
		}
	}

	@Override
	public void onAdded(GuiContainer parent, int index) {
		if (!(parent instanceof GuiRoot)) throw BecauseOf.illegalArgument("parent", parent);
		if (parent.getChildrenView().indexOf(this) != 0)
			throw new IllegalStateException("Illegal background GUI component index");
		anchors.add(anchors.getAnchorsToMatch(this, parent));
		super.onAdded(parent, index);
	}
}
