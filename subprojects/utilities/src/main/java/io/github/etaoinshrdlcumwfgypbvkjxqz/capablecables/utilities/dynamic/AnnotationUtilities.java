package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.slf4j.Marker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public enum AnnotationUtilities {
	;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static <R extends AnnotatedElement> R getElementAnnotatedWith(Class<? extends Annotation> annotationClass, Iterator<? extends R> elements)
			throws NoSuchElementException, IllegalArgumentException {
		return Iterators.getOnlyElement(Spliterators.iterator(getElementsAnnotatedWith(annotationClass, elements)));
	}

	@SuppressWarnings("UnstableApiUsage")
	@Immutable
	public static <R extends AnnotatedElement> Spliterator<R> getElementsAnnotatedWith(Class<? extends Annotation> annotationClass, Iterator<? extends R> elements) {
		return Streams.stream(elements).unordered()
				.filter(element -> element.isAnnotationPresent(annotationClass))
				.<R>map(Function.identity())
				.spliterator();
	}

	public static <A extends Annotation> A getEffectiveAnnotationWithInheritingConsidered(Class<A> annotationType, Method method) throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsWithInheritingConsidered(annotationType, method);
		if (r.length != 1)
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(AnnotationUtilities::getClassMarker)
							.addKeyValue("annotationType", annotationType).addKeyValue("method", method)
							.addMessages(() -> getResourceBundle().getString("annotations.get.plural.fail"))
							.build()
			);
		return r[0];
	}

	static <A extends Annotation> A[] getEffectiveAnnotationsWithInheritingConsidered(Class<A> annotationType, Method method) {
		A[] ret = method.getDeclaredAnnotationsByType(annotationType);
		if (ret.length != 0)
			return ret;

		String mName = method.getName();
		Class<?>[] mArgs = method.getParameterTypes();
		return ClassUtilities.getSuperclassesAndInterfaces(method.getDeclaringClass()).stream()
				.flatMap(Collection::stream)
				.map(clazz -> {
					try {
						return clazz.getDeclaredMethod(mName, mArgs);
					} catch (NoSuchMethodException e) {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.map(superMethod -> superMethod.getAnnotationsByType(annotationType))
				.filter(annotations -> annotations.length != 0)
				.findFirst()
				.orElse(ret);
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
