package $group__.ui;

import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.MarkerUtilitiesTemplate;
import org.slf4j.Marker;

public final class UIMarkers extends MarkerUtilitiesTemplate {
	private static final UIMarkers INSTANCE = Singleton.getSingletonInstance(UIMarkers.class, UIConfiguration.getInstance().getLogger());

	private final Marker markerShape;
	private final Marker markerParser;
	private final Marker markerJAXB;
	private final Marker markerUI;
	private final Marker markerUIInfrastructure;
	private final Marker markerUIView;
	private final Marker markerUIViewModel;
	private final Marker markerUIModel;
	private final Marker markerUIEvent;
	private final Marker markerUIComponent;
	private final Marker markerUIComponentRenderer;

	{
		// COMMENT structures
		markerShape = addReferences(getMarker("shape"), getMarkerStructure());
		// COMMENT parsing
		markerParser = getMarker("parser");
		markerJAXB = addReferences(getMarker("JAXB"), getMarkerParser());
		// COMMENT UI
		markerUI = getMarker("UI");
		markerUIInfrastructure = addReferences(getMarker("UI infrastructure"), getMarkerUI());
		markerUIView = addReferences(getMarker("UI view"), getMarkerUIInfrastructure());
		markerUIViewModel = addReferences(getMarker("UI view-model"), getMarkerUIInfrastructure());
		markerUIModel = addReferences(getMarker("UI model"), getMarkerUIInfrastructure());
		markerUIEvent = addReferences(getMarker("UI event"), getMarkerUIView());
		// COMMENT UI component
		markerUIComponent = addReferences(getMarker("UI component"), getMarkerUIView());
		markerUIComponentRenderer = addReferences(getMarker("UI component renderer"), getMarkerUIComponent());
	}

	private UIMarkers() { super(UIConstants.MODULE_NAME, UIConfiguration.getInstance().getLogger()); }

	public static UIMarkers getInstance() { return INSTANCE; }

	public Marker getMarkerShape() { return markerShape; }

	public Marker getMarkerParser() { return markerParser; }

	public Marker getMarkerUI() { return markerUI; }

	public Marker getMarkerUIInfrastructure() { return markerUIInfrastructure; }

	public Marker getMarkerUIView() { return markerUIView; }

	public Marker getMarkerUIViewModel() { return markerUIViewModel; }

	public Marker getMarkerUIModel() { return markerUIModel; }

	public Marker getMarkerUIComponent() { return markerUIComponent; }

	public Marker getMarkerUIComponentRenderer() { return markerUIComponentRenderer; }

	public Marker getMarkerJAXB() { return markerJAXB; }

	public Marker getMarkerUIEvent() { return markerUIEvent; }
}
