package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.variables.Globals;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Set;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.forName;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.getDeclaredMethod;
import static $group__.$modId__.utilities.helpers.Reflections.getMethodNameDescriptor;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;

public interface IAnnotationProcessor<A extends Annotation> {
	/* SECTION methods */

	Class<A> annotationType();

	boolean isProcessed();


	void process(ASMDataTable asm);


	/* SECTION static methods */

	static String getMessage(IAnnotationProcessor<?> processor, @Nullable String msg) { return "Processing annotation '" + processor.annotationType() + "'" + (msg == null || msg.isEmpty() ? StringUtils.EMPTY : ": " + msg); }


	static <A extends Annotation> A[] getEffectiveAnnotationsIfInheritingConsidered(IAnnotationProcessor<A> processor, Class<?> clazz, Method method) {
		Class<A> aClass = processor.annotationType();
		String mName = method.getName();
		Class<?>[] mArgs = method.getParameterTypes();

		A[] r = castUncheckedUnboxedNonnull(Array.newInstance(aClass, 0));
		do
		{ r = getDeclaredMethod(clazz, mName, mArgs).get().map(t -> t.getDeclaredAnnotationsByType(aClass)).orElse(r); } while (r.length == 0 && (clazz = clazz.getSuperclass()) != null);

		return r;
	}


	/* SECTION static classes */

	interface IClass<A extends Annotation> extends IAnnotationProcessor<A> {
		/* SECTION methods */

		void processClass(Result result);

		/** {@inheritDoc} */
		@Override
		default void process(ASMDataTable asm) {
			Set<ASMDataTable.ASMData> thisAsm = asm.getAll(annotationType().getName());
			thisAsm.forEach(t -> processClass(new Result(asm, thisAsm, t, forName(t.getClassName(), false, getClass().getClassLoader()).orElseThrow(Globals::rethrowCaughtThrowableStatic))));
		}


		/* SECTION static classes */

		class Result implements IStruct {
			/* SECTION variables */

			public final ASMDataTable asm;
			public final Set<ASMDataTable.ASMData> thisAsm;
			public final ASMDataTable.ASMData currentAsm;
			public final Class<?> clazz;


			/* SECTION constructors */

			protected Result(ASMDataTable asm, Set<ASMDataTable.ASMData> thisAsm, ASMDataTable.ASMData currentAsm, Class<?> clazz) {
				this.asm = asm;
				this.thisAsm = thisAsm;
				this.currentAsm = currentAsm;
				this.clazz = clazz;
			}

			protected Result(Result c) { this(c.asm, c.thisAsm, c.currentAsm, c.clazz); }
		}


		interface IElement<A extends Annotation, AE extends AnnotatedElement> extends IClass<A> {
			/* SECTION methods */

			AE findElement(IClass.Result result);

			void processElement(Result<A, AE> result);

			/** {@inheritDoc} */
			@Override
			default void processClass(IClass.Result result) {
				AE ae = findElement(result);
				processElement(new Result<>(result, ae, ae.getDeclaredAnnotationsByType(annotationType())));
			}


			/* SECTION static classes */

			class Result<A extends Annotation, AE extends AnnotatedElement> extends IClass.Result {
				/* SECTION variables */

				public final AE element;
				public final A[] annotations;


				/* SECTION constructors */

				protected Result(IClass.Result result, AE element, A[] annotations) {
					super(result);
					this.element = element;
					this.annotations = annotations;
				}

				protected Result(Result<? extends A, ? extends AE> c) { this(c, c.element, c.annotations); }
			}


			interface IMethod<A extends Annotation> extends IElement<A, Method> {
				/* SECTION methods */

				void processMethod(Result<A> result);

				/** {@inheritDoc} */
				@Override
				default Method findElement(IClass.Result result) {
					String mName = result.currentAsm.getObjectName();
					@Nullable Method r = null;
					for (Method m : result.clazz.getDeclaredMethods()) {
						if (mName.equals(getMethodNameDescriptor(m))) {
							r = m;
							break;
						}
					}
					if (r == null)
						throw rejectArguments(new NoSuchMethodException(getMessage(this, "No method name '" + mName + "' in class '" + result.clazz.toGenericString() + "'")), result.thisAsm);
					return r;
				}

				/** {@inheritDoc} */
				@Override
				default void processElement(IElement.Result<A, Method> result) {
					processMethod(new Result<>(result));
				}


				/* SECTION static classes */

				class Result<A extends Annotation> extends IElement.Result<A, Method> {
					/* SECTION constructors */

					protected Result(IElement.Result<? extends A, Method> result) { super(result); }

					protected Result(Result<? extends A> c) { this((IElement.Result<? extends A, Method>) c); }
				}
			}
		}
	}
}
