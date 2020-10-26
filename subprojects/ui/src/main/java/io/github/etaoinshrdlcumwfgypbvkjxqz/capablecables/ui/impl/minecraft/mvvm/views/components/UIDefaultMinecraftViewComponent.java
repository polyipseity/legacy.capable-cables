package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.modifiers.IUIComponentMinecraftLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering.IUIMinecraftComponentRenderer.EnumCropStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering.IUIMinecraftComponentRenderer.EnumRenderStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.events.bus.UIImmutableMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions.background.UIDefaultMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.EnumCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIDefaultViewComponent<S, M>
		implements IUIMinecraftViewComponent<S, M> {

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor
	public UIDefaultMinecraftViewComponent(UIViewComponentConstructor.IArguments arguments) {
		super(arguments);

		IExtensionContainer.addExtensionChecked(this, new UIDefaultMinecraftBackgroundExtension()); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	@Override
	public void render(IUIViewContext context, double partialTicks) {
		IUIView.getAnimationController(this).update();
		IUIView.getAnimationController(this).render();
		getManager()
				.ifPresent(manager ->
						EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
									EnumCropMethod cropMethod = EnumCropMethod.getBestMethod();
									cropMethod.enable();
									IUIViewComponent.traverseComponentTreeDefault(createComponentContext()
													.orElseThrow(IllegalStateException::new),
											manager,
											(componentContext, result) -> {
												assert result != null;
												assert componentContext != null;
												IUIComponentMinecraft component = (IUIComponentMinecraft) result.getComponent();
												IUIComponentModifier.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
														.forEachOrdered(modifierIntersection -> {
															IUIComponentModifier left = modifierIntersection.getLeft();
															IUIComponentRendererInvokerModifier right = modifierIntersection.getRight();
															EnumModifyStage.PRE.advanceModifyStage(left);
															right.invokeRenderer(componentContext); // COMMENT pre
															left.resetModifyStage();
														});
												component.getRendererContainer().getRenderer()
														.ifPresent(renderer -> {
															renderer.crop(componentContext, CastUtilities.castUnchecked(component),
																	EnumCropStage.CROP, cropMethod, partialTicks);
															renderer.render(componentContext, CastUtilities.castUnchecked(component),
																	EnumRenderStage.PRE_CHILDREN, partialTicks);
														});
											},
											(componentContext, result, children) -> {
												assert result != null;
												assert componentContext != null;
												IUIComponentMinecraft component = (IUIComponentMinecraft) result.getComponent();
												component.getRendererContainer().getRenderer().ifPresent(renderer -> {
													renderer.render(componentContext, CastUtilities.castUnchecked(component),
															EnumRenderStage.POST_CHILDREN, partialTicks);
													renderer.crop(componentContext, CastUtilities.castUnchecked(component),
															EnumCropStage.UN_CROP, cropMethod, partialTicks);
												});
												IUIComponentModifier.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
														.forEachOrdered(modifierIntersection -> {
															IUIComponentModifier left = modifierIntersection.getLeft();
															IUIComponentRendererInvokerModifier right = modifierIntersection.getRight();
															EnumModifyStage.POST.advanceModifyStage(left);
															right.invokeRenderer(componentContext); // COMMENT post
															left.resetModifyStage();
														});
											},
											component -> component.isVisible() && component instanceof IUIComponentMinecraft);
									cropMethod.disable();
									return true;
								},
								FunctionUtilities.accept(new UIAbstractViewBusEvent.Render(EnumHookStage.PRE, this),
										event -> IExtensionContainer.addExtensionChecked(event,
												UIImmutableMinecraftRenderEventExtension.of(partialTicks))),
								FunctionUtilities.accept(new UIAbstractViewBusEvent.Render(EnumHookStage.POST, this),
										event -> IExtensionContainer.addExtensionChecked(event,
												UIImmutableMinecraftRenderEventExtension.of(partialTicks))))
				);
		MinecraftOpenGLUtilities.Stacks.clearAll();
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void tick() {
		getManager()
				.ifPresent(manager -> IUIViewComponent.<RuntimeException>traverseComponentTreeDefault(createComponentContext()
								.orElseThrow(IllegalStateException::new),
						manager,
						(componentContext, result) -> {
							assert componentContext != null;
							assert result != null;
							CastUtilities.castChecked(IUIComponentMinecraft.class, result.getComponent())
									.ifPresent(cc ->
											IUIComponentMinecraftLifecycleModifier.handleComponentModifiers(cc,
													result.getModifiersView(),
													componentContext,
													IUIComponentMinecraftLifecycleModifier::tick)
									);
						},
						IConsumer3.StaticHolder.getEmpty(),
						IUIEventTarget::isActive));
	}
}
