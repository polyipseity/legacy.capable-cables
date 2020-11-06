package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.internationalization;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.StackTraceUtilities;

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
			.initialCapacity(CapacityUtilities.getInitialCapacitySmall())
			.expireAfterAccess(CacheUtilities.getCacheExpirationAccessDuration(), CacheUtilities.getCacheExpirationAccessTimeUnit());
	private final LoadingCache<Locale, ResourceBundle> bundleMap;
	private final Supplier<@Nonnull ? extends Locale> localeSupplier;

	public ChangingLocaleResourceBundle(CacheLoader<? super Locale, ResourceBundle> loader, Supplier<@Nonnull ? extends Locale> localeSupplier) {
		this.bundleMap = getBundleMapBuilder().build(loader);
		this.localeSupplier = localeSupplier;
	}

	protected static CacheBuilder<Object, Object> getBundleMapBuilder() {
		return BUNDLE_MAP_BUILDER;
	}

	@Override
	public String getBaseBundleName() { return getCurrentBundle(this).getBaseBundleName(); }

	@Override
	public Locale getLocale() { return getCurrentBundle(this).getLocale(); }

	@Override
	protected Object handleGetObject(String key) { return getCurrentBundle(this).getObject(key); }

	@Override
	public @Nonnull Enumeration<String> getKeys() { return getCurrentBundle(this).getKeys(); }

	@Override
	public boolean containsKey(String key) { return getCurrentBundle(this).containsKey(key); }

	@Override
	public @Nonnull Set<String> keySet() { return getCurrentBundle(this).keySet(); }

	@Override
	protected @Nonnull Set<String> handleKeySet() { return getCurrentBundle(this).keySet(); }

	protected static ResourceBundle getCurrentBundle(ChangingLocaleResourceBundle self) { return self.getBundleMap().getUnchecked(self.getLocaleSupplier().get()); }

	protected LoadingCache<Locale, ResourceBundle> getBundleMap() { return bundleMap; }

	protected Supplier<@Nonnull ? extends Locale> getLocaleSupplier() { return localeSupplier; }

	public static class Builder {
		private static final ConcurrentMap<String, ChangingLocaleResourceBundle> BASE_NAME_BUNDLE_MAP =
				MapBuilderUtilities.newMapMakerNormalThreaded()
						.initialCapacity(CapacityUtilities.getInitialCapacitySmall())
						.weakValues()
						.makeMap();
		private String baseName;
		private BiFunction<@Nonnull ? super String, @Nonnull ? super Locale, @Nonnull ? extends ResourceBundle> loader = ResourceBundle::getBundle;
		private Supplier<@Nonnull ? extends Locale> localeSupplier = Locale::getDefault;

		public Builder() { this.baseName = StackTraceUtilities.getCallerClass().getName(); }

		public ChangingLocaleResourceBundle build() { return getBundle(getBaseName(), getLoader(), getLocaleSupplier()); }

		public static ChangingLocaleResourceBundle getBundle(CharSequence baseName, BiFunction<@Nonnull ? super String, @Nonnull ? super Locale, @Nonnull ? extends ResourceBundle> loader, Supplier<@Nonnull ? extends Locale> localeSupplier) {
			return BASE_NAME_BUNDLE_MAP.computeIfAbsent(baseName.toString(), key ->
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

		protected BiFunction<@Nonnull ? super String, @Nonnull ? super Locale, @Nonnull ? extends ResourceBundle> getLoader() { return loader; }

		public Builder setLoader(BiFunction<@Nonnull ? super String, @Nonnull ? super Locale, @Nonnull ? extends ResourceBundle> loader) {
			this.loader = loader;
			return this;
		}

		protected Supplier<@Nonnull ? extends Locale> getLocaleSupplier() { return localeSupplier; }

		public Builder setLocaleSupplier(Supplier<@Nonnull ? extends Locale> localeSupplier) {
			this.localeSupplier = localeSupplier;
			return this;
		}
	}
}
