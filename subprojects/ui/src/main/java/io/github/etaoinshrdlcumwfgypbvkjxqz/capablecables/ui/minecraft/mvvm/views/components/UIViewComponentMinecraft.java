package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.modifiers.IUIComponentMinecraftLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft.EnumCropStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft.EnumRenderStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions.background.DefaultUIExtensionMinecraftBackground;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.EnumCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class UIViewComponentMinecraft<S extends Shape, M extends IUIComponentManager<S>>
		extends UIViewComponent<S, M>
		implements IUIViewComponentMinecraft<S, M> {

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor(type = UIViewComponentConstructor.EnumConstructorType.MAPPINGS)
	public UIViewComponentMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> manager) {
		super(manager);
		IExtensionContainer.StaticHolder.addExtensionExtendedChecked(this, new DefaultUIExtensionMinecraftBackground<>(IUIViewComponentMinecraft.class)); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	@Override
	public void render(IUIViewContext context, double partialTicks) {
		getManager()
				.ifPresent(manager ->
						EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
									EnumCropMethod cropMethod = EnumCropMethod.getBestMethod();
									cropMethod.enable();
									IUIViewComponent.StaticHolder.traverseComponentTreeDefault(createComponentContext(context),
											manager,
											(componentContext, result) -> {
												assert result != null;
												assert componentContext != null;
												IUIComponentMinecraft component = (IUIComponentMinecraft) result.getComponent();
												IUIComponentModifier.StaticHolder.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
														.forEachOrdered(modifier -> {
															assert modifier.getKey() != null;
															assert modifier.getValue() != null;
															EnumModifyStage.PRE.advanceModifyStage(modifier.getKey());
															modifier.getValue().invokeRenderer(componentContext); // COMMENT pre
															modifier.getKey().resetModifyStage();
														});
												component.getRenderer()
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
												component.getRenderer().ifPresent(renderer -> {
													renderer.render(componentContext, CastUtilities.castUnchecked(component),
															EnumRenderStage.POST_CHILDREN, partialTicks);
													renderer.crop(componentContext, CastUtilities.castUnchecked(component),
															EnumCropStage.UN_CROP, cropMethod, partialTicks);
												});
												IUIComponentModifier.StaticHolder.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
														.forEachOrdered(modifier -> {
															assert modifier.getKey() != null;
															assert modifier.getValue() != null;
															EnumModifyStage.POST.advanceModifyStage(modifier.getKey());
															modifier.getValue().invokeRenderer(componentContext); // COMMENT post
															modifier.getKey().resetModifyStage();
														});
											},
											component -> component.isVisible() && component instanceof IUIComponentMinecraft);
									cropMethod.disable();
									return true;
								},
								new UIViewMinecraftBusEvent.Render(EnumHookStage.PRE, this, context, partialTicks),
								new UIViewMinecraftBusEvent.Render(EnumHookStage.POST, this, context, partialTicks))
				);
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void tick(IUIViewContext context) {
		getManager()
				.ifPresent(manager -> IUIViewComponent.StaticHolder.<RuntimeException>traverseComponentTreeDefault(createComponentContext(context),
						manager,
						(componentContext, result) -> {
							assert componentContext != null;
							assert result != null;
							CastUtilities.castChecked(IUIComponentMinecraft.class, result.getComponent())
									.ifPresent(cc ->
											IUIComponentMinecraftLifecycleModifier.StaticHolder.handleComponentModifiers(cc,
													result.getModifiersView(),
													componentContext,
													IUIComponentMinecraftLifecycleModifier::tick)
									);
						},
						IConsumer3.StaticHolder.getEmpty(),
						IUIEventTarget::isActive));
	}
}
