package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.EnumCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft.EnumCropStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft.EnumRenderStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions.DefaultUIExtensionMinecraftBackground;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;
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
	public void render(Point2D cursorPosition, double partialTicks) {
		getManager()
				.ifPresent(manager ->
						EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
									EnumCropMethod cropMethod = EnumCropMethod.getBestMethod();
									cropMethod.enable();
									IUIViewComponent.StaticHolder.traverseComponentTreeDefault(createComponentContext(),
											manager,
											(context, component) -> {
												assert component instanceof IUIComponentMinecraft;
												IUIComponentMinecraft componentMinecraft = (IUIComponentMinecraft) component;
												componentMinecraft.getRenderer()
														.ifPresent(renderer -> {
															renderer.crop(context, CastUtilities.castUnchecked(component), // COMMENT component should contain a renderer that accepts itself
																	EnumCropStage.CROP, cropMethod, partialTicks);
															renderer.render(context, CastUtilities.castUnchecked(component), // COMMENT component should contain a renderer that accepts itself
																	EnumRenderStage.PRE_CHILDREN, partialTicks);
														});
											},
											(context, component, children) -> {
												assert component instanceof IUIComponentMinecraft;
												IUIComponentMinecraft componentMinecraft = (IUIComponentMinecraft) component;
												componentMinecraft.getRenderer()
														.ifPresent(renderer -> {
															renderer.render(context, CastUtilities.castUnchecked(component), // COMMENT component should contain a renderer that accepts itself
																	EnumRenderStage.POST_CHILDREN, partialTicks);
															renderer.crop(context, CastUtilities.castUnchecked(component), // COMMENT component should contain a renderer that accepts itself
																	EnumCropStage.UN_CROP, cropMethod, partialTicks);
														});
											},
											component -> component.isVisible() & component instanceof IUIComponentMinecraft);
									cropMethod.disable();
									return true;
								},
								new UIViewMinecraftBusEvent.Render(EnumHookStage.PRE, this, cursorPosition, partialTicks),
								new UIViewMinecraftBusEvent.Render(EnumHookStage.POST, this, cursorPosition, partialTicks))
				);
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void tick() {
		getManager()
				.ifPresent(manager -> IUIViewComponent.StaticHolder.<RuntimeException>traverseComponentTreeDefault(createComponentContext(),
						manager,
						(context, component) -> CastUtilities.castChecked(IUIComponentMinecraft.class, component).ifPresent(cc ->
								cc.tick(context)),
						IConsumer3.StaticHolder.empty(),
						IUIEventTarget::isActive));
	}
}
