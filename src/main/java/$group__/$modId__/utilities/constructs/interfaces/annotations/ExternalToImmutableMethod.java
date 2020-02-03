package $group__.$modId__.utilities.constructs.interfaces.annotations;

import $group__.$modId__.utilities.constructs.classes.concrete.AnnotationProcessingEvent;
import $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor;
import $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable;
import $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.MethodAdapter;
import $group__.$modId__.utilities.helpers.Throwables;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor.getMessage;
import static $group__.$modId__.utilities.variables.Constants.MOD_ID;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Nonnull
@Retention(RUNTIME)
@Target(METHOD)
public @interface ExternalToImmutableMethod {
	/* SECTION methods */

	Class<?>[] value();

	@SuppressWarnings("SameReturnValue") boolean allowExtends() default false;


	/* SECTION static classes */

	@Mod.EventBusSubscriber(modid = MOD_ID)
	enum AnnotationProcessor implements IAnnotationProcessor.IClass.IElement.IMethod<ExternalToImmutableMethod> {
		/* SECTION enums */
		INSTANCE;


		/* SECTION variables */

		private volatile boolean processed = false;


		/* SECTION static methods */

		@SubscribeEvent(priority = EventPriority.HIGHEST)
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
		public Class<ExternalToImmutableMethod> annotationType() { return ExternalToImmutableMethod.class; }

		@Override
		public boolean isProcessed() { return processed; }

		@SuppressWarnings("ConstantConditions")
		@Override
		public void processMethod(Result<ExternalToImmutableMethod> result) {
			ExternalToImmutableMethod a = result.annotations[0];
			@Nullable ExternalToImmutableMethod ap;
			Method m = result.element;

			Class<?>[] ks = a.value();
			if (ks.length == 0) {
				LOGGER.warn(getMessage(this, "Method '{}' with annotation '{}' has no usage"), m.toGenericString(), a);
				return;
			}
			IImmutablizable.EXTERNAL_METHOD_MAP.put(a, MethodAdapter.of(m));

			for (Class<?> k : ks) {
				ap = IImmutablizable.EXTERNAL_ANNOTATIONS_CACHE.getIfPresent(k);
				IImmutablizable.EXTERNAL_ANNOTATIONS_CACHE.put(k, a);
				if (ap == null)
					LOGGER.debug(getMessage(this, "Registered method '{}' with annotation '{}' for class '{}'"), m.toGenericString(), a, k.toGenericString());
				else
					LOGGER.warn(getMessage(this, "Replaced previous method '{}' with annotation '{}' with method '{}' with annotation '{}' for class '{}'"), IImmutablizable.EXTERNAL_METHOD_MAP.get(ap).get().orElseThrow(Throwables::unexpected).toGenericString(), ap, m.toGenericString(), a, k.toGenericString());
			}
		}
	}
}
