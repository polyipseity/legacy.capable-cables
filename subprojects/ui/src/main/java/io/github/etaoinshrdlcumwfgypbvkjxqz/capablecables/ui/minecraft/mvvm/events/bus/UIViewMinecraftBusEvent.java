package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class UIViewMinecraftBusEvent
		extends UIViewBusEvent {
	protected UIViewMinecraftBusEvent(EnumHookStage stage, IUIView<?> view) {
		super(stage, view);
	}

	// TODO replace this
	@OnlyIn(Dist.CLIENT)
	public static class Render extends UIViewMinecraftBusEvent {
		private final IUIViewContext context;
		private final double partialTicks;

		public Render(EnumHookStage stage, IUIView<?> view, IUIViewContext context, double partialTicks) {
			super(stage, view);
			this.context = context;
			this.partialTicks = partialTicks;
		}

		public IUIViewContext getContextView() { return getContext(); }

		protected IUIViewContext getContext() { return context; }

		public double getPartialTicks() { return partialTicks; }

		@Override
		public boolean isCancelable() { return getStage().isPre(); }

		@Override
		public boolean hasResult() { return getStage().isPre(); }
	}
}
