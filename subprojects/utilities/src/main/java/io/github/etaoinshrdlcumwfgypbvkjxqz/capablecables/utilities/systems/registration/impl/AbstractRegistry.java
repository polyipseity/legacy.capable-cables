package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public abstract class AbstractRegistry<K, V>
		implements IRegistry<K, V> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private static final long serialVersionUID = 8593887271997933205L;
	private final Marker marker = UtilitiesMarkers.getInstance().getClassMarker(getClass());

	private final boolean overridable;

	public AbstractRegistry(boolean overridable) {
		this.overridable = overridable;
	}

	@Override
	public <VE extends V> IRegistryObject<VE> register(K key, VE value) {
		AtomicReference<IRegistryObjectInternal<VE>> retRef = new AtomicReference<>();
		if (getData().computeIfPresent(key, (keyToo, registryObject) -> {
			if (isOverridable())
				getLogger().atInfo()
						.addMarker(getMarker())
						.addKeyValue("keyToo", keyToo).addKeyValue("value", value).addKeyValue("registryObject", registryObject)
						.log(() -> getResourceBundle().getString("override.allowed"));
			else
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(this::getMarker)
								.addKeyValue("keyToo", keyToo).addKeyValue("value", value).addKeyValue("registryObject", registryObject)
								.addMessages(() -> getResourceBundle().getString("override.disabled"))
								.build()
				);
			@SuppressWarnings("unchecked") IRegistryObjectInternal<VE> vc = (IRegistryObjectInternal<VE>) registryObject; // COMMENT responsibility goes to the caller
			vc.setValue(value);
			retRef.set(vc);
			return registryObject;
		}) == null)
			getData().put(key, retRef.accumulateAndGet(new DefaultRegistryObject<>(value), (vp, vn) -> vn));
		return AssertionUtilities.assertNonnull(retRef.get());
	}

	protected abstract Map<K, IRegistryObjectInternal<? extends V>> getData();

	protected abstract Logger getLogger();

	protected Marker getMarker() { return marker; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public boolean isOverridable() { return overridable; }

	@Override
	public Optional<? extends IRegistryObject<? extends V>> get(K key) { return Optional.ofNullable(getData().get(key)); }

	@Override
	@SuppressWarnings("SuspiciousMethodCalls")
	public boolean containsKey(Object key) { return getData().containsKey(key); }

	@Override
	public boolean containsValue(Object value) {
		return getData().values().stream().unordered()
				.map(IRegistryObject::getValue)
				.anyMatch(Predicate.isEqual(value));
	}
}
