package $group__.ui;

import $group__.utilities.templates.MarkerUtilitiesTemplate;

public final class UIMarkers extends MarkerUtilitiesTemplate {
	public static final UIMarkers INSTANCE = new UIMarkers();

	private UIMarkers() { super(UIConstants.DISPLAY_NAME, UIConfiguration.INSTANCE.getLogger()); }
}
