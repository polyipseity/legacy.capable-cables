package $group__.client.gui.core.events;

import $group__.client.gui.core.IGuiComponent;
import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventGuiComponentStateChanged<T> extends EventGuiComponentChanged<T> {
	protected final AffineTransformStack stack;

	protected EventGuiComponentStateChanged(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack, T previous, T next) {
		super(stage, component, previous, next);
		this.stack = stack;
	}

	public AffineTransformStack getStack() { return stack; }

	@OnlyIn(Dist.CLIENT)
	public static class Visible extends EventGuiComponentStateChanged<Boolean> {
		public Visible(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component,
		               AffineTransformStack stack,
		               boolean previous, boolean current) {
			super(stage, component, stack, previous, current);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Active extends EventGuiComponentStateChanged<Boolean> {
		public Active(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component,
		              AffineTransformStack stack,
		              boolean previous, boolean current) {
			super(stage, component, stack, previous, current);
		}
	}
}
