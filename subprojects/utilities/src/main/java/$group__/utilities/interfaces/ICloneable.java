package $group__.utilities.interfaces;

import $group__.utilities.DynamicUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.UtilitiesConfiguration;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICloneable
		extends Cloneable {
	MethodHandle CLONE_METHOD_HANDLE = ThrowableUtilities.Try.call(() ->
			DynamicUtilities.IMPL_LOOKUP.findVirtual(Object.class, "clone", MethodType.methodType(Object.class)), UtilitiesConfiguration.INSTANCE.getLogger())
			.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);

	@SuppressWarnings("unchecked")
	static <T extends Cloneable> T cloneUnchecked(T obj) {
		// TODO Java 9 - use LambdaMetaFactory
		return ThrowableUtilities.Try.call(() ->
				(T) CLONE_METHOD_HANDLE.invoke(obj), UtilitiesConfiguration.INSTANCE.getLogger())
				.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
	}

	@SuppressWarnings("override")
	ICloneable clone() throws CloneNotSupportedException;
}
