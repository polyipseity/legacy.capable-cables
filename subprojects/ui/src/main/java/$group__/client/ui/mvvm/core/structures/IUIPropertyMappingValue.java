package $group__.client.ui.mvvm.core.structures;

import java.util.Optional;

public interface IUIPropertyMappingValue {
	Optional<String> getDefaultValue();

	Optional<String> getBindingString();
}
