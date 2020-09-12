package $group__.ui.minecraft.mvvm.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.components.UIViewComponentConstructor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.minecraft.core.mvvm.views.EnumCropMethod;
import $group__.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft.EnumCropStage;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft.EnumRenderStage;
import $group__.ui.minecraft.mvvm.events.bus.EventUIViewMinecraft;
import $group__.ui.mvvm.views.components.UIViewComponent;
import $group__.utilities.CastUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventBusUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.ImmutableSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.function.Function;

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
									IAffineTransformStack stack = getCleanTransformStack();
									EnumCropMethod cropMethod = EnumCropMethod.getBestMethod();
									cropMethod.enable();
									TreeUtilities.<IUIComponent, IUIComponent>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, manager,
											Function.identity(),
											c -> {
												if (c.isVisible()) {
													return CastUtilities.castChecked(IUIComponentMinecraft.class, c)
															.flatMap(IUIComponentRendererContainer::getRenderer)
															.map(ccr -> {
																ccr.crop(CastUtilities.castUnchecked(c), // COMMENT component should contain a renderer that accepts itself
																		EnumCropStage.CROP, stack, cropMethod, cursorPosition, partialTicks);
																ccr.render(CastUtilities.castUnchecked(c), // COMMENT component should contain a renderer that accepts itself
																		EnumRenderStage.PRE_CHILDREN, stack, cursorPosition, partialTicks);
																stack.push();
																return CastUtilities.castChecked(IUIComponentContainer.class, c)
																		.<Iterable<IUIComponent>>map(cp -> {
																			cp.transformChildren(stack);
																			return cp.getChildrenView();
																		})
																		.orElseGet(ImmutableSet::of);
															})
															.orElseGet(ImmutableSet::of);
												}
												return ImmutableSet.of();
											},
											(p, c) -> {
												if (p.isVisible()) {
													CastUtilities.castChecked(IUIComponentMinecraft.class, p)
															.flatMap(IUIComponentRendererContainer::getRenderer)
															.ifPresent(pc -> {
																stack.pop();
																pc.render(CastUtilities.castUnchecked(p), // COMMENT component should contain a renderer that accepts itself
																		EnumRenderStage.POST_CHILDREN, stack, cursorPosition, partialTicks);
																pc.crop(CastUtilities.castUnchecked(p), // COMMENT component should contain a renderer that accepts itself
																		EnumCropStage.UN_CROP, stack, cropMethod, cursorPosition, partialTicks);
															});
												}
												return p;
											},
											r -> { throw new InternalError(); });
									cropMethod.disable();
									return true;
								},
								new EventUIViewMinecraft.Render(EnumEventHookStage.PRE, this, cursorPosition, partialTicks),
								new EventUIViewMinecraft.Render(EnumEventHookStage.POST, this, cursorPosition, partialTicks))
				);
	}

	@Override
	public void tick() {
		getManager()
				.ifPresent(manager -> {
					IAffineTransformStack stack = getCleanTransformStack();
					TreeUtilities.<IUIComponent, IUIComponent>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, manager,
							Function.identity(),
							c -> {
								if (c.isActive()) {
									CastUtilities.castChecked(IUIComponentMinecraft.class, c).ifPresent(cc ->
											cc.tick(stack));
									stack.push();
									return CastUtilities.castChecked(IUIComponentContainer.class, c)
											.<Iterable<IUIComponent>>map(cp -> {
												cp.transformChildren(stack);
												return cp.getChildrenView();
											})
											.orElseGet(ImmutableSet::of);
								}
								return ImmutableSet.of();
							},
							(p, c) -> {
								if (p.isActive())
									stack.pop();
								return p;
							},
							r -> { throw new InternalError(); });
				});
	}
}
