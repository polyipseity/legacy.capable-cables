package $group__.client.ui.core.mvvm.views.components.parsers;

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
		MAPPING__EXTENDED_CLASS(MethodType.methodType(void.class, Map.class, Class.class)),
		MAPPING(MethodType.methodType(void.class, Map.class)),
		EXTENDED_CLASS(MethodType.methodType(void.class, Class.class)),
		NO_ARGS(MethodType.methodType(void.class)),
		;

		protected final MethodType methodType;

		ConstructorType(MethodType methodType) { this.methodType = methodType; }

		@Override
		public MethodType getMethodType() { return methodType; }
	}
}
