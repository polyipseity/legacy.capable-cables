package $group__.ui;

import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.MarkerUtilitiesTemplate;

public final class UIMarkers extends MarkerUtilitiesTemplate {
	private static final UIMarkers INSTANCE = Singleton.getSingletonInstance(UIMarkers.class, UIConfiguration.getInstance().getLogger());

	private UIMarkers() { super(UIConstants.MODULE_NAME, UIConfiguration.getInstance().getLogger()); }

	public static UIMarkers getInstance() { return INSTANCE; }
}
