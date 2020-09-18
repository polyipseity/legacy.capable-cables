package $group__.utilities;

import $group__.utilities.templates.MarkerUtilitiesTemplate;
import org.slf4j.Marker;

public final class UtilitiesMarkers extends MarkerUtilitiesTemplate {
	public static final UtilitiesMarkers INSTANCE = new UtilitiesMarkers();

	private final Marker markerDynamic = getMarker("dynamic");

	private UtilitiesMarkers() { super(UtilitiesConstants.DISPLAY_NAME, UtilitiesConfiguration.INSTANCE.getLogger()); }
}
