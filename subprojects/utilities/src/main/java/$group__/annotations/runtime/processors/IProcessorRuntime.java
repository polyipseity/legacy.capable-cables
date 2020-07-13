package $group__.annotations.runtime.processors;

import $group__.traits.IStruct;
import $group__.utilities.helpers.Dynamics;
import $group__.utilities.helpers.specific.Throwables;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static java.lang.Class.forName;

public interface IProcessorRuntime<A extends Annotation> {
	static String makeMessage(IProcessorRuntime<?> processor, @Nullable String msg) { return "Processing annotation '" + processor.annotationType() + '\'' + (msg == null || msg.isEmpty() ? "" : ": " + msg); }


	Class<A> annotationType();


	boolean isProcessed();

	void process(ASMDataTable asm, @Nullable Logger logger);


	interface IClass<A extends Annotation> extends IProcessorRuntime<A> {
		@Override
		default void process(ASMDataTable asm, @Nullable Logger logger) {
			Set<ASMDataTable.ASMData> thisAsm = asm.getAll(annotationType().getName());
			thisAsm.forEach(t -> processClass(new Result(asm, thisAsm, t,
					Throwables.tryCall(() -> forName(t.getClassName(), false, getClass().getClassLoader()), logger).orElseThrow(Throwables::rethrowCaughtThrowableStatic)), logger));
		}

		void processClass(Result result, @Nullable Logger logger);


		interface IElement<A extends Annotation, AE extends AnnotatedElement> extends IClass<A> {
			@Override
			default void processClass(IClass.Result result, @Nullable Logger logger) {
				AE ae = findElement(result, logger);
				processElement(new Result<>(result, ae, ae.getDeclaredAnnotationsByType(annotationType())), logger);
			}

			AE findElement(IClass.Result result, @Nullable Logger logger);

			void processElement(Result<A, AE> result, @Nullable Logger logger);


			interface IMethod<A extends Annotation> extends IElement<A, Method> {
				@Override
				default Method findElement(IClass.Result result, @Nullable Logger logger) {
					String mName = result.currentAsm.getObjectName();
					@Nullable Method r =
							Arrays.stream(result.clazz.getDeclaredMethods()).filter(m -> mName.equals(Dynamics.getMethodNameDescriptor(m))).findFirst().orElse(null);
					if (r == null)
						throw Throwables.rejectArguments(new NoSuchMethodException(makeMessage(this, "No method name '" + mName + "' in class '" + result.clazz.toGenericString() + '\'')),
								result.thisAsm);
					return r;
				}

				@Override
				default void processElement(IElement.Result<A, Method> result, @Nullable Logger logger) { processMethod(new Result<>(result), logger); }

				void processMethod(Result<A> result, @Nullable Logger logger);


				class Result<A extends Annotation> extends IElement.Result<A, Method> {
					protected Result(Result<? extends A> c) { this((IElement.Result<? extends A, Method>) c); }

					protected Result(IElement.Result<? extends A, Method> result) { super(result); }
				}
			}

			class Result<A extends Annotation, AE extends AnnotatedElement> extends IClass.Result {
				public final AE element;
				public final A[] annotations;


				protected Result(Result<? extends A, ? extends AE> c) { this(c, c.element, c.annotations); }

				protected Result(IClass.Result result, AE element, A[] annotations) {
					super(result);
					this.element = element;
					this.annotations = annotations;
				}
			}
		}

		class Result implements IStruct {
			public final ASMDataTable asm;
			public final Set<ASMDataTable.ASMData> thisAsm;
			public final ASMDataTable.ASMData currentAsm;
			public final Class<?> clazz;


			protected Result(Result c) { this(c.asm, c.thisAsm, c.currentAsm, c.clazz); }

			protected Result(ASMDataTable asm, Set<ASMDataTable.ASMData> thisAsm, ASMDataTable.ASMData currentAsm,
			                 Class<?> clazz) {
				this.asm = asm;
				this.thisAsm = thisAsm;
				this.currentAsm = currentAsm;
				this.clazz = clazz;
			}
		}
	}
}
