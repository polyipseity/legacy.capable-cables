package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.MarkersTemplate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface INamespacePrefixedString {
	default String asString() { return getNamespace() + StaticHolder.SEPARATOR + getPath(); }

	@NonNls
	String getNamespace();

	@NonNls
	String getPath();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
		@NonNls
		public static final String SEPARATOR = ":";
		@NonNls
		public static final String DEFAULT_NAMESPACE = "minecraft";
		@NonNls
		public static final String DEFAULT_PREFIX = DEFAULT_NAMESPACE + SEPARATOR;
		private static final Marker CLASS_MARKER =
				MarkersTemplate.addReferences(UtilitiesMarkers.getInstance().getClassMarker(INamespacePrefixedString.class),
						UtilitiesMarkers.getInstance().getMarkerStructure());
		private static final ImmutableList<Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES = ImmutableList.of(
				INamespacePrefixedString::getNamespace, INamespacePrefixedString::getPath);
		private static final ImmutableMap<String, Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(getObjectVariables().size(),
				ImmutableList.of("namespace", "path"),
				getObjectVariables()));

		public static ImmutableList<Function<? super INamespacePrefixedString, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super INamespacePrefixedString, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }

		public static String[] decompose(String string) {
			String[] ss = string.split(Pattern.quote(SEPARATOR), 2);
			switch (ss.length) {
				case 2:
					return ss;
				case 1:
					return new String[]{DEFAULT_NAMESPACE, ss[0]};
				default:
					throw new IllegalArgumentException(
							new LogMessageBuilder()
									.addMarkers(StaticHolder::getClassMarker)
									.addKeyValue("string", string)
									.addMessages(() -> getResourceBundle().getString("decompose.fail"))
									.build()
					);
			}
		}

		public static Marker getClassMarker() { return CLASS_MARKER; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
