package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.software.MinecraftSoftwareGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIAbstractViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.components.modifiers.IUIComponentMinecraftLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.extensions.IUIMinecraftRenderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.events.bus.UIImmutableMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.extensions.background.UIDefaultMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;
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
		MinecraftSoftwareGraphics2D.clear();
		super.render0();
		MinecraftSoftwareGraphics2D.draw();
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
							try (AutoCloseableGraphics2D graphics = MinecraftSoftwareGraphics2D.createGraphics()) {
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
