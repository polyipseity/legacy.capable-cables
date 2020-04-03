package $group__.$modId__.caching;

import java.util.function.Supplier;

public interface ICache<T> extends Supplier<T> {
	/* SECTION methods */

	void invalidate();
}
