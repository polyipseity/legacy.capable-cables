package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class UIAbstractSubInfrastructure<C extends IUISubInfrastructureContext>
		implements IUISubInfrastructure<C> {
	private OptionalWeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = new OptionalWeakReference<>(null);
	@Nullable
	private C context;

	@Override
	public Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure() {
		return infrastructure.getOptional();
	}

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) {
		this.infrastructure = new OptionalWeakReference<>(infrastructure);
	}

	@Override
	public Optional<? extends C> getContext() {
		return Optional.ofNullable(context);
	}

	@Override
	public void setContext(@Nullable C context) {
		this.context = context;
	}
}
