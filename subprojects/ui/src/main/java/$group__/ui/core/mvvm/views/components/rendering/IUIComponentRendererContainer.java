package $group__.ui.core.mvvm.views.components.rendering;

import $group__.ui.core.mvvm.views.components.IUIComponent;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUIComponentRendererContainer<R extends IUIComponentRenderer<?>> {
	Optional<? extends R> getRenderer();

	void setRenderer(@Nullable R renderer);

	static <C extends IUIComponent & IUIComponentRendererContainer<? super R>, R extends IUIComponentRenderer<? super C>> void setRendererChecked(C container, R renderer) { container.setRenderer(renderer); }
}
