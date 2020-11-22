package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftGraphics;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.modifiers.IUIComponentMinecraftLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.extensions.IUIMinecraftRenderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.events.bus.UIImmutableMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions.background.UIDefaultMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.EnumMinecraftCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIAbstractViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIImmutableComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths.ArrayAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.DelayedFieldInitializer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths.FunctionalPath;
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
	private final DelayedFieldInitializer<ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>>> extensionsInitializer;

	@UIViewComponentConstructor
	public UIDefaultMinecraftViewComponent(IUIViewComponentArguments arguments) {
		super(arguments);

		this.extensionsInitializer = new DelayedFieldInitializer<>(field ->
				IExtensionContainer.addExtensionChecked(this, new UIDefaultMinecraftBackgroundExtension()) /* COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired */);
	}

	@Override
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() {
		return extensionsInitializer.apply(super.getExtensions());
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

	@Override
	public Optional<? extends IUIComponentContext> createComponentContext() {
		return getContext()
				.map(context -> {
							try (AutoCloseableGraphics2D graphics = MinecraftGraphics.createGraphics()) {
								return new UIImmutableComponentContext(
										context,
										this,
										new UIDefaultComponentContextMutator(),
										graphics,
										new FunctionalPath<>(ImmutableList.of(), Lists::newArrayList),
										new ArrayAffineTransformStack(CapacityUtilities.getInitialCapacityMedium()));
							}
						}
				);
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
}
