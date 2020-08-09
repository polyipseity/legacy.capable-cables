package $group__.client.ui.coredeprecated.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIAnchor {
	IShapeDescriptor<?, ?> getTo();

	EnumUISide getToSide();

	EnumUISide getFromSide();

	double getBorderThickness();

	void anchor(IShapeDescriptor<?, ?> from);

	void onContainerAdd(IUIAnchorSet<?> container);

	void onContainerRemove();
}
