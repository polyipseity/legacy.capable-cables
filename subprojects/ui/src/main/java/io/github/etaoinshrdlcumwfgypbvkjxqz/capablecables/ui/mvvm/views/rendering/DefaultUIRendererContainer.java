package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.naming.AbstractNamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;

import javax.annotation.Nullable;
import java.util.Optional;

public class DefaultUIRendererContainer<C, R extends IUIRenderer<?>>
		extends AbstractNamed
		implements IUIRendererContainer<R> {
	private final OptionalWeakReference<C> container;
	private final Class<? extends R> defaultRendererClass;
	@Nullable
	private R renderer;

	public DefaultUIRendererContainer(String name, C container, Class<? extends R> defaultRendererClass) {
		super(name);
		this.container = new OptionalWeakReference<>(container);
		this.defaultRendererClass = defaultRendererClass;
	}

	@Override
	public Optional<? extends R> getRenderer() { return Optional.ofNullable(renderer); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable R renderer) {
		IUIRendererContainer.StaticHolder.setRendererImpl(getContainer().getOptional().orElseThrow(IllegalStateException::new),
				renderer,
				renderer2 -> this.renderer = renderer2,
				this.renderer);
	}

	@Override
	public Class<? extends R> getDefaultRendererClass() { return defaultRendererClass; }

	protected OptionalWeakReference<C> getContainer() { return container; }
}
