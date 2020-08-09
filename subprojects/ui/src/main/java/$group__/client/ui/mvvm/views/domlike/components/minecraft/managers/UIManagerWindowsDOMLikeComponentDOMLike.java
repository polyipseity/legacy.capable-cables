package $group__.client.ui.mvvm.views.domlike.components.minecraft.managers;

import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.UIManagerDOMLikeComponentDOMLike;
import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.views.domlike.components.minecraft.common.UIWindowDOMLikeComponentDOMLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class UIManagerWindowsDOMLikeComponentDOMLike
		<M extends IGuiModel<?>, V extends IUIComponentDOMLike.IGuiView<?, SD>, C extends IUIComponentDOMLike.IGuiController.IManager<?, CH>,
		CH extends UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>,
		SD extends IShapeDescriptor<?, ?>>
		extends UIManagerDOMLikeComponentDOMLike {
	public UIManagerWindowsDOMLikeComponentDOMLike(M model, V view, C controller) { super(model, view, controller); }

	@Override
	public boolean reshape(Function<? super SD, Boolean> action) throws ConcurrentModificationException {
		// TODO implement
		return super.reshape(action);
	}
}
