package $group__.utilities;

import $group__.utilities.collections.CacheUtilities;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public enum CleanerUtilities {
	;

	private static final LoadingCache<Object, Object> CLEANER_REFERENT_MAP =
			CacheUtilities.newCacheBuilderNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_ENORMOUS).weakKeys()
					.build(CacheLoader.from(Object::new));

	public static Object getCleanerReferent(Object object) { return getCleanerReferentMap().getUnchecked(object); }

	@SuppressWarnings("SameReturnValue")
	private static LoadingCache<Object, Object> getCleanerReferentMap() { return CLEANER_REFERENT_MAP; }
}
