package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
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
			this.cursorPosition = ImmutablePoint2D.of(cursorPosition);
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
