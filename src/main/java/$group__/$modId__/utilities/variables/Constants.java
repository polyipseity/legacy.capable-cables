package $group__.$modId__.utilities.variables;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.HashSet;

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
			GROUP = "${group}";


	public static final ImmutableMap<Class<?>, Object> PRIMITIVE_DATA_TYPES = ImmutableMap.<Class<?>, Object>builder()
			.put(int.class, 0)
			.put(float.class, 0F)
			.put(double.class, 0D)
			.put(long.class, 0L)
			.put(byte.class, (byte) 0)
			.put(short.class, (short) 0)
			.put(boolean.class, false)
			.put(char.class, '\u0000').build();
	public static final ImmutableSet<Class<?>> PRIMITIVE_TYPES;


	/* SECTION static initializer */

	static {
		HashSet<Class<?>> primitiveDataTypeKeys = Sets.newHashSet(PRIMITIVE_DATA_TYPES.keySet());
		primitiveDataTypeKeys.add(void.class);
		PRIMITIVE_TYPES = ImmutableSet.copyOf(primitiveDataTypeKeys);
	}
}
