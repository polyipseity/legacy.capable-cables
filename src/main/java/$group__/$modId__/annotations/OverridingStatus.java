package $group__.$modId__.annotations;

import $group__.$modId__.common.events.AnnotationProcessingEvent;
import $group__.$modId__.utilities.throwables.AnnotationProcessingException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
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

import static $group__.$modId__.traits.basic.IAnnotationProcessor.*;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.Dynamics.*;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.$modId__.utilities.helpers.specific.Throwables.throwThrowable;
import static $group__.$modId__.utilities.helpers.specific.Throwables.throw_;
import static $group__.$modId__.utilities.variables.Constants.INITIAL_SIZE_MEDIUM;
import static $group__.$modId__.utilities.variables.Constants.MOD_ID;
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
		public static void process(AnnotationProcessingEvent event) { if (MOD_ID.equals(event.getModId())) INSTANCE.process(event.getAsm(), event.getLogger()); }


		/* SECTION methods */

		@Override
		public void process(ASMDataTable asm, Logger logger) {
			IMethod.super.process(asm, logger);
			processed = true;
		}

		@Override
		public Class<OverridingStatus> annotationType() { return OverridingStatus.class; }

		@Override
		public boolean isProcessed() { return processed; }

		@Override
		public void processMethod(Result<OverridingStatus> result, Logger logger) {
			OverridingStatus a = result.annotations[0];
			When when = a.when();
			@Nullable Boolean whenB = when == When.ALWAYS ? Boolean.TRUE : when == When.NEVER ? Boolean.FALSE : null;
			if (when == When.UNKNOWN) return;

			Reflections refs;
			String g = a.group();
			try { refs = REFLECTIONS_CACHE.get(g); } catch (ExecutionException e) { throw throwThrowable(e); }

			Class<?> superC = result.clazz;
			Method superM = result.element;
			Set<Class<?>> ignore = new HashSet<>(INITIAL_SIZE_MEDIUM);
			refs.getSubTypesOf(superC).forEach(subC -> {
				if (isClassAbstract(subC) || ignore.contains(subC)) return;
				OverridingStatus[] ar = getEffectiveAnnotationsIfInheritingConsidered(this, subC, superM, logger);
				if (ar.length != 0 && ar[0] != a) {
					ignore.addAll(refs.getSubTypesOf(subC));
					return;
				}

				for (Method subM : subC.getDeclaredMethods()) {
					if (isFormerMethodOverriddenByLatter(superM, subM)) {
						if (whenB == null || whenB)
							logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(getAnnotationProcessorMessage(this, "method '{}' -> @ superclass '{}' ({})"), subM.toGenericString(), superM.toGenericString(), a));
						else
							throw throw_(new AnnotationProcessingException(getAnnotationProcessorMessage(this, "Unfulfilled requirement: subclass '" + subC.toGenericString() + "' -X> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + ")," + System.lineSeparator() + "instead: method '" + subM.toGenericString() + "' -> @ superclass '" + superC.toGenericString() + '\'')));
						return;
					}
				}

				if (whenB == null || !whenB) {
					logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(getAnnotationProcessorMessage(this, "subclass '{}' -X> method '{}' @ superclass '{}' ({})"), subC.toGenericString(), superM.toGenericString(), superC.toGenericString(), a));
				} else {
					for (Class<?> superC1 : getIntermediateSuperclasses(subC, castUncheckedUnboxed(superC.getSuperclass()))) {
						for (Method superM1 : superC1.getDeclaredMethods()) {
							if (isFormerMethodOverriddenByLatter(superM, superM1) && isMemberFinal(superM1)) {
								logger.log(superM1.getDeclaringClass().getName().startsWith(g) ? Level.WARN : Level.INFO, getAnnotationProcessorMessage(this, "Impossible requirement: subclass '" + subC.toGenericString() + "' -Y> final method '" + superM1.toGenericString() + "' @ superclass '" + superM1.toGenericString() + "' (" + a + ')'));
								return;
							}
						}
					}
					throw throw_(new AnnotationProcessingException(getAnnotationProcessorMessage(this, "Unfulfilled requirement: subclass '" + subC.toGenericString() + "' -Y> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + "' (" + a + ")," + System.lineSeparator() + "instead: subclass '" + subC.toGenericString() + "' -X> method '" + superM.toGenericString() + "' @ superclass '" + superC.toGenericString() + '\'')));
				}
			});
		}
	}
}
