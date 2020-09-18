package $group__.utilities;

import $group__.utilities.templates.MarkerUtilitiesTemplate;

public final class UtilitiesMarkers extends MarkerUtilitiesTemplate {
	public static final UtilitiesMarkers INSTANCE = new UtilitiesMarkers();

	private UtilitiesMarkers() { super(UtilitiesConstants.DISPLAY_NAME, UtilitiesConfiguration.INSTANCE.getLogger()); }
}
