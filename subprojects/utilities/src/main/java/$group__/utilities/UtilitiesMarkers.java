package $group__.utilities;

import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.MarkerUtilitiesTemplate;

public final class UtilitiesMarkers extends MarkerUtilitiesTemplate {
	public static final UtilitiesMarkers INSTANCE = Singleton.getSingletonInstance(UtilitiesMarkers.class, UtilitiesConfiguration.getInstance().getLogger());

	private UtilitiesMarkers() { super(UtilitiesConstants.MODULE_NAME, UtilitiesConfiguration.getInstance().getLogger()); }
}
