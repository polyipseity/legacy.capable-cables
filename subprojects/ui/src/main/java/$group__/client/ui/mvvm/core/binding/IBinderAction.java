package $group__.client.ui.mvvm.core.binding;

public interface IBinderAction {
	EnumType getType();
	
	Iterable<IBindingField<?>> getFields();

	Iterable<IBindingMethod<?>> getMethods();
	
	enum EnumType {
		BIND,
		UNBIND,
	}
}
