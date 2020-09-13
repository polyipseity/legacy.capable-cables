package $group__.ui.core.parsers.components;

import $group__.utilities.ThrowableUtilities;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IConstructorType {
	MethodType getMethodType();

	static <T extends IConstructorType, A extends Annotation> T getConstructorType(Class<?> clazz, Class<A> annotationType, Function<? super A, ? extends T> getter) {
		return findConstructorType(clazz, annotationType, getter)
				.orElseThrow(() ->
						ThrowableUtilities.BecauseOf.illegalArgument("Cannot find any annotated constructor",
								"clazz.getDeclaredConstructors()", clazz.getDeclaredConstructors(),
								"clazz", clazz));
	}

	static <T extends IConstructorType, A extends Annotation> Optional<T> findConstructorType(Class<?> clazz, Class<A> annotationType, Function<? super A, ? extends T> getter) {
		return Arrays.stream(clazz.getDeclaredConstructors()).unordered()
				.map(cc -> cc.getAnnotation(annotationType))
				.filter(Objects::nonNull)
				.findAny()
				.map(getter);
	}
}
