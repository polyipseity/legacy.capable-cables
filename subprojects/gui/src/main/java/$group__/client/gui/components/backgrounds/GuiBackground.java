package $group__.client.gui.components.backgrounds;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.utilities.Backgrounds;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiBackground extends GuiComponent {
	public GuiBackground() { super(new Rectangle2D.Double()); }

	public void renderBackground(MatrixStack matrix, Screen screen, Point2D mouse, float partialTicks) { Backgrounds.drawnBackground(screen); }

	@Override
	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {
		if (EnumState.READY.isReachedBy(getState())) {
			Screen screen = getRoot().orElseThrow(BecauseOf::unexpected).getScreen();
			renderBackground(matrix, screen, mouse, partialTicks);
		}
	}

	@Override
	public void onAdded(GuiContainer parent) {
		if (!(parent instanceof GuiRoot)) throw BecauseOf.illegalArgument("parent", parent);
		if (parent.getChildrenView().indexOf(this) != 0)
			throw new IllegalStateException("Illegal background GUI component index");
		anchors.add(anchors.getAnchorsToMatch(this, parent));
		super.onAdded(parent);
	}
}
