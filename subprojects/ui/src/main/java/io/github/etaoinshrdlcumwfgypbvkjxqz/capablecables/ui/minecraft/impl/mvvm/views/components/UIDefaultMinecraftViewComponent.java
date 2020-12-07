package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftGraphics;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIAbstractViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.modifiers.IUIComponentMinecraftLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.extensions.IUIMinecraftRenderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.events.bus.UIImmutableMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.extensions.background.UIDefaultMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities.EnumMinecraftCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIAbstractViewComponent<S, M>
		implements IUIMinecraftViewComponent<S, M> {
	private final Runnable extensionsInitializer;

	@UIViewComponentConstructor
	public UIDefaultMinecraftViewComponent(IUIViewComponentArguments arguments) {
		super(arguments);

		this.extensionsInitializer = new OneUseRunnable(() ->
				IExtensionContainer.addExtensionChecked(this, new UIDefaultMinecraftBackgroundExtension()) /* COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired */);
	}

	@Override
	protected ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensions() {
		extensionsInitializer.run();
		return super.getExtensions();
	}

	@Override
	protected void render0() {
		EnumMinecraftCropMethod cropMethod = EnumMinecraftCropMethod.getBestMethod();

		MinecraftGraphics.clear();
		MinecraftOpenGLUtilities.Stacks.clearAll();
		cropMethod.enable();
		super.render0();
		cropMethod.disable();
		MinecraftOpenGLUtilities.Stacks.clearAll();
		MinecraftGraphics.draw();
	}

	@Override
	protected void acceptRenderEvent(UIAbstractViewBusEvent.Render event) {
		IUIMinecraftRenderExtension.StaticHolder.getType().getValue().find(this).ifPresent(extension ->
				IExtensionContainer.addExtensionChecked(event, UIImmutableMinecraftRenderEventExtension.of(extension.getPartialTicks())));
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void tick() {
		try (IUIComponentContext componentContext = createComponentContext().orElseThrow(IllegalStateException::new)) {
			IUIViewComponent.<RuntimeException>traverseComponentTreeDefault(componentContext, // TODO javac bug, need explicit type arguments
					getManager(),
					(componentContext2, result) -> {
						assert componentContext2 != null;
						assert result != null;
						CastUtilities.castChecked(IUIComponentMinecraft.class, result.getComponent())
								.ifPresent(cc ->
										IUIComponentMinecraftLifecycleModifier.handleComponentModifiers(cc,
												result.getModifiersView(),
												componentContext2,
												IUIComponentMinecraftLifecycleModifier::tick)
								);
					},
					IConsumer3.StaticHolder.getEmpty(),
					IUIEventTarget::isActive);
		}
	}

	@Override
	public Optional<? extends IUIComponentContext> createComponentContext() {
		return getContext()
				.map(context -> {
							try (AutoCloseableGraphics2D graphics = MinecraftGraphics.createGraphics()) {
								return new UIDefaultComponentContext(
										context,
										this,
										new UIDefaultComponentContextMutator(),
										graphics);
							}
						}
				);
	}
}
