package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentStateChanged<T> extends EventUIComponentChanged<T> {
	protected final AffineTransformStack stack;

	protected EventUIComponentStateChanged(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack, T previous, T next) {
		super(stage, component, previous, next);
		this.stack = stack;
	}

	public AffineTransformStack getStack() { return stack; }

	@OnlyIn(Dist.CLIENT)
	public static class Visible extends EventUIComponentStateChanged<Boolean> {
		public Visible(EnumEventHookStage stage, IUIComponentDOMLike component,
		               AffineTransformStack stack,
		               boolean previous, boolean current) {
			super(stage, component, stack, previous, current);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Active extends EventUIComponentStateChanged<Boolean> {
		public Active(EnumEventHookStage stage, IUIComponentDOMLike component,
		              AffineTransformStack stack,
		              boolean previous, boolean current) {
			super(stage, component, stack, previous, current);
		}
	}
}
