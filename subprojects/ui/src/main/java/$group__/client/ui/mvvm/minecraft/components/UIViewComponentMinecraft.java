package $group__.client.ui.mvvm.minecraft.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.minecraft.core.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewComponentMinecraft;
import $group__.client.ui.mvvm.minecraft.events.bus.EventUIViewMinecraft;
import $group__.client.ui.mvvm.views.components.UIViewComponent;
import $group__.utilities.CastUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import com.google.common.collect.ImmutableSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class UIViewComponentMinecraft<S extends Shape, M extends IUIComponentManager<S>>
		extends UIViewComponent<S, M>
		implements IUIViewComponentMinecraft<S, M> {
	public UIViewComponentMinecraft(M manager) { super(manager); }

	@Override
	public void render(Point2D cursorPosition, double partialTicks) {
		EventUtilities.callWithPrePostHooks(() -> {
					IAffineTransformStack stack = getCleanTransformStack();
					IUIComponentMinecraft.EnumCropMethod cropMethod = IUIComponentMinecraft.EnumCropMethod.getBestMethod();
					cropMethod.enable();
					TreeUtilities.<IUIComponent, IUIComponent>visitNodesDepthFirst(getManager(),
							Function.identity(),
							c -> {
								if (c.isVisible()) {
									CastUtilities.castChecked(IUIComponentMinecraft.class, c).ifPresent(cc -> {
										cc.crop(stack, cropMethod, true, cursorPosition, partialTicks);
										cc.render(stack, cursorPosition, partialTicks, true);
									});
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
								if (p.isVisible()) {
									stack.getDelegated().pop();
									CastUtilities.castChecked(IUIComponentMinecraft.class, p).ifPresent(pc -> {
										pc.render(stack, cursorPosition, partialTicks, false);
										pc.crop(stack, cropMethod, false, cursorPosition, partialTicks);
									});
								}
								return p;
							},
							r -> { throw new InternalError(); });
					cropMethod.disable();
					return true;
				},
				new EventUIViewMinecraft.Render(EnumEventHookStage.PRE, this, cursorPosition, partialTicks),
				new EventUIViewMinecraft.Render(EnumEventHookStage.POST, this, cursorPosition, partialTicks));
	}

	@Override
	public void initialize() {
		IAffineTransformStack stack = getCleanTransformStack();
		TreeUtilities.<IUIComponent, IUIComponent>visitNodesDepthFirst(getManager(),
				Function.identity(),
				c -> {
					CastUtilities.castChecked(IUIComponentMinecraft.class, c).ifPresent(cc ->
							cc.initialize(stack));
					stack.push();
					return CastUtilities.castChecked(IUIComponentContainer.class, c)
							.<Iterable<IUIComponent>>map(cp -> {
								cp.transformChildren(stack);
								return cp.getChildrenView();
							})
							.orElseGet(ImmutableSet::of);
				},
				(p, c) -> {
					stack.getDelegated().pop();
					return p;
				},
				r -> { throw new InternalError(); });
	}

	@Override
	public void tick() {
		IAffineTransformStack stack = getCleanTransformStack();
		TreeUtilities.<IUIComponent, IUIComponent>visitNodesDepthFirst(getManager(),
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
						stack.getDelegated().pop();
					return p;
				},
				r -> { throw new InternalError(); });
	}

	@Override
	public void removed() {
		IAffineTransformStack stack = getCleanTransformStack();
		TreeUtilities.<IUIComponent, IUIComponent>visitNodesDepthFirst(getManager(),
				Function.identity(),
				c -> {
					CastUtilities.castChecked(IUIComponentMinecraft.class, c).ifPresent(cc ->
							cc.removed(stack));
					stack.push();
					return CastUtilities.castChecked(IUIComponentContainer.class, c)
							.<Iterable<IUIComponent>>map(cp -> {
								cp.transformChildren(stack);
								return cp.getChildrenView();
							})
							.orElseGet(ImmutableSet::of);
				},
				(p, c) -> {
					stack.getDelegated().pop();
					return p;
				},
				r -> { throw new InternalError(); });
	}
}
