package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public final class UIEventRegistry
		extends AbstractRegistry<IIdentifier, Class<? extends IUIEvent>> {
	private static final Supplier<@Nonnull UIEventRegistry> INSTANCE = Suppliers.memoize(UIEventRegistry::new);
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private static final long serialVersionUID = 321572769828882690L;

	private final ConcurrentMap<IIdentifier, IRegistryObjectInternal<? extends Class<? extends IUIEvent>>> data =
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).makeMap();

	private UIEventRegistry() {
		super(false);
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
				.map(IRegistryObject::getValue)
				.filter(ec -> ec.isInstance(event))
				.isPresent();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Map<IIdentifier, IRegistryObjectInternal<? extends Class<? extends IUIEvent>>> getData() {
		return data;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}

	public static UIEventRegistry getInstance() { return INSTANCE.get(); }
}
