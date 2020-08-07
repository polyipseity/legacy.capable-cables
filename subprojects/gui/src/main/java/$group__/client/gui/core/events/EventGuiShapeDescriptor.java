package $group__.client.gui.core.events;

import $group__.client.gui.core.structures.IShapeDescriptor;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventGuiShapeDescriptor extends EventGui {
	protected final IShapeDescriptor<?, ?> shapeDescriptor;

	protected EventGuiShapeDescriptor(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) {
		super(stage);
		this.shapeDescriptor = shapeDescriptor;
	}

	public IShapeDescriptor<?, ?> getShapeDescriptor() { return shapeDescriptor; }

	@OnlyIn(Dist.CLIENT)
	public static abstract class Inbound extends EventGuiShapeDescriptor {
		protected Inbound(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Outbound extends EventGuiShapeDescriptor {
		protected Outbound(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Modify extends Outbound {
		public Modify(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}
}
