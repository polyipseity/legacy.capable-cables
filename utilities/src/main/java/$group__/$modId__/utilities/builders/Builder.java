package $group__.$modId__.utilities.builders;

import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

public class Builder<T extends Builder<T, V>, V> implements IBuilder<V> {
	/* SECTION variables */

	public final Function<? super T, ? extends V> constructor;


	/* SECTION constructors */

	public Builder(Function<? super T, ? extends V> constructor) { this.constructor = constructor; }

	public Builder(Builder<T, V> copy) { this(copy.constructor); }


	/* SECTION methods */

	@Override
	public V build() { return constructor.apply(castUncheckedUnboxedNonnull(this)); }
}
