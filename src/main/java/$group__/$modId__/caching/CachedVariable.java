package $group__.$modId__.caching;

import $group__.$modId__.concurrent.IImmutablizable;
import com.google.common.base.Suppliers;

import javax.annotation.concurrent.Immutable;
import java.util.function.Supplier;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

// COMMENT from https://stackoverflow.com/a/3636791/9341868, https://stackoverflow.com/a/3649392/9341868
@Immutable
public class CachedVariable<T extends CachedVariable<T, V>, V> implements ICache<V>, IImmutablizable<T> {
	/* SECTION variables */

	protected final Supplier<V> supplier;

	@SuppressWarnings("NotNullFieldNotInitialized")
	protected volatile Supplier<V> cache;


	/* SECTION constructors */

	public CachedVariable(Supplier<V> supplier) {
		this.supplier = supplier;
		invalidate();
	}


	/* SECTION getters & setters */

	@Override
	public V get() { return cache.get(); }


	public Supplier<V> getSupplier() { return supplier; }


	/* SECTION methods */

	@Override
	public void invalidate() { cache = Suppliers.memoize(supplier::get); }


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(this); }

	@Override
	public boolean isImmutable() { return true; }
}
