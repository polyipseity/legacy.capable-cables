package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import org.reflections.Reflections;

import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.IAnnotationProcessor.getMessage;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.*;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Types.*;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References.LOGGER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface MethodOverrideable {
	String group();

	When when() default When.UNKNOWN;


	IAnnotationProcessor<MethodOverrideable> PROCESSOR = new AnnotationProcessor();

	final class AnnotationProcessor implements IAnnotationProcessor<MethodOverrideable> {
		private AnnotationProcessor() { requireRunOnceOnly(); }


		/** {@inheritDoc} */
		@Override
		public Class<MethodOverrideable> annotationType() { return MethodOverrideable.class; }


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

				String methodName = t.getObjectName();
				Method method = null;
				for (Method m : supper.getDeclaredMethods()) {
					if (methodName.equals(getMethodNameDescriptor(m))) {
						method = m;
						break;
					}
				}
				if (method == null)
					throw rejectArguments(new NoSuchMethodException(getMessage(this, "No method name '" + methodName + "' in class '" + supper.toGenericString() + "'")), thisAsm);

				String group = (String) info.getOrDefault("group", "");
				Reflections refs;
				if (cachedRefs.containsKey(group)) refs = cachedRefs.get(group);
				else {
					refs = new Reflections(group);
					cachedRefs.put(group, refs);
				}

				Method methodF = method;
				refs.getSubTypesOf(supper).forEach(sub -> {
					if (isClassAbstract(sub)) return;
					for (Method m : sub.getDeclaredMethods()) {
						if (whenB == null) {
							if (isFormerOverriddenByLatter(methodF, m)) {
								LOGGER.info(getMessage(this, "Subclass '" + sub.toGenericString() + "' overrides method '" + methodF.toGenericString() + "' in superclass '" + supper.toGenericString() + "'"));
								return;
							}
						} else if (isFormerOverriddenByLatter(methodF, m) == whenB) return;
					}
					if (whenB == null) {
						LOGGER.info(getMessage(this, "Subclass '" + sub.toGenericString() + "' does NOT override method '" + methodF.toGenericString() + "' in superclass '" + supper.toGenericString() + "'"));
						return;
					}
					throw wrapUnhandledThrowable(new NoSuchMethodException(getMessage(this, (whenB ? "No required" : "Forbidden") + " overriding method '" + methodF.toGenericString() + "' from superclass '" + supper.toGenericString() + "' in subclass '" + sub + "'")));
				});
			});
		}
	}
}
