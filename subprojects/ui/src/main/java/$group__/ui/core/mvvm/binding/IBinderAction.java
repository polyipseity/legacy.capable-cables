package $group__.ui.core.mvvm.binding;

public interface IBinderAction {
	EnumType getType();

	Iterable<IBindingField<?>> getFields();

	Iterable<IBindingMethod<?>> getMethods();

	enum EnumType {
		BIND,
		UNBIND,
	}
}
