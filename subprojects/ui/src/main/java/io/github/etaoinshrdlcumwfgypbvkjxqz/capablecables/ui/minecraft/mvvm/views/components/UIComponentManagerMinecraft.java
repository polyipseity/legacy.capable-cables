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

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentManagerMinecraft
		extends UIComponentManager<Rectangle2D>
		implements IUIComponentMinecraft {
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	protected final IUIRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new DefaultUIRendererContainer<>(this, CastUtilities.castUnchecked(NullUIComponentRendererMinecraft.class));

	@UIComponentConstructor
	public UIComponentManagerMinecraft(UIComponentConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) {
		getRendererContainer().setRenderer(renderer);
	}

	@Override
	public Class<? extends IUIComponentRendererMinecraft<?>> getDefaultRendererClass() { return getRendererContainer().getDefaultRendererClass(); }

	protected IUIRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }

	@Override
	public void tick(IUIComponentContext context) {}
}
