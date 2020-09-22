package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinding;
import com.google.common.collect.ImmutableSet;

public class BinderAction implements IBinderAction {
	protected final EnumActionType type;
	protected final Iterable<? extends IBinding<?>> bindings;

	public BinderAction(EnumActionType type, Iterable<? extends IBinding<?>> bindings) {
		this.type = type;
		this.bindings = ImmutableSet.copyOf(bindings);
	}

	@Override
	public EnumActionType getActionType() { return type; }

	@Override
	public Iterable<? extends IBinding<?>> getBindings() { return bindings; }

	public static class Bind extends BinderAction {
		public Bind(Iterable<? extends IBinding<?>> bindings) { super(EnumActionType.BIND, bindings); }
	}

	public static class Unbind extends BinderAction {
		public Unbind(Iterable<? extends IBinding<?>> bindings) { super(EnumActionType.UNBIND, bindings); }
	}
}
