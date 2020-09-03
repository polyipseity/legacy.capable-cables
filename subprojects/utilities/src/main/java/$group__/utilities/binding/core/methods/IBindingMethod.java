package $group__.utilities.binding.core.methods;

import $group__.utilities.binding.core.IBinding;

public interface IBindingMethod<T>
		extends IBinding<T> {
	@Override
	default EnumBindingType getBindingType() { return EnumBindingType.METHOD; }

	EnumMethodType getMethodType();

	enum EnumMethodType {
		SOURCE,
		DESTINATION
	}
}
