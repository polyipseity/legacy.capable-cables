package $group__.utilities.binding.core.fields;

import $group__.utilities.LogMessageBuilder;
import $group__.utilities.UtilitiesConfiguration;
import $group__.utilities.UtilitiesMarkers;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.templates.MarkersTemplate;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.ResourceBundle;

public interface IField<T> {
	Optional<? extends T> getValue();

	void setValue(@Nullable T value);

	enum StaticHolder {
		;

		private static final Marker CLASS_MARKER =
				MarkersTemplate.addReferences(UtilitiesMarkers.getInstance().getClassMarker(IField.class),
						UtilitiesMarkers.getInstance().getMarkerStructure());
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

		public static <T> T getValueNonnull(IField<? extends T> field) {
			return field.getValue()
					.orElseThrow(() -> new IllegalArgumentException(
							new LogMessageBuilder()
									.addMarkers(StaticHolder::getClassMarker)
									.addKeyValue("field", field)
									.addMessages(() -> getResourceBundle().getString("value.get.null"))
									.build()
					));
		}

		public static Marker getClassMarker() { return CLASS_MARKER; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
