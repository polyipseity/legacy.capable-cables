package $group__.utilities;

import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.MarkersTemplate;
import org.slf4j.Marker;

public final class UtilitiesMarkers extends MarkersTemplate {
	private static final UtilitiesMarkers INSTANCE = Singleton.getSingletonInstance(UtilitiesMarkers.class);

	private final Marker markerThrowable;
	private final Marker markerRender;
	private final Marker markerOpenGL;
	private final Marker markerEvent;
	private final Marker markerExtension;

	{
		markerThrowable = getMarker("throwable");
		markerRender = getMarker("render");
		markerOpenGL = addReferences(getMarker("OpenGL"), getMarkerRender());
		markerEvent = getMarker("event");
		markerExtension = getMarker("extension");
	}

	private UtilitiesMarkers() { super(UtilitiesConstants.MODULE_NAME, UtilitiesConfiguration.getInstance().getLogger()); }

	public static UtilitiesMarkers getInstance() { return INSTANCE; }

	public Marker getMarkerThrowable() { return markerThrowable; }

	public Marker getMarkerRender() { return markerRender; }

	public Marker getMarkerOpenGL() { return markerOpenGL; }

	public Marker getMarkerEvent() { return markerEvent; }

	public Marker getMarkerExtension() { return markerExtension; }
}
