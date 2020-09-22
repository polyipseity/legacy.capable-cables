package $group__.ui.minecraft.mvvm.components;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.parsers.components.UIViewComponentConstructor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.minecraft.core.mvvm.views.EnumCropMethod;
import $group__.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft.EnumCropStage;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft.EnumRenderStage;
import $group__.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import $group__.ui.mvvm.views.components.UIViewComponent;
import $group__.utilities.CastUtilities;
import $group__.utilities.events.EnumHookStage;
import $group__.utilities.events.EventBusUtilities;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.structures.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class UIViewComponentMinecraft<S extends Shape, M extends IUIComponentManager<S>>
		extends UIViewComponent<S, M>
		implements IUIViewComponentMinecraft<S, M> {

	@UIViewComponentConstructor(type = UIViewComponentConstructor.EnumConstructorType.MAPPINGS)
	public UIViewComponentMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> manager) { super(manager); }

	@Override
	public void render(Point2D cursorPosition, double partialTicks) {
		getManager()
				.ifPresent(manager ->
						EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
									EnumCropMethod cropMethod = EnumCropMethod.getBestMethod();
									cropMethod.enable();
									IUIViewComponent.StaticHolder.traverseComponentTreeDefault(createContext(),
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
				.ifPresent(manager -> IUIViewComponent.StaticHolder.<RuntimeException>traverseComponentTreeDefault(createContext(),
						manager,
						(context, component) -> CastUtilities.castChecked(IUIComponentMinecraft.class, component).ifPresent(cc ->
								cc.tick(context)),
						IConsumer3.StaticHolder.empty(),
						IUIEventTarget::isActive));
	}
}
