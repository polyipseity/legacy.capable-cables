package $group__.annotations.runtime.processors;

import $group__.annotations.runtime.ExternalToImmutableMethod;
import $group__.commons.events.AnnotationProcessingEvent;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;

import static $group__.annotations.runtime.processors.IProcessorRuntime.makeMessage;
import static $group__.utilities.concurrent.IImmutablizable.EXTERNAL_ANNOTATIONS_MAP;
import static $group__.utilities.concurrent.IImmutablizable.EXTERNAL_METHOD_MAP;
import static $group__.utilities.helpers.Dynamics.IMPL_LOOKUP;
import static $group__.utilities.helpers.specific.Loggers.EnumMessages.*;
import static $group__.utilities.helpers.specific.Throwables.consumeIfCaughtThrowable;
import static $group__.utilities.helpers.specific.Throwables.tryCall;

public enum ExternalToImmutableMethodProcessor implements IProcessorRuntime.IClass.IElement.IMethod<ExternalToImmutableMethod> {
	/* SECTION enums */
	INSTANCE;


	/* SECTION variables */

	private volatile boolean processed = false;


	/* SECTION static methods */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void process(AnnotationProcessingEvent event) {
		INSTANCE.process(event.getAsm(),
				event.getLogger());
	}


	/* SECTION methods */

	@Override
	public void process(ASMDataTable asm, @Nullable Logger logger) {
		if (isProcessed()) return;
		IMethod.super.process(asm, logger);
		processed = true;
	}

	@Override
	public Class<ExternalToImmutableMethod> annotationType() { return ExternalToImmutableMethod.class; }

	@Override
	public boolean isProcessed() { return processed; }

	@Override
	public void processMethod(Result<ExternalToImmutableMethod> result, @Nullable Logger logger) {
		ExternalToImmutableMethod a = result.annotations[0];
		@Nullable ExternalToImmutableMethod ap;
		@Nullable MethodHandle m = tryCall(() -> IMPL_LOOKUP.unreflect(result.element), logger).orElseGet(() -> {
			consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("to-immutable method", result.element, IMPL_LOOKUP), t)));
			return null;
		});
		if (m == null) return;

		Class<?>[] ks = a.value();
		if (ks.length == 0) {
			logger.warn(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(makeMessage(this, "Method '{}' with " +
					"annotation" +
					" '{}' has no usage"), m, a));
			return;
		}
		EXTERNAL_METHOD_MAP.put(a, m);

		for (Class<?> k : ks) {
			ap = EXTERNAL_ANNOTATIONS_MAP.get(k);
			EXTERNAL_ANNOTATIONS_MAP.put(k, a);
			if (ap == null)
				logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(makeMessage(this, "Registered method " +
						"'{}'" +
						" with annotation '{}' for class '{}'"), m, a, k.toGenericString()));
			else {
				ExternalToImmutableMethod apf = ap;
				logger.warn(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(makeMessage(this, "Replaced previous " +
								"method '{}' with annotation '{}' with method '{}' with annotation '{}' for class " +
								"'{}'"),
						EXTERNAL_METHOD_MAP.get(apf), apf, m, a, k.toGenericString()));
			}
		}
	}
}
