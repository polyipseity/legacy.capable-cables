package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.client.ui.mvvm.views.components.IUIComponentContainer;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentHierarchyChanged<T> extends EventUIComponentChanged<T> {
	protected EventUIComponentHierarchyChanged(EnumEventHookStage stage, IUIComponent component, T previous, T next) { super(stage, component, previous, next); }

	@OnlyIn(Dist.CLIENT)
	public static class Parent extends EventUIComponentChanged<Optional<IUIComponentContainer>> {
		public Parent(EnumEventHookStage stage, IUIComponent component,
		              @Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer current) {
			super(stage, component, Optional.ofNullable(previous), Optional.ofNullable(current));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Index extends EventUIComponentChanged<Integer> {
		public Index(EnumEventHookStage stage, IUIComponent component,
		             int previous, int current) {
			super(stage, component, previous, current);
		}
	}
}
