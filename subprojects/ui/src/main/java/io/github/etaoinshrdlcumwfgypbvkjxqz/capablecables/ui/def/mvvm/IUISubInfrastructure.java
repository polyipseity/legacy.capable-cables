package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUIActiveLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUIStructureLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;

import java.util.Optional;

public interface IUISubInfrastructure<C extends IUISubInfrastructureContext>
		extends
		IUIStructureLifecycle<IUIStructureLifecycleContext, @AlwaysNull @Nullable Void>, IUIActiveLifecycle<@AlwaysNull @Nullable Void, @AlwaysNull @Nullable Void>,
		IHasBinding, IExtensionContainer<IIdentifier> {
	Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure();

	void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure);

	Optional<? extends C> getContext();

	void setContext(@Nullable C context);
}
