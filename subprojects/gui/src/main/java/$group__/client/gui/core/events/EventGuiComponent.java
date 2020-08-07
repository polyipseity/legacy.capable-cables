package $group__.client.gui.core.events;

import $group__.client.gui.core.IGuiComponent;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventGuiComponent extends EventGui {
	protected final IGuiComponent<?, ?, ?> component;

	protected EventGuiComponent(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component) {
		super(stage);
		this.component = component;
	}

	public IGuiComponent<?, ?, ?> getComponent() { return component; }

	@OnlyIn(Dist.CLIENT)
	public static abstract class Inbound extends EventGuiComponent {
		protected Inbound(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component) { super(stage, component); }
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Outbound extends EventGuiComponent {
		protected Outbound(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component) { super(stage, component); }
	}
}
