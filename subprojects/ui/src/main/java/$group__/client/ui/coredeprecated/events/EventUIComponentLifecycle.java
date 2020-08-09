package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentLifecycle extends EventUIComponent.Outbound {
	protected final AffineTransformStack stack;

	protected EventUIComponentLifecycle(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack) {
		super(stage, component);
		this.stack = stack;
	}

	public AffineTransformStack getStack() { return stack; }

	@OnlyIn(Dist.CLIENT)
	public static class Initialize extends EventUIComponentLifecycle {
		public Initialize(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack) { super(stage, component, stack); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Tick extends EventUIComponentLifecycle {
		public Tick(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack) { super(stage, component, stack); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Close extends EventUIComponentLifecycle {
		public Close(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack) { super(stage, component, stack); }
	}
}
