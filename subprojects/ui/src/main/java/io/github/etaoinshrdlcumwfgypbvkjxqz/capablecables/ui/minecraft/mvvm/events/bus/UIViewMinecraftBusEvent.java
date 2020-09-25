package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
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
		private final Point2D cursorPosition;
		private final double partialTicks;

		public Render(EnumHookStage stage, IUIView<?> view, Point2D cursorPosition, double partialTicks) {
			super(stage, view);
			this.cursorPosition = (Point2D) cursorPosition.clone();
			this.partialTicks = partialTicks;
		}

		public Point2D getCursorPositionView() { return (Point2D) getCursorPosition().clone(); }

		protected Point2D getCursorPosition() { return cursorPosition; }

		public double getPartialTicks() { return partialTicks; }

		@Override
		public boolean isCancelable() { return getStage().isPre(); }

		@Override
		public boolean hasResult() { return getStage().isPre(); }
	}
}
