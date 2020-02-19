package $group__.$modId__.utilities.variables;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraftforge.fml.relauncher.Side;

public enum Constants {
	/* MARK empty */;


	/* SECTION static variables */

	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public static final String
			MOD_ID = "${modId}",
			NAME = "${name}",
			VERSION = "${version}",
			DEPENDENCIES = "${dependencies}",
			ACCEPTED_MINECRAFT_VERSIONS = "${minecraftVersionRange}",
			CERTIFICATE_FINGERPRINT = "${certificateFingerprint}",
			UPDATE_JSON = "${updateJSON}",
			GROUP = "${group}",
			PACKAGE = GROUP + '.' + MOD_ID;

	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public static final String
			COFH_CORE_ID = "cofhcore", COFH_CORE_PACKAGE = "cofh",
			BUILDCRAFT_API_ID = "BuildCraftAPI|core", BUILDCRAFT_API_PACKAGE = "buildcraft";


	public static final int INITIAL_CAPACITY_SCALING_FACTOR = 8;
	public static final int INITIAL_CAPACITY_0 = 0;
	public static final int INITIAL_CAPACITY_1 = 1;
	public static final int INITIAL_CAPACITY_2 = INITIAL_CAPACITY_1 * INITIAL_CAPACITY_SCALING_FACTOR;
	public static final int INITIAL_CAPACITY_3 = INITIAL_CAPACITY_2 * INITIAL_CAPACITY_SCALING_FACTOR;
	public static final int INITIAL_CAPACITY_4 = INITIAL_CAPACITY_3 * INITIAL_CAPACITY_SCALING_FACTOR;


	public static final int
			MULTI_THREAD_THREAD_COUNT = Side.values().length,
			SINGLE_THREAD_THREAD_COUNT = 1;


	public static final Class<?>[]
			PRIMITIVE_TYPE_ARRAY = {int.class, float.class, double.class, long.class, byte.class, short.class, boolean.class, char.class, void.class},
			PRIMITIVE_DATA_TYPE_ARRAY = {int.class, float.class, double.class, long.class, byte.class, short.class, boolean.class, char.class};
	public static final ImmutableSet<Class<?>>
			PRIMITIVE_TYPE_SET = ImmutableSet.copyOf(PRIMITIVE_TYPE_ARRAY),
			PRIMITIVE_DATA_TYPE_SET = ImmutableSet.copyOf(PRIMITIVE_DATA_TYPE_ARRAY);
	public static final ImmutableMap<Class<?>, Object> PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP = new ImmutableMap.Builder<Class<?>, Object>()
			.put(int.class, 0)
			.put(float.class, 0F)
			.put(double.class, 0D)
			.put(long.class, 0L)
			.put(byte.class, (byte) 0)
			.put(short.class, (short) 0)
			.put(boolean.class, false)
			.put(char.class, '\u0000').build();
	public static final ImmutableBiMap<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_BOXED_TYPE_BI_MAP = new ImmutableBiMap.Builder<Class<?>, Class<?>>()
			.put(int.class, Integer.class)
			.put(float.class, Float.class)
			.put(double.class, Double.class)
			.put(long.class, Long.class)
			.put(byte.class, Byte.class)
			.put(short.class, Short.class)
			.put(boolean.class, Boolean.class)
			.put(char.class, Character.class)
			.put(void.class, Void.class).build();
}
