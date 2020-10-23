package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIVirtualComponent;

import java.util.List;
import java.util.Optional;

public interface IUIComponentPathResolverResult {
	Optional<? extends IUIComponent> getComponent();

	Optional<? extends IUIComponent> getConcreteComponent();

	List<? extends IUIVirtualComponent> getVirtualComponentsView();

	boolean isPresent();

	boolean isVirtual();
}
