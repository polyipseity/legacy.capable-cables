package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICloneable
		extends Cloneable {
	@SuppressWarnings("unchecked")
	static <T extends Cloneable> T clone(T obj) {
		// TODO Java 9 - use LambdaMetaFactory
		try {
			return (T) (Object) StaticHolder.getCloneMethodHandle().invokeExact((Object) obj);
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}

	ICloneable clone()
			throws CloneNotSupportedException;

	enum StaticHolder {
		;

		private static final MethodHandle CLONE_METHOD_HANDLE;

		static {
			try {
				CLONE_METHOD_HANDLE = InvokeUtilities.getImplLookup().findVirtual(Object.class, "clone", MethodType.methodType(Object.class));
			} catch (NoSuchMethodException | IllegalAccessException e) {
				throw ThrowableUtilities.propagate(e);
			}
		}

		@SuppressWarnings("SameReturnValue")
		private static MethodHandle getCloneMethodHandle() { return CLONE_METHOD_HANDLE; }
	}
}
