package $group__.ui.minecraft.mvvm.events.bus;

import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.mvvm.views.events.bus.UIViewBusEvent;
import $group__.ui.structures.ImmutablePoint2D;
import $group__.utilities.events.EnumHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public abstract class UIViewMinecraftBusEvent
		extends UIViewBusEvent {
	protected UIViewMinecraftBusEvent(EnumHookStage stage, IUIView<?> view) {
		super(stage, view);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Render extends UIViewMinecraftBusEvent {
		protected final ImmutablePoint2D cursorPosition;
		protected final double partialTicks;

		public Render(EnumHookStage stage, IUIView<?> view, Point2D cursorPosition, double partialTicks) {
			super(stage, view);
			this.cursorPosition = ImmutablePoint2D.copyOf(cursorPosition);
			this.partialTicks = partialTicks;
		}

		public ImmutablePoint2D getCursorPositionView() { return cursorPosition; }

		public double getPartialTicks() { return partialTicks; }

		@Override
		public boolean isCancelable() { return getStage().isPre(); }

		@Override
		public boolean hasResult() { return getStage().isPre(); }
	}
}
