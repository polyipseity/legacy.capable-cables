package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodDestination;

import java.util.Optional;
import java.util.function.Consumer;

public final class ImmutableBindingMethodDestination<T>
		implements IBindingMethodDestination<T> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<T> typeToken;
	@Nullable
	private final INamespacePrefixedString bindingKey;
	private final Consumer<@Nonnull ? super T> action;

	@SuppressWarnings("UnstableApiUsage")
	public ImmutableBindingMethodDestination(Class<T> type, @Nullable INamespacePrefixedString bindingKey, Consumer<@Nonnull ? super T> action) {
		this.typeToken = TypeToken.of(type);
		this.bindingKey = bindingKey;
		this.action = action;
	}

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }

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
