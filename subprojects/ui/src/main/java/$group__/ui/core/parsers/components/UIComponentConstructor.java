package $group__.ui.core.parsers.components;

import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;

import java.lang.annotation.*;
import java.lang.invoke.MethodType;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIComponentConstructor {
	ConstructorType type();

	enum ConstructorType
			implements IConstructorType {
		SHAPE_DESCRIPTOR__MAPPING(MethodType.methodType(void.class, IShapeDescriptor.class, Map.class)),
		;

		protected final MethodType methodType;

		ConstructorType(MethodType methodType) { this.methodType = methodType; }

		@Override
		public MethodType getMethodType() { return methodType; }
	}
}
