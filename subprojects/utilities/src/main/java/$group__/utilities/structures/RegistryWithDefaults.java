package $group__.utilities.structures;

import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

public abstract class RegistryWithDefaults<K, V>
		extends Registry<K, V> {
	protected RegistryWithDefaults(@SuppressWarnings("SameParameterValue") boolean overrideable, Logger logger) { super(overrideable, logger); }

	@Override
	public Optional<? extends RegistryObject<? extends V>> get(K key) {
		Optional<? extends RegistryObject<? extends V>> ret = super.get(key);
		if (!ret.isPresent())
			ret = Optional.ofNullable(getDefaults().get(key));
		return ret;
	}

	protected abstract Map<K, RegistryObject<? extends V>> getDefaults();
}
