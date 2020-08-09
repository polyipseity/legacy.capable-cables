package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentView extends EventUIComponent.Outbound {
	protected final AffineTransformStack stack;

	protected EventUIComponentView(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack) {
		super(stage, component);
		this.stack = stack;
	}

	public AffineTransformStack getStack() { return stack; }

	@OnlyIn(Dist.CLIENT)
	public static class Render extends EventUIComponentView {
		protected final Point2D cursor;
		protected final float partialTicks;

		public Render(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack, Point2D cursor, float partialTicks) {
			super(stage, component, stack);
			this.cursor = (Point2D) cursor.clone();
			this.partialTicks = partialTicks;
		}

		public Point2D getCursorView() { return (Point2D) cursor.clone(); }

		public float getPartialTicks() { return partialTicks; }
	}
}
