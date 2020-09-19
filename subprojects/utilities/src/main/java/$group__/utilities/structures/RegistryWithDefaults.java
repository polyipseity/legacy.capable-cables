package $group__.utilities.structures;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

public abstract class RegistryWithDefaults<K, V>
		extends Registry<K, V> {
	private final ImmutableMap<K, RegistryObject<? extends V>> defaults;

	protected RegistryWithDefaults(boolean overrideable, Map<? extends K, ? extends RegistryObject<? extends V>> defaults, Logger logger) {
		super(overrideable, logger);
		this.defaults = ImmutableMap.copyOf(defaults);
	}

	@Override
	public Optional<? extends RegistryObject<? extends V>> get(K key) {
		Optional<? extends RegistryObject<? extends V>> ret = super.get(key);
		if (!ret.isPresent())
			ret = Optional.ofNullable(getDefaults().get(key));
		return ret;
	}

	protected ImmutableMap<K, RegistryObject<? extends V>> getDefaults() { return defaults; }
}
