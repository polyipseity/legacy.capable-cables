package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import javax.annotation.Nullable;
import java.util.Optional;

public class DefaultUIRendererContainer<R extends IUIRenderer<?>>
		implements IUIRendererContainer<R> {
	private final Class<? extends R> defaultRendererClass;
	@Nullable
	private R renderer;

	public DefaultUIRendererContainer(R renderer) {
		this(CastUtilities.<Class<? extends R>>castUnchecked(renderer.getClass())); // COMMENT should be safe
		this.renderer = renderer;
	}

	public DefaultUIRendererContainer(Class<? extends R> defaultRendererClass) { this.defaultRendererClass = defaultRendererClass; }

	@Override
	public Optional<? extends R> getRenderer() { return Optional.ofNullable(renderer); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable R renderer) { this.renderer = renderer; }

	@Override
	public Class<? extends R> getDefaultRendererClass() { return defaultRendererClass; }
}
