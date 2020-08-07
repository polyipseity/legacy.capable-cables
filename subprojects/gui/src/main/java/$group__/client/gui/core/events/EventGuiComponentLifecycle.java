package $group__.client.gui.core.events;

import $group__.client.gui.core.IGuiComponent;
import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventGuiComponentLifecycle extends EventGuiComponent.Outbound {
	protected final AffineTransformStack stack;

	protected EventGuiComponentLifecycle(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack) {
		super(stage, component);
		this.stack = stack;
	}

	public AffineTransformStack getStack() { return stack; }

	@OnlyIn(Dist.CLIENT)
	public static class Initialize extends EventGuiComponentLifecycle {
		public Initialize(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack) { super(stage, component, stack); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Tick extends EventGuiComponentLifecycle {
		public Tick(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack) { super(stage, component, stack); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Close extends EventGuiComponentLifecycle {
		public Close(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack) { super(stage, component, stack); }
	}
}
