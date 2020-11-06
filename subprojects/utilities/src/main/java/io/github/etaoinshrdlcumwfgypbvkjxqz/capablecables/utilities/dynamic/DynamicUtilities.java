package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import net.jodah.typetools.TypeResolver;
import sun.misc.Unsafe;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum DynamicUtilities {
	;

	private static final Unsafe UNSAFE;

	static {
		UNSAFE = Arrays.stream(Unsafe.class.getDeclaredFields()).unordered()
				.filter(f -> Unsafe.class.equals(f.getType()))
				.map(f -> {
					try {
						return (Unsafe) InvokeUtilities.getImplLookup().unreflectGetter(f).invokeExact();
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
				})
				.findAny()
				.orElseThrow(() -> ThrowableUtilities.propagate(new NoSuchFieldException()));
	}

	public static Unsafe getUnsafe() {
		return UNSAFE;
	}

	public enum Extensions {
		;

		@SuppressWarnings("unchecked")
		public static Optional<Class<?>>[] wrapTypeResolverResults(Class<?>... results) {
			return Arrays.stream(results)
					.map(Extensions::wrapTypeResolverResult)
					.toArray(Optional[]::new);
		}

		public static Optional<Class<?>> wrapTypeResolverResult(@Nullable Class<?> result) {
			return Optional.<Class<?>>ofNullable(result)
					.filter(Predicate.isEqual(TypeResolver.Unknown.class).negate());
		}
	}
}
