package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUISubInfrastructure<C extends IUISubInfrastructureContext> {
	Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure();

	void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure);

	void initialize();

	void removed();

	Optional<? extends C> getContext();

	void setContext(@Nullable C context);
}
