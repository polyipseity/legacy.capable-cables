package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration;

import com.google.common.collect.MapMaker;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractRegistryWithDefaults<K, V>
		extends AbstractRegistry<K, V> {
	private static final long serialVersionUID = -7624033031861891802L;

	protected AbstractRegistryWithDefaults(@SuppressWarnings("SameParameterValue") boolean overrideable, Logger logger, Consumer<? super MapMaker> configuration) {
		super(overrideable, logger, configuration);
	}

	@Override
	public Optional<? extends RegistryObject<? extends V>> get(K key) {
		Optional<? extends RegistryObject<? extends V>> ret = super.get(key);
		if (!ret.isPresent())
			ret = Optional.ofNullable(getDefaults().get(key));
		return ret;
	}

	protected abstract Map<K, RegistryObject<? extends V>> getDefaults();
}
