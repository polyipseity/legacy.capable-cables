package $group__.client.gui.traits;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.GuiDragInfo;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public interface IScreenBridge {
	AffineTransformStack getTransformStack();

	GuiRoot<?, ?> getRoot();

	Optional<GuiDragInfo> getDragInfo(int button);
}
