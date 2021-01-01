package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;

import java.util.Optional;

public abstract class UIAbstractComponentHierarchyChangeBusEvent<T, V>
		extends UIAbstractComponentChangeBusEvent<T, V> {
	protected UIAbstractComponentHierarchyChangeBusEvent(Class<T> genericType, EnumHookStage stage, IUIComponent component, V previous, V next) {
		super(genericType, stage, component, previous, next);
	}

	public static class Parent
			extends UIAbstractComponentChangeBusEvent<Void, Optional<IUIComponent>> {
		public Parent(EnumHookStage stage, IUIComponent component,
		              @Nullable IUIComponent previous, @Nullable IUIComponent current) {
			super(Void.class, stage, component, Optional.ofNullable(previous), Optional.ofNullable(current));
		}
	}

	public static class View
			extends UIAbstractComponentChangeBusEvent<Void, Optional<IUIComponentView<?, ?>>> {
		public View(EnumHookStage stage, IUIComponent component,
		            @Nullable IUIComponentView<?, ?> previous, @Nullable IUIComponentView<?, ?> next) {
			super(Void.class, stage, component, Optional.ofNullable(previous), Optional.ofNullable(next));
		}
	}
}
