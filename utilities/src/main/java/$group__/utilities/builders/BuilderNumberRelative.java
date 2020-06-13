package $group__.utilities.builders;

import $group__.utilities.NumberRelative;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

import static $group__.utilities.builders.BuilderDefaults.peekDefault;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static com.google.common.collect.Maps.immutableEntry;

public class BuilderNumberRelative<T extends BuilderNumberRelative<T, V>, V extends NumberRelative<V>> extends BuilderStructure<T, V> {
	public static final Map.Entry<Class<Number>, String> KEY_DEFAULT_PARENT = immutableEntry(Number.class, "parent");
	public static final Map.Entry<Class<Number>, String> KEY_DEFAULT_OFFSET = immutableEntry(Number.class, "offset");


	@Nullable
	public Number parent = peekDefault(KEY_DEFAULT_PARENT);
	@Nullable
	public Number offset = peekDefault(KEY_DEFAULT_OFFSET);


	public BuilderNumberRelative(Function<? super T, ? extends V> constructor) { super(constructor); }

	public BuilderNumberRelative(BuilderNumberRelative<T, V> copy) {
		super(copy);
		parent = copy.parent;
		offset = copy.offset;
	}


	public T setParent(@Nullable Number parent) {
		this.parent = parent;
		return castUncheckedUnboxedNonnull(this);
	}

	public T setOffset(@Nullable Number offset) {
		this.offset = offset;
		return castUncheckedUnboxedNonnull(this);
	}
}
