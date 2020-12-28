package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.MarkersTemplate;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface IIdentifier
		extends CharSequence {
	@NonNls
	String getNamespace();

	static ITuple2<String, String> decompose(CharSequence charSequence) {
		String[] decomposed = StaticHolder.getSeparatorPattern().split(charSequence, 2);
		if (decomposed.length == 2)
			return ImmutableTuple2.of(decomposed[0], decomposed[1]);
		throw new IllegalArgumentException(
				new LogMessageBuilder()
						.addMarkers(StaticHolder::getClassMarker)
						.addKeyValue("charSequence", charSequence)
						.addMessages(() -> StaticHolder.getResourceBundle().getString("decompose.fail"))
						.build()
		);
	}

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

	static String asString(IIdentifier instance) {
		return instance.getNamespace() + StaticHolder.getSeparator() + instance.getName();
	}

	@NonNls
	String getName();

	enum StaticHolder {
		;

		public static final char SEPARATOR_CHAR = ':';
		public static final @NonNls String SEPARATOR = "" + SEPARATOR_CHAR;
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
		private static final Pattern SEPARATOR_PATTERN = Pattern.compile(getSeparator(), Pattern.LITERAL);
		private static final Marker CLASS_MARKER =
				MarkersTemplate.addReferences(UtilitiesMarkers.getInstance().getClassMarker(),
						UtilitiesMarkers.getInstance().getMarkerStructure());

		private static final @Immutable @NonNls Map<String, Function<@Nonnull IIdentifier, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull IIdentifier, @Nullable ?>>builder()
						.put("namespace", IIdentifier::getNamespace)
						.put("name", IIdentifier::getName)
						.build();

		public static char getSeparatorChar() {
			return SEPARATOR_CHAR;
		}

		public static String getSeparator() {
			return SEPARATOR;
		}

		public static Pattern getSeparatorPattern() {
			return SEPARATOR_PATTERN;
		}

		public static @Immutable @NonNls Map<String, Function<@Nonnull IIdentifier, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }

		public static Marker getClassMarker() { return CLASS_MARKER; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
