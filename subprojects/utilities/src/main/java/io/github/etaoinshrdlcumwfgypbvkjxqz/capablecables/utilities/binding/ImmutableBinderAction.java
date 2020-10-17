package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinding;

import java.util.Arrays;

public final class ImmutableBinderAction implements IBinderAction {
	private final EnumActionType type;
	private final Iterable<? extends IBinding<?>> bindings;

	private ImmutableBinderAction(EnumActionType type, Iterable<? extends IBinding<?>> bindings) {
		this.type = type;
		this.bindings = ImmutableSet.copyOf(bindings);
	}

	public static ImmutableBinderAction bind(IBinding<?>... bindings) {
		return bind(Arrays.asList(bindings)); // COMMENT reuse the array
	}

	public static ImmutableBinderAction bind(Iterable<? extends IBinding<?>> bindings) {
		return of(EnumActionType.BIND, bindings);
	}

	private static ImmutableBinderAction of(EnumActionType type, Iterable<? extends IBinding<?>> bindings) {
		return new ImmutableBinderAction(type, bindings);
	}

	public static ImmutableBinderAction unbind(IBinding<?>... bindings) {
		return unbind(Arrays.asList(bindings)); // COMMENT reuse the array
	}

	public static ImmutableBinderAction unbind(Iterable<? extends IBinding<?>> bindings) {
		return of(EnumActionType.UNBIND, bindings);
	}

	@Override
	public EnumActionType getActionType() { return type; }

	@Override
	public Iterable<? extends IBinding<?>> getBindings() { return bindings; }
}
