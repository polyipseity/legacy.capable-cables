package $group__.utilities.binding.core;

public interface IBinderAction {
	EnumActionType getActionType();

	Iterable<? extends IBinding<?>> getBindings();

	enum EnumActionType {
		BIND,
		UNBIND,
	}
}
