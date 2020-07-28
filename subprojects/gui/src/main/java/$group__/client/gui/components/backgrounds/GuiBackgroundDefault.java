package $group__.client.gui.components.backgrounds;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.utilities.GuiUtilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class GuiBackgroundDefault<D extends GuiComponent.Data<E>, E extends GuiComponent.Events> extends GuiBackground<D, E> {
	public GuiBackgroundDefault(D data) { super(data); }

	@Override
	public void renderBackground(AffineTransformStack stack, Screen screen, Point2D mouse, float partialTicks) { GuiUtilities.GuiBackgrounds.renderBackground(screen.getMinecraft(), screen.width, screen.height, 0); }
}
