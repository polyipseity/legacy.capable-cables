package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUISubInfrastructure<C extends IUISubInfrastructureContext>
		extends IHasBinding, IExtensionContainer<INamespacePrefixedString> {
	Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure();

	void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure);

	void initialize();

	void removed();

	Optional<? extends C> getContext();

	void setContext(@Nullable C context);
}
