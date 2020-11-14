package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.ui.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.Predicate;

public class UIDefaultComponentManager<S extends Shape>
		extends UIDefaultComponent
		implements IUIComponentManager<S> {
	private OptionalWeakReference<IUIViewComponent<?, ?>> view = new OptionalWeakReference<>(null);

	@UIComponentConstructor
	public UIDefaultComponentManager(UIComponentConstructor.IArguments arguments) { super(arguments); }

	@Override
	public boolean reshape(Predicate<@Nonnull ? super IShapeDescriptor<? super S>> action) throws ConcurrentModificationException { return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action); }

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<S> getShapeDescriptor() {
		return (IShapeDescriptor<S>) super.getShapeDescriptor(); // COMMENT should be safe
	}

	@Override
	public boolean isFocusable() { return true; }

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public Optional<? extends IUIViewComponent<?, ?>> getView() {
		@Nullable Optional<? extends IUIViewComponent<?, ?>> ret;
		if ((ret = view.getOptional()).isPresent())
			return ret;
		return getParent()
				.flatMap(IUIComponent::getManager)
				.flatMap(IUIComponentManager::getView);
	}

	@Override
	public void setView(@Nullable IUIViewComponent<?, ?> view) { this.view = new OptionalWeakReference<>(view); }
}
