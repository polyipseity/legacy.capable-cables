package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components;

import java.lang.annotation.*;
import java.lang.invoke.MethodType;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIViewComponentConstructor {
	EnumConstructorType type();

	enum EnumConstructorType
			implements IConstructorType {
		MAPPINGS(MethodType.methodType(void.class, Map.class)),
		;

		protected final MethodType methodType;

		EnumConstructorType(MethodType methodType) { this.methodType = methodType; }

		@Override
		public MethodType getMethodType() { return methodType; }
	}
}
