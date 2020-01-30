package $group__.$modId__.utilities.constructs.interfaces.annotations;

import $group__.$modId__.utilities.constructs.classes.concrete.AnnotationProcessingEvent;
import $group__.$modId__.utilities.constructs.classes.concrete.throwables.AnnotationProcessingException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.Reflections.*;
import static $group__.$modId__.utilities.helpers.Throwables.throwThrowable;
import static $group__.$modId__.utilities.helpers.Throwables.throw_;
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


		/* SECTION static methods */

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void process(AnnotationProcessingEvent event) {
			if (MOD_ID.equals(event.getModId())) INSTANCE.process(event.getAsm());
		}


		/* SECTION methods */

		@Override
		public void process(ASMDataTable asm) {
			IMethod.super.process(asm);
			processed = true;
		}

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
			try { refs = REFLECTIONS_CACHE.get(g); } catch (ExecutionException e) { throw throwThrowable(e); }

			Class<?> superC = result.clazz;
			Method superM = result.element;
			Set<Class<?>> ignore = new HashSet<>();
			refs.getSubTypesOf(superC).forEach(subC -> {
				if (isClassAbstract(subC) || ignore.contains(subC)) return;
				OverridingStatus[] ar = getEffectiveAnnotationsIfInheritingConsidered(this, subC, superM);
				if (ar.length != 0 && ar[0] != a) {
					ignore.addAll(refs.getSubTypesOf(subC));
					return;
				}

				for (Method subM : subC.getDeclaredMethods()) {
					if (isFormerMethodOverriddenByLatter(superM, subM)) {
						if (whenB == null || whenB)
							LOGGER.debug(getMessage(this, "method '" + subM.toGenericString() + "' -> @ superclass '" + superM.toGenericString() + "' (" + a + ")"));
						else
							throw throw_(new AnnotationProcessingException(getMessage(this, "Unfulfilled requirement: subclass '" + subC.toGenericString() + "' -X> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + "),\ninstead: method '" + subM.toGenericString() + "' -> @ superclass '" + superC.toGenericString() + "'")));
						return;
					}
				}

				if (whenB == null || !whenB) {
					LOGGER.debug(getMessage(this, "subclass '" + subC.toGenericString() + "' -X> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + ")"));
				} else {
					for (Class<?> superC1 : getIntermediateSuperclasses(subC, castUncheckedUnboxed(superC.getSuperclass()))) {
						for (Method superM1 : superC1.getDeclaredMethods()) {
							if (isFormerMethodOverriddenByLatter(superM, superM1) && isMemberFinal(superM1)) {
								LOGGER.log(superM1.getDeclaringClass().getName().startsWith(g) ? Level.WARN : Level.INFO, getMessage(this, "Impossible requirement: subclass '" + subC.toGenericString() + "' -Y> final method '" + superM1.toGenericString() + "' @ superclass '" + superM1.toGenericString() + "' (" + a + ")"));
								return;
							}
						}
					}
					throw throw_(new AnnotationProcessingException(getMessage(this, "Unfulfilled requirement: subclass '" + subC.toGenericString() + "' -Y> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + "),\ninstead: subclass '" + subC.toGenericString() + "' -X> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "'")));
				}
			});
		}
	}
}
