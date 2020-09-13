package $group__.ui.minecraft.mvvm.events.bus;

import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.mvvm.views.events.bus.EventUIView;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIViewMinecraft
		extends EventUIView {
	protected EventUIViewMinecraft(EnumEventHookStage stage, IUIView<?> view) {
		super(stage, view);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Render extends EventUIViewMinecraft {
		protected final Point2D cursorPosition;
		protected final double partialTicks;

		public Render(EnumEventHookStage stage, IUIView<?> view, Point2D cursorPosition, double partialTicks) {
			super(stage, view);
			this.cursorPosition = cursorPosition;
			this.partialTicks = partialTicks;
		}

		public Point2D getCursorPositionView() { return (Point2D) cursorPosition.clone(); }

		public double getPartialTicks() { return partialTicks; }

		@Override
		public boolean isCancelable() { return getStage().isPre(); }

		@Override
		public boolean hasResult() { return getStage().isPre(); }
	}
}
