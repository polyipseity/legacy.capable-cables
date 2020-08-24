package $group__.client.ui.mvvm.binding;

import $group__.client.ui.core.mvvm.binding.IBinderAction;
import $group__.client.ui.core.mvvm.binding.IBindingField;
import $group__.client.ui.core.mvvm.binding.IBindingMethod;
import com.google.common.collect.ImmutableSet;

public class BinderAction implements IBinderAction {
	protected final EnumType type;
	protected final ImmutableSet<IBindingField<?>> fields;
	protected final ImmutableSet<IBindingMethod<?>> methods;

	public BinderAction(EnumType type, Iterable<IBindingField<?>> fields, Iterable<IBindingMethod<?>> methods) {
		this.type = type;
		this.fields = ImmutableSet.copyOf(fields);
		this.methods = ImmutableSet.copyOf(methods);
	}

	@Override
	public EnumType getType() { return type; }

	@Override
	public Iterable<IBindingField<?>> getFields() { return fields; }

	@Override
	public Iterable<IBindingMethod<?>> getMethods() { return methods; }

	public static class Bind extends BinderAction {
		public Bind(Iterable<IBindingField<?>> fields, Iterable<IBindingMethod<?>> methods) { super(EnumType.BIND, fields, methods); }
	}

	public static class Unbind extends BinderAction {
		public Unbind(Iterable<IBindingField<?>> fields, Iterable<IBindingMethod<?>> methods) { super(EnumType.UNBIND, fields, methods); }
	}
}
