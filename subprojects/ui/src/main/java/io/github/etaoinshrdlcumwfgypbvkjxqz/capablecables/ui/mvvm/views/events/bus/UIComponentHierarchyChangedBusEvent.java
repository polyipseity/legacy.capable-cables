package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class UIComponentHierarchyChangedBusEvent<T> extends UIComponentChangedBusEvent<T> {
	protected UIComponentHierarchyChangedBusEvent(EnumHookStage stage, IUIComponent component, T previous, T next) { super(stage, component, previous, next); }

	public static class Parent extends UIComponentChangedBusEvent<Optional<IUIComponentContainer>> {
		public Parent(EnumHookStage stage, IUIComponent component,
		              @Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer current) {
			super(stage, component, Optional.ofNullable(previous), Optional.ofNullable(current));
		}
	}

	public static class View extends UIComponentChangedBusEvent<Optional<IUIViewComponent<?, ?>>> {
		public View(EnumHookStage stage, IUIComponent component,
		            @Nullable IUIViewComponent<?, ?> previous, @Nullable IUIViewComponent<?, ?> next) {
			super(stage, component, Optional.ofNullable(previous), Optional.ofNullable(next));
		}
	}
}
