package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.rendering.NullUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.DefaultUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Rectangle2D;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@OnlyIn(Dist.CLIENT)
public class UIComponentManagerMinecraft
		extends UIComponentManager<Rectangle2D>
		implements IUIComponentMinecraft {
	@UIComponentConstructor
	public UIComponentManagerMinecraft(UIComponentConstructor.IArguments arguments) {
		super(arguments);
	}

	private final AtomicReference<IUIRendererContainer<IUIComponentRendererMinecraft<?>>> rendererContainerReference = new AtomicReference<>();

	@Override
	public IUIRendererContainer<? extends IUIComponentRendererMinecraft<?>> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(String name)
			throws IllegalStateException {
		if (!getRendererContainerReference().compareAndSet(null,
				new DefaultUIRendererContainer<>(name, this, CastUtilities.castUnchecked(NullUIComponentRendererMinecraft.class))))
			throw new IllegalStateException();
	}

	protected AtomicReference<IUIRendererContainer<IUIComponentRendererMinecraft<?>>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	public void tick(IUIComponentContext context) {}
}
