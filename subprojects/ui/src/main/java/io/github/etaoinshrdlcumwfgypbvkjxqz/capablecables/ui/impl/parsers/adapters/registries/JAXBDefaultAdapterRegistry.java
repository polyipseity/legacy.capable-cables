package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBElementAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBObjectAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBIdentityObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import jakarta.xml.bind.JAXBElement;

import java.util.Optional;
import java.util.ResourceBundle;

public class JAXBDefaultAdapterRegistry
		implements IJAXBAdapterRegistry {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final IJAXBObjectAdapterRegistry objectRegistry;
	private final IJAXBElementAdapterRegistry elementRegistry;

	public JAXBDefaultAdapterRegistry(IJAXBObjectAdapterRegistry objectRegistry, IJAXBElementAdapterRegistry elementRegistry) {
		this.objectRegistry = objectRegistry;
		this.elementRegistry = elementRegistry;
	}

	@SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
	@Override
	public <L> Optional<? extends IJAXBAdapter<L, ?>> findFromJAXBAdapter(L jaxbObject) {
		if (IJAXBAdapterRegistry.isOfJAXBSpecialType(jaxbObject))
			return Optional.of(JAXBIdentityObjectAdapter.getInstance()); // COMMENT handles 'String's, primitives, etc.

		if (jaxbObject instanceof JAXBElement)
			return (Optional<? extends IJAXBAdapter<L, ?>>) // COMMENT should be safe
					getElementRegistry().getWithLeftChecked(((JAXBElement<?>) jaxbObject).getName())
							.map(IRegistryObject::getValue);
		return (Optional<? extends IJAXBAdapter<L, ?>>) // COMMENT should be safe
				getObjectRegistry().getWithLeftChecked(jaxbObject.getClass())
						.map(IRegistryObject::getValue);
	}

	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	@Override
	public <R> Optional<? extends IJAXBAdapter<?, R>> findToJAXBAdapter(R object)
			throws IllegalStateException {
		@SuppressWarnings("unchecked") Optional<? extends IJAXBAdapter<?, R>> objectAdapter = (Optional<? extends IJAXBAdapter<?, R>>)
				getObjectRegistry().getWithRightChecked(object.getClass())
						.map(IRegistryObject::getValue);
		@SuppressWarnings("unchecked") Optional<? extends IJAXBAdapter<?, R>> elementAdapter = (Optional<? extends IJAXBAdapter<?, R>>)
				getElementRegistry().getWithRightChecked(object.getClass())
						.map(IRegistryObject::getValue);
		if (objectAdapter.isPresent() && elementAdapter.isPresent())
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerJAXB)
							.addKeyValue("object", object)
							.addMessages(() -> getResourceBundle().getString("adapter.to.find.ambiguous"))
							.build()
			);
		return objectAdapter.isPresent() ? objectAdapter : elementAdapter;
	}

	@Override
	public IJAXBElementAdapterRegistry getElementRegistry() {
		return elementRegistry;
	}

	@Override
	public IJAXBObjectAdapterRegistry getObjectRegistry() {
		return objectRegistry;
	}

	public static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
