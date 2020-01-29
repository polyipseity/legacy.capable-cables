package $group__.$modId__.utilities.constructs.interfaces.annotations;

import $group__.$modId__.utilities.constructs.classes.concrete.AnnotationProcessingEvent;
import $group__.$modId__.utilities.constructs.classes.concrete.throwables.AnnotationProcessingException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor.*;
import static $group__.$modId__.utilities.helpers.Reflections.*;
import static $group__.$modId__.utilities.helpers.Throwables.throw_;
import static $group__.$modId__.utilities.helpers.Throwables.wrapCheckedThrowable;
import static $group__.$modId__.utilities.variables.Constants.MOD_ID;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static $group__.$modId__.utilities.variables.Globals.REFLECTIONS_CACHE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface OverridingStatus {
	/* SECTION methods */

	String group();

	When when() default When.UNKNOWN;


	/* SECTION static classes */

	@Mod.EventBusSubscriber(modid = MOD_ID)
	enum AnnotationProcessor implements IClass.IElement.IMethod<OverridingStatus> {
		/* SECTION enums */
		INSTANCE;


		/* SECTION variables */

		private volatile boolean processed = false;


		/* SECTION methods */

		@Override
		public Class<OverridingStatus> annotationType() { return OverridingStatus.class; }

		@Override
		public boolean isProcessed() { return processed; }


		@Override
		public void processMethod(Result<OverridingStatus> result) {
			OverridingStatus a = result.annotations[0];
			When when = a.when();
			@Nullable Boolean whenB = when == When.ALWAYS ? Boolean.TRUE : when == When.NEVER ? Boolean.FALSE : null;
			if (when == When.UNKNOWN) return;

			Reflections refs;
			String g = a.group();
			try { refs = REFLECTIONS_CACHE.get(g); } catch (ExecutionException e) { throw wrapCheckedThrowable(e); }

			Class<?> superC = result.clazz;
			Method superM = result.element;
			Set<Class<?>> ignore = new HashSet<>();
			check:
			for (Class<?> subC : refs.getSubTypesOf(superC)) {
				if (isClassAbstract(subC) || ignore.contains(subC)) continue;
				OverridingStatus[] ar = getEffectiveAnnotationsIfInheritingConsidered(this, subC, superM);
				if (ar.length != 0 && ar[0] != a) {
					ignore.addAll(refs.getSubTypesOf(subC));
					continue;
				}

				for (Method subM : subC.getDeclaredMethods()) {
					if (whenB == null) {
						if (isFormerMethodOverriddenByLatter(superM, subM)) {
							LOGGER.debug(getMessage(this, "method '" + subM.toGenericString() + "' -> @ subclass '" + superM.toGenericString() + "' (" + a + ")"));
						}
						continue check;
					} else if (isFormerMethodOverriddenByLatter(superM, subM) == whenB) {
						if (whenB && isMemberFinal(subM))
							LOGGER.warn(getMessage(this, "Impossible: subclass -Y> final method '" + superM.toGenericString() + "' (" + a + ")"));
						continue check;
					}
				}

				if (whenB == null) {
					LOGGER.debug(getMessage(this, "subclass '" + subC.toGenericString() + "' -X> method '" + superM.toGenericString() + "' (" + a + ")"));
				} else {
					if (whenB) {
						Class<?> superC1 = subC;
						while (superC1 != null && superC1 != superC) {
							for (Method superM1 : superC1.getDeclaredMethods()) {
								if (isFormerMethodOverriddenByLatter(superM, superM1) && isMemberFinal(superM1)) {
									LOGGER.log(superM1.getDeclaringClass().getName().startsWith(g) ? Level.WARN : Level.INFO, getMessage(this, "Impossible: subclass '" + subC.toGenericString() + "' -Y> final method '" + superM1.toGenericString() + "' (" + a + ")"));
									continue check;
								}
							}
							superC1 = superC1.getSuperclass();
						}
					}

					throw throw_(new AnnotationProcessingException(getMessage(this, "Unfulfilled requirement: subclass '" + subC.toGenericString() + "' -" + (whenB ? "Y" : "X") + "> method '" + superM.toGenericString() + "' (" + a + "), instead: subclass '" + subC.toGenericString() + "' -" + (whenB ? "X" : StringUtils.EMPTY) + "> method '" + superM.toGenericString() + "'")));
				}
			}
		}

		@Override
		public void process(ASMDataTable asm) {
			IMethod.super.process(asm);
			processed = true;
		}


		/* SECTION static methods */

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void process(AnnotationProcessingEvent event) { if (MOD_ID.equals(event.getModId())) INSTANCE.process(event.getAsm()); }
	}
}
