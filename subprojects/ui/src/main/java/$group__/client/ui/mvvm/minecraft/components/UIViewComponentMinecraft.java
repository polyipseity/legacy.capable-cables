package $group__.client.ui.mvvm.minecraft.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewComponentMinecraft;
import $group__.client.ui.mvvm.minecraft.events.bus.EventUIViewMinecraft;
import $group__.client.ui.mvvm.minecraft.extensions.UIExtensionMinecraft;
import $group__.client.ui.mvvm.views.components.UIViewComponent;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class UIViewComponentMinecraft<SD extends IShapeDescriptor<?, ?>, M extends IUIComponentManager<? extends SD>>
		extends UIViewComponent<SD, M>
		implements IUIViewComponentMinecraft<SD, M> {
	public UIViewComponentMinecraft(M manager) { super(manager); }

	@Override
	public void render(Point2D cursorPosition, double partialTicks) {
		IAffineTransformStack stack = getCleanTransformStack();
		EventUtilities.callWithPrePostHooks(() -> {
					UIExtensionMinecraft.TYPE.getValue().get(getManager()).ifPresent(e ->
							e.render(stack, cursorPosition, partialTicks));
					return true;
				},
				new EventUIViewMinecraft.Render(EnumEventHookStage.PRE, this, cursorPosition, partialTicks),
				new EventUIViewMinecraft.Render(EnumEventHookStage.POST, this, cursorPosition, partialTicks));
	}

	@Override
	public void tick() {
		IAffineTransformStack stack = getCleanTransformStack();
		UIExtensionMinecraft.TYPE.getValue().get(getManager()).ifPresent(e ->
				e.tick(stack));
	}

	@Override
	public void removed() {
		IAffineTransformStack stack = getCleanTransformStack();
		UIExtensionMinecraft.TYPE.getValue().get(getManager()).ifPresent(e ->
				e.removed(stack));
	}
}
