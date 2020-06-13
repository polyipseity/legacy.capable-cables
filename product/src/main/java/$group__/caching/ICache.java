package $group__.caching;

import java.util.function.Supplier;

public interface ICache<T> extends Supplier<T> {
	void invalidate();
}
