package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering.IUIMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.rendering.UIDefaultMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.Rectangle2D;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftComponentManager
		extends UIDefaultComponentManager<Rectangle2D>
		implements IUIComponentMinecraft {
	private final AtomicReference<IUIRendererContainer<IUIMinecraftComponentRenderer<?>>> rendererContainerReference = new AtomicReference<>();

	@UIComponentConstructor
	public UIDefaultMinecraftComponentManager(UIComponentConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public IUIRendererContainer<? extends IUIMinecraftComponentRenderer<?>> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		IUIRendererContainer<IUIMinecraftComponentRenderer<?>> rendererContainer = new UIDefaultRendererContainer<>(name, this, CastUtilities.castUnchecked(UIDefaultMinecraftComponentRenderer.class));
		if (!getRendererContainerReference().compareAndSet(null, rendererContainer))
			throw new IllegalStateException();
		getBinderObserverSupplier().ifPresent(rendererContainer::initializeBindings);
	}

	protected AtomicReference<IUIRendererContainer<IUIMinecraftComponentRenderer<?>>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		Optional.ofNullable(getRendererContainerReference().get())
				.ifPresent(rendererContainer -> rendererContainer.initializeBindings(binderObserverSupplier));
	}

	@Override
	public void tick(IUIComponentContext context) {}
}
