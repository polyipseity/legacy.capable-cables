package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core;

public interface IBinderAction {
	EnumActionType getActionType();

	Iterable<? extends IBinding<?>> getBindings();

	enum EnumActionType {
		BIND,
		UNBIND,
	}
}
