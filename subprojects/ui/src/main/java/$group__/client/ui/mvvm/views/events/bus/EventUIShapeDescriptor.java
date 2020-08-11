package $group__.client.ui.mvvm.views.events.bus;

import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.utilities.events.EnumEventHookStage;

public abstract class EventUIShapeDescriptor extends EventUI {
	protected final IShapeDescriptor<?, ?> shapeDescriptor;

	protected EventUIShapeDescriptor(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) {
		super(stage);
		this.shapeDescriptor = shapeDescriptor;
	}

	public IShapeDescriptor<?, ?> getShapeDescriptor() { return shapeDescriptor; }

	public static abstract class Inbound extends EventUIShapeDescriptor {
		protected Inbound(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}

	public static abstract class Outbound extends EventUIShapeDescriptor {
		protected Outbound(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}

	public static class Modify extends Outbound {
		public Modify(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}
}
