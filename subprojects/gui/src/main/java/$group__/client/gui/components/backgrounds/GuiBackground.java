package $group__.client.gui.components.backgrounds;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.GuiRoot;
import $group__.client.gui.utilities.Backgrounds;
import $group__.utilities.helpers.Adapters;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiBackground extends GuiComponent {
	public abstract void renderBackground(Screen screen, Point2D mouse, float partialTicks);

	@Override
	public void render(Point2D mouse, float partialTicks) {
		if (State.READY.isReachedBy(state)) {
			Screen screen = Adapters.get(GuiRoot.class, Screen.class, GuiRoot.TO_SCREEN_ADAPTER_DEFAULT).orElseThrow(BecauseOf::unexpected).apply(getRoot().orElseThrow(BecauseOf::unexpected));
			renderBackground(screen, mouse, partialTicks);
			Backgrounds.drawnBackground(screen);
		}
	}

	@Override
	protected void onAdded(GuiContainer parent) {
		if (!(parent instanceof GuiRoot)) throw BecauseOf.illegalArgument("parent", parent);
		if (parent.getChildren().indexOf(this) != 0)
			throw new IllegalStateException("Illegal background GUI component index");
		anchors.add(anchors.getAnchorsToMatch(parent));
		super.onAdded(parent);
	}
}
