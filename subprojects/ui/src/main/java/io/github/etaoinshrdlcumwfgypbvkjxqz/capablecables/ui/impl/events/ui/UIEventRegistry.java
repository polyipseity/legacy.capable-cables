package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public final class UIEventRegistry extends Registry<INamespacePrefixedString, Class<? extends IUIEvent>> {
	private static final Supplier<UIEventRegistry> INSTANCE = Suppliers.memoize(UIEventRegistry::new);
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	protected UIEventRegistry() {
		super(false, UIConfiguration.getInstance().getLogger());
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
		return Optional.ofNullable(getInstance().getData().get(event.getType()))
				.map(RegistryObject::getValue)
				.filter(ec -> ec.isInstance(event))
				.isPresent();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static UIEventRegistry getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
}
