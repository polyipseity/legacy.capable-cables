package $group__.client.ui.mvvm.views.events.bus;

import $group__.client.ui.events.bus.EventUI;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.utilities.events.EnumEventHookStage;

public abstract class EventUIShapeDescriptor extends EventUI {
	protected final IShapeDescriptor<?, ?> shapeDescriptor;

	protected EventUIShapeDescriptor(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) {
		super(stage);
		this.shapeDescriptor = shapeDescriptor;
	}

	public IShapeDescriptor<?, ?> getShapeDescriptor() { return shapeDescriptor; }

	public static class Modify extends EventUIShapeDescriptor {
		public Modify(EnumEventHookStage stage, IShapeDescriptor<?, ?> shapeDescriptor) { super(stage, shapeDescriptor); }
	}
}
