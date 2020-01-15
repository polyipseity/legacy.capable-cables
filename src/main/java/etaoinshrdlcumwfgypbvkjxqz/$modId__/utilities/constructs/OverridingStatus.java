package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import org.reflections.Reflections;

import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IAnnotationProcessor.getMessage;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Reflections.getMethodNameDescriptor;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Reflections.isMethodFormerOverriddenByLatter;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.wrapUnhandledThrowable;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.References.LOGGER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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

	enum AnnotationProcessor implements IAnnotationProcessor<OverridingStatus> {
		/* SECTION enums */
		INSTANCE;


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public Class<OverridingStatus> annotationType() { return OverridingStatus.class; }


		/** {@inheritDoc} */
		@Override
		public void process(ASMDataTable asm) {
			Set<ASMDataTable.ASMData> thisAsm = asm.getAll(annotationType().getName());
			Map<String, Reflections> cachedRefs = new HashMap<>();

			thisAsm.forEach(t -> {
				Map<String, Object> info = t.getAnnotationInfo();
				When when = When.valueOf(((ModAnnotation.EnumHolder) info.get("when")).getValue());
				Boolean whenB = when == When.ALWAYS ? Boolean.TRUE : when == When.NEVER ? Boolean.FALSE : null;
				if (whenB == null && when == When.UNKNOWN) return;

				Class<?> supper;
				try { supper = Class.forName(t.getClassName(), false, getClass().getClassLoader()); } catch (ClassNotFoundException e) { throw rejectArguments(e, t.getClassName()); }

				String superMName = t.getObjectName();
				Method superM = null;
				for (Method m : supper.getDeclaredMethods()) {
					if (superMName.equals(getMethodNameDescriptor(m))) {
						superM = m;
						break;
					}
				}
				if (superM == null)
					throw rejectArguments(new NoSuchMethodException(getMessage(this, "No method name '" + superMName + "' in class '" + supper.toGenericString() + "'")), thisAsm);

				String group = (String) info.getOrDefault("group", "");
				Reflections refs = cachedRefs.computeIfAbsent(group, Reflections::new);

				Iterator<Class<?>> subs = castUnchecked(refs.getSubTypesOf(supper).iterator());
				Set<Class<?>> ignore = new HashSet<>();
				Class<?> sub;

				check:
				while (subs.hasNext()) {
					sub = subs.next();
					if (ignore.contains(sub)) continue;
					/* CODE
					if (isClassAbstract(sub)) for (Method subM : sub.getMethods()) {
						if (isFormerOverriddenByLatter(superM, subM)) {
							if (checkAnnotationOverriding(this, supper, superM, sub, subM))
								ignore.addAll(refs.getSubTypesOf(sub));
							continue check;
						}
						LOGGER.warn(getMessage(this, "Subclass '" + sub.toGenericString() + "' does NOT contain method '" + superM.toGenericString() + "' in superclass '" + supper.toGenericString() + "'"));
						continue check;
					} */

					for (Method subM : sub.getDeclaredMethods()) {
						if (whenB == null) {
							if (isMethodFormerOverriddenByLatter(superM, subM)) {
								LOGGER.debug(getMessage(this, "Subclass '" + sub.toGenericString() + "' overrides method '" + superM.toGenericString() + "' in superclass '" + supper.toGenericString() + "'"));
							}
							if (checkAnnotationOverriding(this, supper, superM, sub, subM))
								ignore.addAll(refs.getSubTypesOf(sub));
							continue check;
						} else if (isMethodFormerOverriddenByLatter(superM, subM) == whenB) {
							if (checkAnnotationOverriding(this, supper, superM, sub, subM))
								ignore.addAll(refs.getSubTypesOf(sub));
							continue check;
						}
					}
					if (whenB == null) {
						LOGGER.debug(getMessage(this, "Subclass '" + sub.toGenericString() + "' does NOT override method '" + superM.toGenericString() + "' in superclass '" + supper.toGenericString() + "'"));
					} else
						throw wrapUnhandledThrowable(new NoSuchMethodException(getMessage(this, (whenB ? "No required" : "Forbidden") + " overriding method '" + superM.toGenericString() + "' from superclass '" + supper.toGenericString() + "' in subclass '" + sub + "'")));
				}
			});
		}


		/* SECTION static methods */

		private static boolean checkAnnotationOverriding(AnnotationProcessor processor, Class<?> supper, Method superM, Class<?> sub, Method subM) {
			OverridingStatus a = subM.getDeclaredAnnotation(processor.annotationType());
			boolean r;
			if (r = (a != null))
				LOGGER.debug(getMessage(processor, "Subclass '" + sub.toGenericString() + "' overrides annotation '" + a.getClass().toGenericString() + "' with '" + a + "' in method  '" + superM.toGenericString() + "' in superclass '" + supper.toGenericString() + "'"));
			return r;
		}
	}
}
