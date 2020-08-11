package $group__.client.ui.mvvm.core;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUICommon {
	Optional<IUIInfrastructure<?, ?, ?>> getInfrastructure();

	void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure);
}
