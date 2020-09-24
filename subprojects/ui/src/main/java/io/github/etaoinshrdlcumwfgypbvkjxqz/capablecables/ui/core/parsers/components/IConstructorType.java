package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Marker;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IConstructorType {
	MethodType getMethodType();

	enum StaticHolder {
		;

		private static final Marker CLASS_MARKER = UIMarkers.getInstance().getClassMarker();

		public static <T extends IConstructorType, A extends Annotation> T getConstructorType(Class<?> clazz, Class<A> annotationType, Function<? super A, ? extends T> getter) {
			return findConstructorType(clazz, annotationType, getter)
					.orElseThrow(() ->
							new IllegalStateException(
									new LogMessageBuilder()
											.addMarkers(StaticHolder::getClassMarker)
											.addKeyValue("clazz", clazz).addKeyValue("annotationType", annotationType).addKeyValue("getter", getter)
											.addMessages(() -> getResourceBundle().getString("constructor_type.find.missing"))
											.build()
							));
		}

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static Marker getClassMarker() { return CLASS_MARKER; }

		public static <T extends IConstructorType, A extends Annotation> Optional<T> findConstructorType(Class<?> clazz, Class<A> annotationType, Function<? super A, ? extends T> getter) {
			return Arrays.stream(clazz.getDeclaredConstructors()).unordered()
					.map(cc -> cc.getAnnotation(annotationType))
					.filter(Objects::nonNull)
					.findAny()
					.map(getter);
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
