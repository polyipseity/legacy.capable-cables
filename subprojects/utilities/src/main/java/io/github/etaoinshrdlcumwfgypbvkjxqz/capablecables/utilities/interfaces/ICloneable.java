package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICloneable
		extends Cloneable {
	@SuppressWarnings("override")
	ICloneable clone() throws CloneNotSupportedException;

	enum StaticHolder {
		;

		private static final MethodHandle CLONE_METHOD_HANDLE;

		static {
			try {
				CLONE_METHOD_HANDLE = DynamicUtilities.IMPL_LOOKUP.findVirtual(Object.class, "clone", MethodType.methodType(Object.class));
			} catch (NoSuchMethodException | IllegalAccessException e) {
				throw ThrowableUtilities.propagate(e);
			}
		}

		@SuppressWarnings("unchecked")
		static <T extends Cloneable> T cloneUnchecked(T obj) {
			// TODO Java 9 - use LambdaMetaFactory
			try {
				return (T) getCloneMethodHandle().invoke(obj);
			} catch (Throwable throwable) {
				throw ThrowableUtilities.propagate(throwable);
			}
		}

		@SuppressWarnings("SameReturnValue")
		private static MethodHandle getCloneMethodHandle() { return CLONE_METHOD_HANDLE; }
	}
}
