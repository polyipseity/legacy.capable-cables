package $group__.ui.core.mvvm.views.components.rendering;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUIComponentRendererContainer<R extends IUIComponentRenderer> {
	Optional<? extends R> getRenderer();

	void setRenderer(@Nullable R renderer);
}
