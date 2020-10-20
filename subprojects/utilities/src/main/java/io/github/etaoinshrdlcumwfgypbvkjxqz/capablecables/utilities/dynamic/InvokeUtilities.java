package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public enum InvokeUtilities {
	;


	public static final int TRUSTED_LOOKUP_MODES = 15;
	private static final MethodHandles.Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
	private static final MethodHandles.Lookup IMPL_LOOKUP;

	static {
		IMPL_LOOKUP = Arrays.stream(MethodHandles.Lookup.class.getDeclaredFields()).unordered()
				.filter(f -> MethodHandles.Lookup.class.equals(f.getType()))
				.map(f -> {
					f.setAccessible(true);
					MethodHandles.Lookup ret;
					try {
						ret = (MethodHandles.Lookup) getPublicLookup().unreflectGetter(f).invokeExact();
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
					f.setAccessible(false);
					return ret;
				})
				.filter(lookup -> lookup.lookupModes() == getTrustedLookupModes())
				.findAny()
				.orElseThrow(() -> ThrowableUtilities.propagate(new NoSuchFieldException()));
	}

	@SuppressWarnings("unchecked")
	public static <T> Consumer<T> makeConsumer(MethodHandles.Lookup lookup, MethodHandle method,
	                                           Class<T> inputType) throws Throwable {
		return (Consumer<T>) LambdaMetafactory.metafactory(lookup,
				"accept",
				MethodType.methodType(Consumer.class),
				MethodType.methodType(void.class, Object.class),
				method,
				MethodType.methodType(void.class, inputType))
				.getTarget()
				.invokeExact();
	}

	@SuppressWarnings("unchecked")
	public static <T, U> BiConsumer<T, U> makeBiConsumer(MethodHandles.Lookup lookup, MethodHandle method,
	                                                     Class<T> inputType1, Class<U> inputType2) throws Throwable {
		return (BiConsumer<T, U>) LambdaMetafactory.metafactory(lookup,
				"accept",
				MethodType.methodType(BiConsumer.class),
				MethodType.methodType(void.class, Object.class, Object.class),
				method,
				MethodType.methodType(void.class, inputType1, inputType2))
				.getTarget()
				.invokeExact();
	}

	@SuppressWarnings("unchecked")
	public static <T, R> Function<T, R> makeFunction(MethodHandles.Lookup lookup, MethodHandle method,
	                                                 Class<R> returnType, Class<T> inputType) throws Throwable {
		return (Function<T, R>) LambdaMetafactory.metafactory(lookup,
				"apply",
				MethodType.methodType(Function.class),
				MethodType.methodType(Object.class, Object.class),
				method,
				MethodType.methodType(returnType, inputType))
				.getTarget()
				.invokeExact();
	}

	@SuppressWarnings("unchecked")
	public static <T, U, R> BiFunction<T, U, R> makeBiFunction(MethodHandles.Lookup lookup, MethodHandle method,
	                                                           Class<R> returnType, Class<T> inputType1, Class<U> inputType2) throws Throwable {
		return (BiFunction<T, U, R>) LambdaMetafactory.metafactory(lookup,
				"apply",
				MethodType.methodType(BiFunction.class),
				MethodType.methodType(Object.class, Object.class, Object.class),
				method,
				MethodType.methodType(returnType, inputType1, inputType2))
				.getTarget()
				.invokeExact();
	}

	public static MethodHandles.Lookup getPublicLookup() {
		return PUBLIC_LOOKUP;
	}

	public static MethodHandles.Lookup getImplLookup() {
		return IMPL_LOOKUP;
	}

	private static int getTrustedLookupModes() {
		return TRUSTED_LOOKUP_MODES;
	}
}
