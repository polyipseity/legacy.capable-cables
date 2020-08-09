package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIShapeDescriptor extends EventUI {
	protected final IShapeDescriptor<?, ?> shapeDescriptor;

	protected EventUIShapeDescriptor(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) {
		super(stage);
		this.shapeDescriptor = shapeDescriptor;
	}

	public IShapeDescriptor<?, ?> getShapeDescriptor() { return shapeDescriptor; }

	@OnlyIn(Dist.CLIENT)
	public static abstract class Inbound extends EventUIShapeDescriptor {
		protected Inbound(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Outbound extends EventUIShapeDescriptor {
		protected Outbound(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Modify extends Outbound {
		public Modify(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}
}
