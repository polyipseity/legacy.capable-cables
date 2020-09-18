package $group__;

import $group__.utilities.templates.MarkerUtilitiesTemplate;

public final class ModMarkers extends MarkerUtilitiesTemplate {
	public static final ModMarkers INSTANCE = new ModMarkers();

	private ModMarkers() { super(ModConstants.DISPLAY_NAME, ModConfiguration.LOGGER); }
}
