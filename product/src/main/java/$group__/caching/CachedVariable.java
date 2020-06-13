package $group__.caching;

import com.google.common.base.Suppliers;

import javax.annotation.concurrent.Immutable;
import java.util.function.Supplier;

// COMMENT from https://stackoverflow.com/a/3636791/9341868, https://stackoverflow.com/a/3649392/9341868
@Immutable
public final class CachedVariable<T extends CachedVariable<T, V>, V> implements ICache<V> {
	protected final Supplier<V> supplier;
	@SuppressWarnings("NotNullFieldNotInitialized")
	private volatile Supplier<V> cache;


	public CachedVariable(Supplier<V> supplier) {
		this.supplier = supplier;
		invalidate();
	}


	@Override
	public V get() { return cache.get(); }

	public Supplier<V> getSupplier() { return supplier; }


	@Override
	public void invalidate() { cache = Suppliers.memoize(supplier::get); }
}
