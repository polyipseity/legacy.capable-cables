package $group__.ui.core.parsers.components;

import java.lang.annotation.*;
import java.lang.invoke.MethodType;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIViewComponentConstructor {
	ConstructorType type();

	enum ConstructorType
			implements IConstructorType {
		MAPPING(MethodType.methodType(void.class, Map.class)),
		;

		protected final MethodType methodType;

		ConstructorType(MethodType methodType) { this.methodType = methodType; }

		@Override
		public MethodType getMethodType() { return methodType; }
	}
}
