package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.methods.IBindingMethodDestination;

import java.util.Optional;
import java.util.function.Consumer;

public final class ImmutableBindingMethodDestination<T>
		implements IBindingMethodDestination<T> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<T> typeToken;
	@Nullable
	private final IIdentifier bindingKey;
	private final Consumer<@Nonnull ? super T> action;

	@SuppressWarnings("UnstableApiUsage")
	private ImmutableBindingMethodDestination(Class<T> type,
	                                          @Nullable IIdentifier bindingKey,
	                                          Consumer<@Nonnull ? super T> action) {
		this.typeToken = TypeToken.of(type);
		this.bindingKey = bindingKey;
		this.action = action;
	}

	public static <T> ImmutableBindingMethodDestination<T> of(Class<T> type,
	                                                          @Nullable IIdentifier bindingKey,
	                                                          Consumer<@Nonnull ? super T> action) {
		return new ImmutableBindingMethodDestination<>(type, bindingKey, action);
	}

	@Override
	public Optional<? extends IIdentifier> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@Override
	public void accept(@Nonnull T argument) { getAction().accept(argument); }

	protected Consumer<@Nonnull ? super T> getAction() {
		return action;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<T> getTypeToken() {
		return typeToken;
	}
}
