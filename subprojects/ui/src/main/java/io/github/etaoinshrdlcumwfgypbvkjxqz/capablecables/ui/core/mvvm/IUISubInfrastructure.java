package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUISubInfrastructure<C extends IUISubInfrastructureContext> {
	Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure();

	void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure);

	void initialize(C context);

	void removed(C context);
}
