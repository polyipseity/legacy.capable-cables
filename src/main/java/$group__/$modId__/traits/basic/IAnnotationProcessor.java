package $group__.$modId__.traits.basic;

import $group__.$modId__.utilities.helpers.specific.Throwables;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Dynamics.getMethodNameDescriptor;
import static $group__.$modId__.utilities.helpers.Dynamics.getSuperclassesAndInterfaces;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.REFLECTION_UNABLE_TO_GET_MEMBER;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.SUFFIX_WITH_THROWABLE;
import static $group__.$modId__.utilities.helpers.specific.StringsExtension.STRING_EMPTY;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static java.lang.Class.forName;

public interface IAnnotationProcessor<A extends Annotation> {
	/* SECTION static methods */

	static String getAnnotationProcessorMessage(IAnnotationProcessor<?> processor, @Nullable String msg) { return "Processing annotation '" + processor.annotationType() + '\'' + (msg == null || msg.isEmpty() ? STRING_EMPTY : ": " + msg); }

	static <A extends Annotation> A[] getEffectiveAnnotationsIfInheritingConsidered(IAnnotationProcessor<A> processor, Class<?> clazz, Method method, Logger logger) {
		Class<A> aClass = processor.annotationType();
		String mName = method.getName();
		Class<?>[] mArgs = method.getParameterTypes();

		A[] r = castUncheckedUnboxedNonnull(Array.newInstance(aClass, 0));
		sss:
		for (LinkedHashSet<Class<?>> ss : getSuperclassesAndInterfaces(clazz)) {
			for (Class<?> s : ss) {
				r = tryCall(() -> s.getDeclaredMethod(mName, mArgs), logger).map(t -> t.getDeclaredAnnotationsByType(aClass)).orElse(r);
				consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_GET_MEMBER.makeMessage("method", s, mName, mArgs), t)));
				if (r.length != 0) break sss;
			}
		}

		return r;
	}


	/* SECTION methods */

	Class<A> annotationType();


	boolean isProcessed();

	void process(ASMDataTable asm, Logger logger);


	/* SECTION static classes */

	interface IClass<A extends Annotation> extends IAnnotationProcessor<A> {
		/* SECTION methods */

		@Override
		default void process(ASMDataTable asm, Logger logger) {
			Set<ASMDataTable.ASMData> thisAsm = asm.getAll(annotationType().getName());
			thisAsm.forEach(t -> processClass(new Result(asm, thisAsm, t, tryCall(() -> forName(t.getClassName(), false, getClass().getClassLoader()), logger).orElseThrow(Throwables::rethrowCaughtThrowableStatic)), logger));
		}

		void processClass(Result result, Logger logger);


		/* SECTION static classes */

		interface IElement<A extends Annotation, AE extends AnnotatedElement> extends IClass<A> {
			/* SECTION methods */

			@Override
			default void processClass(IClass.Result result, Logger logger) {
				AE ae = findElement(result, logger);
				processElement(new Result<>(result, ae, ae.getDeclaredAnnotationsByType(annotationType())), logger);
			}

			AE findElement(IClass.Result result, Logger logger);

			void processElement(Result<A, AE> result, Logger logger);


			/* SECTION static classes */

			interface IMethod<A extends Annotation> extends IElement<A, Method> {
				/* SECTION methods */

				@Override
				default Method findElement(IClass.Result result, Logger logger) {
					String mName = result.currentAsm.getObjectName();
					@Nullable Method r = Arrays.stream(result.clazz.getDeclaredMethods()).filter(m -> mName.equals(getMethodNameDescriptor(m))).findFirst().orElse(null);
					if (r == null)
						throw rejectArguments(new NoSuchMethodException(getAnnotationProcessorMessage(this, "No method name '" + mName + "' in class '" + result.clazz.toGenericString() + '\'')), result.thisAsm);
					return r;
				}

				@Override
				default void processElement(IElement.Result<A, Method> result, Logger logger) { processMethod(new Result<>(result), logger); }

				void processMethod(Result<A> result, Logger logger);


				/* SECTION static classes */

				class Result<A extends Annotation> extends IElement.Result<A, Method> {
					/* SECTION constructors */

					protected Result(Result<? extends A> c) { this((IElement.Result<? extends A, Method>) c); }

					protected Result(IElement.Result<? extends A, Method> result) { super(result); }
				}
			}

			class Result<A extends Annotation, AE extends AnnotatedElement> extends IClass.Result {
				/* SECTION variables */

				public final AE element;
				public final A[] annotations;


				/* SECTION constructors */

				protected Result(Result<? extends A, ? extends AE> c) { this(c, c.element, c.annotations); }

				protected Result(IClass.Result result, AE element, A[] annotations) {
					super(result);
					this.element = element;
					this.annotations = annotations;
				}
			}
		}

		class Result implements IStruct {
			/* SECTION variables */

			public final ASMDataTable asm;
			public final Set<ASMDataTable.ASMData> thisAsm;
			public final ASMDataTable.ASMData currentAsm;
			public final Class<?> clazz;


			/* SECTION constructors */

			protected Result(Result c) { this(c.asm, c.thisAsm, c.currentAsm, c.clazz); }

			protected Result(ASMDataTable asm, Set<ASMDataTable.ASMData> thisAsm, ASMDataTable.ASMData currentAsm, Class<?> clazz) {
				this.asm = asm;
				this.thisAsm = thisAsm;
				this.currentAsm = currentAsm;
				this.clazz = clazz;
			}
		}
	}
}
