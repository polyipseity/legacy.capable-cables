package $group__.client.ui.core.mvvm;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IUICommon {
	Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure();

	void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure);
}
