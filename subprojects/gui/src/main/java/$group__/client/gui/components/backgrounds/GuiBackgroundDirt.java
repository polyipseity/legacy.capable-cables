package $group__.client.gui.components.backgrounds;

import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.utilities.GuiUtilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class GuiBackgroundDirt extends GuiBackground {
	@Override
	public void renderBackground(AffineTransformStack stack, Screen screen, Point2D mouse, float partialTicks) { GuiUtilities.GuiBackgrounds.renderDirtBackground(screen.getMinecraft(), screen.width, screen.height, 0); }
}
