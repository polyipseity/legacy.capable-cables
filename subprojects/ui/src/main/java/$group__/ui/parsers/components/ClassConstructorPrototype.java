package $group__.ui.parsers.components;

import $group__.ui.core.parsers.components.IConstructorType;
import $group__.utilities.DynamicUtilities;

import java.lang.invoke.MethodHandle;
import java.util.function.Function;

public abstract class ClassConstructorPrototype<T, CT extends IConstructorType>
		extends ClassPrototype<T> {
	protected final CT constructorType;
	protected final MethodHandle constructor;

	protected ClassConstructorPrototype(String prototypeClassName,
	                                    Function<? super Class<? extends T>, ? extends CT> constructorTypeFunction)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
		super(prototypeClassName);

		this.constructorType = constructorTypeFunction.apply(this.prototypeClass);
		this.constructor = DynamicUtilities.IMPL_LOOKUP.findConstructor(this.prototypeClass, this.constructorType.getMethodType());
	}

	protected CT getConstructorType() { return constructorType; }

	protected MethodHandle getConstructor() { return constructor; }
}
