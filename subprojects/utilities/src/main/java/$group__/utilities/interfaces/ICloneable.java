package $group__.utilities.interfaces;

import $group__.utilities.DynamicUtilities;
import $group__.utilities.ThrowableUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICloneable
		extends Cloneable {
	Logger LOGGER = LogManager.getLogger();
	MethodHandle METHOD_HANDLE_CLONE = ThrowableUtilities.Try.call(() ->
			DynamicUtilities.IMPL_LOOKUP.findVirtual(Object.class, "clone", MethodType.methodType(Object.class)), LOGGER)
			.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);

	@SuppressWarnings("unchecked")
	static <T extends Cloneable> T cloneUnchecked(T obj) {
		return ThrowableUtilities.Try.call(() ->
				(T) METHOD_HANDLE_CLONE.invoke(obj), LOGGER)
				.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
	}

	ICloneable clone() throws CloneNotSupportedException;
}
