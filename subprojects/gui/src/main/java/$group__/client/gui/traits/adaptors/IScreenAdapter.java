package $group__.client.gui.traits.adaptors;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.GuiDragInfo;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Optional;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public interface IScreenAdapter {
	AffineTransformStack getTransformStack();

	GuiRoot<?, ?> getRoot();

	Map<Integer, GuiDragInfo> getDragInfo();

	Optional<GuiDragInfo> getDragInfo(int button);
}
