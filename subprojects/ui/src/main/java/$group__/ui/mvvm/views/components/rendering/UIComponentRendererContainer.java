package $group__.ui.mvvm.views.components.rendering;

import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRenderer;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIComponentRendererContainer<R extends IUIComponentRenderer>
		implements IUIComponentRendererContainer<R> {
	@Nullable
	protected R renderer;

	public UIComponentRendererContainer(@Nullable R renderer) { this.renderer = renderer; }

	@Override
	public Optional<? extends R> getRenderer() { return Optional.ofNullable(renderer); }

	@Override
	public void setRenderer(@Nullable R renderer) { this.renderer = renderer; }
}
