package $group__.ui.core.parsers.components;

import $group__.ui.UIConfiguration;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.templates.CommonConfigurationTemplate;

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

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static <T extends IConstructorType, A extends Annotation> T getConstructorType(Class<?> clazz, Class<A> annotationType, Function<? super A, ? extends T> getter) {
			return findConstructorType(clazz, annotationType, getter)
					.orElseThrow(() ->
							ThrowableUtilities.logAndThrow(new IllegalStateException(
									new LogMessageBuilder()
											.addKeyValue("clazz", clazz).addKeyValue("annotationType", annotationType).addKeyValue("getter", getter)
											.appendMessages(getResourceBundle().getString("constructor_type.find.missing"))
											.build()
							), UIConfiguration.getInstance().getLogger()));
		}

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
