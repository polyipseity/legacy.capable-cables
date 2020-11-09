package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.DoubleMath;

import java.math.RoundingMode;

public enum PrimitiveUtilities {
	;


	static final ImmutableMap<Class<?>, Object> PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP =
			ImmutableMap.<Class<?>, Object>builder()
					.put(int.class, 0)
					.put(float.class, 0F)
					.put(double.class, 0D)
					.put(long.class, 0L)
					.put(byte.class, (byte) 0)
					.put(short.class, (short) 0)
					.put(boolean.class, false)
					.put(char.class, '\u0000').build();
	private static final Class<?>[]
			PRIMITIVE_TYPE_ARRAY = {int.class, float.class, double.class, long.class, byte.class, short.class,
			boolean.class, char.class, void.class};
	private static final ImmutableSet<Class<?>>
			PRIMITIVE_TYPE_SET = ImmutableSet.copyOf(getPrimitiveTypeArray());
	private static final Class<?>[] PRIMITIVE_DATA_TYPE_ARRAY = {int.class, float.class, double.class, long.class,
			byte.class, short.class, boolean.class, char.class};
	private static final ImmutableSet<Class<?>> PRIMITIVE_DATA_TYPE_SET =
			ImmutableSet.copyOf(getPrimitiveDataTypeArray());
	private static final ImmutableBiMap<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_BOXED_TYPE_BI_MAP =
			ImmutableBiMap.<Class<?>, Class<?>>builder()
					.put(int.class, Integer.class)
					.put(float.class, Float.class)
					.put(double.class, Double.class)
					.put(long.class, Long.class)
					.put(byte.class, Byte.class)
					.put(short.class, Short.class)
					.put(boolean.class, Boolean.class)
					.put(char.class, Character.class)
					.put(void.class, Void.class).build();

	public static Class<?>[] getPrimitiveTypeArray() {
		return PRIMITIVE_TYPE_ARRAY.clone();
	}

	public static ImmutableSet<Class<?>> getPrimitiveTypeSet() {
		return PRIMITIVE_TYPE_SET;
	}

	public static Class<?>[] getPrimitiveDataTypeArray() {
		return PRIMITIVE_DATA_TYPE_ARRAY.clone();
	}

	public static ImmutableSet<Class<?>> getPrimitiveDataTypeSet() {
		return PRIMITIVE_DATA_TYPE_SET;
	}

	public static ImmutableMap<Class<?>, Object> getPrimitiveDataTypeToDefaultValueMap() {
		return PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP;
	}

	public static ImmutableBiMap<Class<?>, Class<?>> getPrimitiveTypeToBoxedTypeBiMap() {
		return PRIMITIVE_TYPE_TO_BOXED_TYPE_BI_MAP;
	}

	public static long toIntegerExact(double x)
			throws ArithmeticException {
		return DoubleMath.roundToLong(x, RoundingMode.UNNECESSARY);
	}
}
