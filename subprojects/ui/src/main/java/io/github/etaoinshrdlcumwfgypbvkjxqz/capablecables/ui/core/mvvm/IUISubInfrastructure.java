package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUISubInfrastructure {
	Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure();

	void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure);

	void initialize();

	void removed();
}
