package $group__.client.gui.core.events;

import $group__.client.gui.core.IGuiComponent;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public abstract class EventGuiComponentHierarchyChanged<T> extends EventGuiComponentChanged<T> {
	protected EventGuiComponentHierarchyChanged(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, T previous, T next) { super(stage, component, previous, next); }

	@OnlyIn(Dist.CLIENT)
	public static class Parent extends EventGuiComponentChanged<Optional<IGuiComponent.IContainer<?, ?, ?, ?>>> {
		public Parent(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component,
		              @Nullable IGuiComponent.IContainer<?, ?, ?, ?> previous, @Nullable IGuiComponent.IContainer<?, ?, ?, ?> current) {
			super(stage, component, Optional.ofNullable(previous), Optional.ofNullable(current));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Index extends EventGuiComponentChanged<Integer> {
		public Index(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component,
		             int previous, int current) {
			super(stage, component, previous, current);
		}
	}
}
