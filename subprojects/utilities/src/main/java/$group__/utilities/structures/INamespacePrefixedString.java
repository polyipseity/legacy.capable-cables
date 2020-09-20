package $group__.utilities.structures;

import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.UtilitiesConfiguration;
import $group__.utilities.UtilitiesMarkers;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.templates.MarkerUtilitiesTemplate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface INamespacePrefixedString {
	default String asString() { return getNamespace() + StaticHolder.getSeparator() + getPath(); }

	@NonNls
	String getNamespace();

	@NonNls
	String getPath();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
		private static final Marker CLASS_MARKER =
				MarkerUtilitiesTemplate.addReferences(UtilitiesMarkers.getInstance().getClassMarker(INamespacePrefixedString.class),
						UtilitiesMarkers.getInstance().getMarkerStructure());
		@NonNls
		private static final String SEPARATOR = ":";
		@NonNls
		private static final String DEFAULT_NAMESPACE = "minecraft";
		@NonNls
		private static final String DEFAULT_PREFIX = getDefaultNamespace() + getSeparator();
		private static final ImmutableList<Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES = ImmutableList.of(
				INamespacePrefixedString::getNamespace, INamespacePrefixedString::getPath);
		private static final ImmutableMap<String, Function<? super INamespacePrefixedString, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(getObjectVariables().size(),
				ImmutableList.of("namespace", "path"),
				getObjectVariables()));

		public static String getDefaultPrefix() { return DEFAULT_PREFIX; }

		public static ImmutableList<Function<? super INamespacePrefixedString, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super INamespacePrefixedString, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }

		public static String[] decompose(String string) {
			String[] ss = string.split(Pattern.quote(getSeparator()), 2);
			switch (ss.length) {
				case 2:
					return ss;
				case 1:
					return new String[]{getDefaultNamespace(), ss[0]};
				default:
					throw ThrowableUtilities.logAndThrow(
							new IllegalArgumentException(
									new LogMessageBuilder()
											.addMarkers(StaticHolder::getClassMarker)
											.addKeyValue("string", string)
											.addMessages(() -> getResourceBundle().getString("decompose.fail"))
											.build()
							),
							UtilitiesConfiguration.getInstance().getLogger()
					);
			}
		}

		public static String getSeparator() { return SEPARATOR; }

		public static String getDefaultNamespace() { return DEFAULT_NAMESPACE; }

		public static Marker getClassMarker() { return CLASS_MARKER; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
