package $group__.ui.mvvm.views.events.bus;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.utilities.events.EnumEventHookStage;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class EventUIComponentHierarchyChanged<T> extends EventUIComponentChanged<T> {
	protected EventUIComponentHierarchyChanged(EnumEventHookStage stage, IUIComponent component, T previous, T next) { super(stage, component, previous, next); }

	public static class Parent extends EventUIComponentChanged<Optional<IUIComponentContainer>> {
		public Parent(EnumEventHookStage stage, IUIComponent component,
		              @Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer current) {
			super(stage, component, Optional.ofNullable(previous), Optional.ofNullable(current));
		}
	}

	public static class Index extends EventUIComponentChanged<Integer> {
		public Index(EnumEventHookStage stage, IUIComponent component,
		             int previous, int current) {
			super(stage, component, previous, current);
		}
	}
}
