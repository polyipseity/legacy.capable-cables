package $group__.client.ui.mvvm.views.components.minecraft.managers;

import $group__.client.ui.mvvm.views.UIManagerComponent;
import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.client.ui.mvvm.views.components.minecraft.common.UIWindowComponent;
import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class UIManagerWindowsComponent
		<M extends IGuiModel<?>, V extends IUIComponent.IGuiView<?, SD>, C extends IUIComponent.IGuiController.IManager<?, CH>,
				CH extends UIWindowComponent<?, ?, ?, ?>,
				SD extends IShapeDescriptor<?, ?>>
		extends UIManagerComponent {
	public UIManagerWindowsComponent(M model, V view, C controller) { super(model, view, controller); }

	@Override
	public boolean reshape(Function<? super SD, Boolean> action) throws ConcurrentModificationException {
		// TODO implement
		return super.reshape(action);
	}
}
