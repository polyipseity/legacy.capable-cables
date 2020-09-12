package $group__.utilities.interfaces;

import $group__.utilities.ThrowableUtilities;
import $group__.utilities.collections.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface INamespacePrefixedString {
	String SEPARATOR = ":";
	String DEFAULT_NAMESPACE = "minecraft";
	String DEFAULT_PREFIX = DEFAULT_NAMESPACE + SEPARATOR;

	ImmutableList<Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES = ImmutableList.of(
			INamespacePrefixedString::getNamespace, INamespacePrefixedString::getPath);
	ImmutableMap<String, Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(OBJECT_VARIABLES.size(),
			ImmutableList.of("namespace", "path"),
			OBJECT_VARIABLES));

	static String[] decompose(String string) {
		String[] ss = string.split(Pattern.quote(SEPARATOR), 2);
		switch (ss.length) {
			case 2:
				return ss;
			case 1:
				return new String[]{DEFAULT_NAMESPACE, ss[0]};
			default:
				throw ThrowableUtilities.BecauseOf.illegalArgument("Cannot decompose string",
						"ss", Arrays.toString(ss),
						"string", string);
		}
	}

	default String asString() { return getNamespace() + SEPARATOR + getPath(); }

	String getNamespace();

	String getPath();
}
