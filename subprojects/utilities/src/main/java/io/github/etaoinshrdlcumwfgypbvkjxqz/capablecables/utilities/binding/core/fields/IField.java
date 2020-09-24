package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.MarkersTemplate;
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
				MarkersTemplate.addReferences(UtilitiesMarkers.getInstance().getClassMarker(),
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
