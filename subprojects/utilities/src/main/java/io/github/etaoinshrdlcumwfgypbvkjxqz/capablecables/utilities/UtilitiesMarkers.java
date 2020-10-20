package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.MarkersTemplate;
import org.slf4j.Marker;

import java.util.function.Supplier;

public final class UtilitiesMarkers extends MarkersTemplate {
	private static final Supplier<UtilitiesMarkers> INSTANCE = Suppliers.memoize(UtilitiesMarkers::new);

	private final Marker markerLogging;
	private final Marker markerThrowable;
	private final Marker markerBinding;
	private final Marker markerRender;
	private final Marker markerOpenGL;
	private final Marker markerEvent;
	private final Marker markerExtension;

	{
		markerLogging = getMarker("logging");
		markerThrowable = getMarker("throwable");
		markerBinding = getMarker("binding");
		markerRender = getMarker("render");
		markerOpenGL = addReferences(getMarker("OpenGL"), getMarkerRender());
		markerEvent = getMarker("event");
		markerExtension = getMarker("extension");
	}

	private UtilitiesMarkers() { super(UtilitiesConstants.getModuleName()); }

	public static UtilitiesMarkers getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	public Marker getMarkerLogging() { return markerLogging; }

	public Marker getMarkerThrowable() { return markerThrowable; }

	public Marker getMarkerBinding() { return markerBinding; }

	public Marker getMarkerRender() { return markerRender; }

	public Marker getMarkerOpenGL() { return markerOpenGL; }

	public Marker getMarkerEvent() { return markerEvent; }

	public Marker getMarkerExtension() { return markerExtension; }
}
