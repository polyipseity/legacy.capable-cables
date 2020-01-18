package $group__.$modId__.utilities.constructs.interfaces.annotations;

import $group__.$modId__.utilities.constructs.classes.concrete.throwables.AnnotationProcessingException;
import org.reflections.Reflections;

import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor.*;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Reflections.isFormerMethodOverriddenByLatter;
import static $group__.$modId__.utilities.helpers.Throwables.throw_;
import static $group__.$modId__.utilities.variables.References.LOGGER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.reflect.Modifier.isFinal;

/**
 *
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface OverridingStatus {
	/* SECTION methods */

	String group();

	When when() default When.UNKNOWN;


	/* SECTION static classes */

	enum AnnotationProcessor implements IClass.IElement.IMethod<OverridingStatus> {
		/* SECTION enums */
		INSTANCE;


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public Class<OverridingStatus> annotationType() { return OverridingStatus.class; }

		/** {@inheritDoc} */
		@Override
		public void processMethod(Result<OverridingStatus> result) {
			OverridingStatus a = result.annotations[0];
			When when = a.when();
			Boolean whenB = when == When.ALWAYS ? Boolean.TRUE : when == When.NEVER ? Boolean.FALSE : null;
			if (whenB == null && when == When.UNKNOWN) return;

			Reflections refs;
			{
				final Reflections[] r = new Reflections[1]; // COMMENT keep hard reference
				refs = CACHED_REFLECTIONS.computeIfAbsent(a.group(), g -> {
					r[0] = new Reflections(g);
					r[0].expandSuperTypes();
					return new SoftReference<>(r[0]);
				}).get();
				assert refs != null;
			}

			Class<?> superC = result.clazz;
			Method superM = result.element;
			Set<Class<?>> ignore = new HashSet<>();
			check:
			for (Class<?> subC : refs.getSubTypesOf(superC)) {
				if (ignore.contains(subC)) continue;
				if (getEffectiveAnnotationIfInheritingConsidered(this, subC, superM) != a) {
					ignore.addAll(refs.getSubTypesOf(subC));
					continue;
				}

				for (Method subM : subC.getDeclaredMethods()) {
					if (whenB == null) {
						if (isFormerMethodOverriddenByLatter(superM, subM)) {
							LOGGER.debug(getMessage(this, "method '" + subM.toGenericString() + "' @ subclass '" + subC.toGenericString() + "' -> @ subclass '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + ")"));
						}
						continue check;
					} else if (isFormerMethodOverriddenByLatter(superM, subM) == whenB) {
						if (whenB && isFinal(subM.getModifiers())) LOGGER.warn(getMessage(this, "Impossible: subclass -Y> final method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + ")"));
						continue check;
					}
				}

				if (whenB == null) {
					LOGGER.debug(getMessage(this, "subclass '" + subC.toGenericString() + "' -X> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + ")"));
				} else throw throw_(new AnnotationProcessingException(getMessage(this, "Unfulfilled requirement: subclass '" + subC.toGenericString() + "' -" + (whenB ? "Y" : "X") + "> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + "), instead: subclass '" + subC.toGenericString() + "' -" + (whenB ? "X" : "") + "> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "'")));
			}
		}
	}
}
