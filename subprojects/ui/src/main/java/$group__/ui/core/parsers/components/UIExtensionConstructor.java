package $group__.ui.core.parsers.components;

import java.lang.annotation.*;
import java.lang.invoke.MethodType;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIExtensionConstructor {
	ConstructorType type();

	enum ConstructorType
			implements IConstructorType {
		MAPPINGS__CONTAINER_CLASS(MethodType.methodType(void.class, Map.class, Class.class)),
		MAPPINGS(MethodType.methodType(void.class, Map.class)),
		CONTAINER_CLASS(MethodType.methodType(void.class, Class.class)),
		NO_ARGS(MethodType.methodType(void.class)),
		;

		protected final MethodType methodType;

		ConstructorType(MethodType methodType) { this.methodType = methodType; }

		@Override
		public MethodType getMethodType() { return methodType; }
	}
}
