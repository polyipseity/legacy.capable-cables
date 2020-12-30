package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.naming.INamedTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class LoadingNamedTrackers
		extends AbstractNamedTrackers {
	private final LoadingCache<Class<? extends INamed>, INamedTracker<?>> data;

	public LoadingNamedTrackers(CacheLoader<? super Class<? extends INamed>, INamedTracker<?>> loader) {
		this.data = new ManualLoadingCache<>(
				CacheUtilities.newCacheBuilderNormalThreaded()
						.weakKeys()
						.initialCapacity(CapacityUtilities.getInitialCapacitySmall())
						.build(loader),
				cache -> cache.asMap().values()
						.removeIf(INamedTracker::isEmpty)
		);
	}

	@Override
	protected LoadingCache<Class<? extends INamed>, INamedTracker<?>> getData() { return data; }
}
