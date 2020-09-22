package $group__.ui.events.ui;

import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.Registry;
import $group__.utilities.templates.CommonConfigurationTemplate;

import java.util.Optional;
import java.util.ResourceBundle;

public final class UIEventRegistry extends Registry<INamespacePrefixedString, Class<? extends IUIEvent>> {
	private static final UIEventRegistry INSTANCE = new UIEventRegistry();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	protected UIEventRegistry() {
		super(false, UIConfiguration.getInstance().getLogger());
		PreconditionUtilities.requireRunOnceOnly();
	}

	public static void checkEvent(IUIEvent event)
			throws IllegalArgumentException {
		if (!isEventValid(event))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIEvent)
							.addKeyValue("event", event)
							.addMessages(() -> getResourceBundle().getString("check.event.unregistered"))
							.build()
			);
	}

	public static boolean isEventValid(IUIEvent event) {
		return Optional.ofNullable(getINSTANCE().getData().get(event.getType()))
				.map(RegistryObject::getValue)
				.filter(ec -> ec.isInstance(event))
				.isPresent();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static UIEventRegistry getINSTANCE() { return INSTANCE; }
}
