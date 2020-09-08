package $group__.utilities.interfaces;

import $group__.utilities.DynamicUtilities;
import $group__.utilities.ThrowableUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodType;
import java.util.function.Function;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICloneable
		extends Cloneable {
	Logger LOGGER = LogManager.getLogger();
	Function<Object, Object> CLONE_FUNCTION = ThrowableUtilities.Try.call(() ->
			DynamicUtilities.InvocationUtilities.LambdaMetaFactoryUtilities
					.makeFunction(DynamicUtilities.IMPL_LOOKUP.findVirtual(Object.class, "clone", MethodType.methodType(Object.class)),
							Object.class, Object.class), LOGGER)
			.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);

	@SuppressWarnings("unchecked")
	static <T extends Cloneable> T cloneUnchecked(T obj) {
		return (T) CLONE_FUNCTION.apply(obj); // COMMENT should be safe
	}

	ICloneable clone() throws CloneNotSupportedException;
}
