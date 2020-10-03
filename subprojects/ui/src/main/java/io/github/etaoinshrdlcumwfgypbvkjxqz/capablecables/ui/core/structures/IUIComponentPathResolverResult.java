package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;

import java.util.List;
import java.util.Optional;

public interface IUIComponentPathResolverResult
		extends ICopyable {
	Optional<? extends IUIComponent> getComponent();

	Optional<? extends IUIComponent> getConcreteComponent();

	List<? extends IUIVirtualComponent> getVirtualComponentsView();

	boolean isPresent();

	boolean isVirtual();

	@Override
	IUIComponentPathResolverResult copy();
}
