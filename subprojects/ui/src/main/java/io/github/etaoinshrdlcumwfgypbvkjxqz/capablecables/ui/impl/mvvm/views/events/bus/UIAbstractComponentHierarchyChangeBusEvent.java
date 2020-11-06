package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;

import java.util.Optional;

public abstract class UIAbstractComponentHierarchyChangeBusEvent<T, V>
		extends UIAbstractComponentChangeBusEvent<T, V> {
	protected UIAbstractComponentHierarchyChangeBusEvent(Class<T> genericType, EnumHookStage stage, IUIComponent component, V previous, V next) {
		super(genericType, stage, component, previous, next);
	}

	public static class Parent
			extends UIAbstractComponentChangeBusEvent<Void, Optional<IUIComponentContainer>> {
		public Parent(EnumHookStage stage, IUIComponent component,
		              @Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer current) {
			super(Void.class, stage, component, Optional.ofNullable(previous), Optional.ofNullable(current));
		}
	}

	public static class View
			extends UIAbstractComponentChangeBusEvent<Void, Optional<IUIViewComponent<?, ?>>> {
		public View(EnumHookStage stage, IUIComponent component,
		            @Nullable IUIViewComponent<?, ?> previous, @Nullable IUIViewComponent<?, ?> next) {
			super(Void.class, stage, component, Optional.ofNullable(previous), Optional.ofNullable(next));
		}
	}
}
