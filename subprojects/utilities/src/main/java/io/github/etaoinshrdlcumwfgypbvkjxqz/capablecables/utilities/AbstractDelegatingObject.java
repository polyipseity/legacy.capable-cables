package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.function.Function;

public abstract class AbstractDelegatingObject<T> {
	private final T delegate;

	public AbstractDelegatingObject(T delegate) { this.delegate = delegate; }

	protected T getDelegate() { return delegate; }

	private static final @NonNls Map<String, Function<@Nonnull AbstractDelegatingObject<?>, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull AbstractDelegatingObject<?>, @Nullable ?>>builder()
					.put("delegate", AbstractDelegatingObject::getDelegate)
					.build();

	public static @Immutable @NonNls Map<String, Function<AbstractDelegatingObject<?>, ?>> getObjectVariableMapView() {
		return ImmutableMap.copyOf(getObjectVariableMap());
	}

	private static @NonNls Map<String, Function<AbstractDelegatingObject<?>, ?>> getObjectVariableMap() {
		return OBJECT_VARIABLE_MAP;
	}
}
