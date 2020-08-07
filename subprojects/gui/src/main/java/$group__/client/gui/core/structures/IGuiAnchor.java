package $group__.client.gui.core.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IGuiAnchor {
	IShapeDescriptor<?, ?> getTo();

	EnumGuiSide getToSide();

	EnumGuiSide getFromSide();

	double getBorderThickness();

	void anchor(IShapeDescriptor<?, ?> from);

	void onContainerAdd(IGuiAnchorSet<?> container);

	void onContainerRemove();
}
