package $group__.client.gui.traits.handlers;

import $group__.client.gui.components.GuiComponent;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public interface IGuiReshapeHandler {
	void reshape(GuiComponent invoker);

	void reshape(GuiComponent invoker, Shape shape);
}
