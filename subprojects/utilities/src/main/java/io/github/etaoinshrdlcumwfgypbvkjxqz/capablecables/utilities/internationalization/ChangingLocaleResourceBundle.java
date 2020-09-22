package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.internationalization;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ChangingLocaleResourceBundle
		extends ResourceBundle {
	private static final CacheBuilder<Object, Object> BUNDLE_MAP_BUILDER = CacheUtilities.newCacheBuilderSingleThreaded()
			.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL)
			.expireAfterAccess(CacheUtilities.CACHE_EXPIRATION_ACCESS_DURATION, CacheUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT);
	protected final LoadingCache<Locale, ResourceBundle> bundleMap;
	protected final Supplier<? extends Locale> localeSupplier;

	public ChangingLocaleResourceBundle(CacheLoader<? super Locale, ResourceBundle> loader, Supplier<? extends Locale> localeSupplier) {
		this.bundleMap = BUNDLE_MAP_BUILDER.build(loader);
		this.localeSupplier = localeSupplier;
	}

	@Override
	public String getBaseBundleName() { return getCurrentBundle(this).getBaseBundleName(); }

	@Override
	public Locale getLocale() { return getCurrentBundle(this).getLocale(); }

	@Override
	protected Object handleGetObject(String key) { return getCurrentBundle(this).getObject(key); }

	@Override
	public Enumeration<String> getKeys() { return getCurrentBundle(this).getKeys(); }

	@Override
	public boolean containsKey(String key) { return getCurrentBundle(this).containsKey(key); }

	@Override
	public Set<String> keySet() { return getCurrentBundle(this).keySet(); }

	@Override
	protected Set<String> handleKeySet() { return getCurrentBundle(this).keySet(); }

	protected static ResourceBundle getCurrentBundle(ChangingLocaleResourceBundle self) { return self.getBundleMap().getUnchecked(AssertionUtilities.assertNonnull(self.getLocaleSupplier().get())); }

	protected LoadingCache<Locale, ResourceBundle> getBundleMap() { return bundleMap; }

	protected Supplier<? extends Locale> getLocaleSupplier() { return localeSupplier; }

	public static class Builder {
		private static final ConcurrentMap<String, ChangingLocaleResourceBundle> BASE_NAME_BUNDLE_MAP =
				MapUtilities.newMapMakerNormalThreaded()
						.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL)
						.weakValues()
						.makeMap();
		protected String baseName;
		protected BiFunction<? super String, ? super Locale, ? extends ResourceBundle> loader = ResourceBundle::getBundle;
		protected Supplier<? extends Locale> localeSupplier = Locale::getDefault;

		public Builder() { this.baseName = DynamicUtilities.getCallerClass().getName(); }

		public ChangingLocaleResourceBundle build() { return getBundle(getBaseName(), getLoader(), getLocaleSupplier()); }

		public static ChangingLocaleResourceBundle getBundle(String baseName, BiFunction<? super String, ? super Locale, ? extends ResourceBundle> loader, Supplier<? extends Locale> localeSupplier) {
			return BASE_NAME_BUNDLE_MAP.computeIfAbsent(baseName, key ->
					new ChangingLocaleResourceBundle(CacheLoader.from(locale ->
							loader.apply(key, locale)),
							localeSupplier));
		}

		protected String getBaseName() { return baseName; }

		public Builder setBaseName(Object baseName) {
			if (baseName instanceof Class)
				this.baseName = ((Class<?>) baseName).getName();
			else
				this.baseName = baseName.toString();
			return this;
		}

		protected BiFunction<? super String, ? super Locale, ? extends ResourceBundle> getLoader() { return loader; }

		public Builder setLoader(BiFunction<? super String, ? super Locale, ? extends ResourceBundle> loader) {
			this.loader = loader;
			return this;
		}

		protected Supplier<? extends Locale> getLocaleSupplier() { return localeSupplier; }

		public Builder setLocaleSupplier(Supplier<? extends Locale> localeSupplier) {
			this.localeSupplier = localeSupplier;
			return this;
		}
	}
}
