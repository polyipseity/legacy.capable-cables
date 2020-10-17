package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.MarkersTemplate;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface INamespacePrefixedString {
	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	static String[] decompose(CharSequence string) {
		String[] ss = string.toString().split(Pattern.quote(StaticHolder.SEPARATOR), 2);
		switch (ss.length) {
			case 2:
				return ss;
			default:
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(StaticHolder::getClassMarker)
								.addKeyValue("string", string)
								.addMessages(() -> StaticHolder.getResourceBundle().getString("decompose.fail"))
								.build()
				);
		}
	}

	default String asString() { return getNamespace() + StaticHolder.SEPARATOR + getPath(); }

	@NonNls
	String getNamespace();

	@NonNls
	String getPath();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
		public static final @NonNls String SEPARATOR = ":";
		private static final Marker CLASS_MARKER =
				MarkersTemplate.addReferences(UtilitiesMarkers.getInstance().getClassMarker(),
						UtilitiesMarkers.getInstance().getMarkerStructure());
		private static final ImmutableList<Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES = ImmutableList.of(
				INamespacePrefixedString::getNamespace, INamespacePrefixedString::getPath);
		private static final ImmutableMap<String, Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("namespace", "path"),
				getObjectVariables()));

		public static ImmutableList<Function<? super INamespacePrefixedString, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super INamespacePrefixedString, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }

		public static Marker getClassMarker() { return CLASS_MARKER; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
