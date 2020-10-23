package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.MarkersTemplate;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;

import java.util.Map;
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

		private static final @Immutable Map<String, Function<INamespacePrefixedString, ?>> OBJECT_VARIABLES_MAP =
				ImmutableMap.<String, Function<INamespacePrefixedString, ?>>builder()
						.put("namespace", INamespacePrefixedString::getNamespace)
						.put("path", INamespacePrefixedString::getPath)
						.build();

		public static @Immutable Map<String, Function<INamespacePrefixedString, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }

		public static Marker getClassMarker() { return CLASS_MARKER; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
