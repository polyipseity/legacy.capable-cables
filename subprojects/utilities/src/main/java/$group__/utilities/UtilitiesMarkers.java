package $group__.utilities;

import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.MarkerUtilitiesTemplate;
import org.slf4j.Marker;

public final class UtilitiesMarkers extends MarkerUtilitiesTemplate {
	private static final UtilitiesMarkers INSTANCE = Singleton.getSingletonInstance(UtilitiesMarkers.class, UtilitiesConfiguration.getInstance().getLogger());

	private final Marker markerRender;
	private final Marker markerOpenGL;

	{
		markerRender = getMarker("render");
		markerOpenGL = addReferences(getMarker("OpenGL"), getMarkerRender());
	}

	private UtilitiesMarkers() { super(UtilitiesConstants.MODULE_NAME, UtilitiesConfiguration.getInstance().getLogger()); }

	public static UtilitiesMarkers getInstance() { return INSTANCE; }

	public Marker getMarkerRender() { return markerRender; }

	public Marker getMarkerOpenGL() { return markerOpenGL; }
}
