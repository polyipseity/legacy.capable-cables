package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.Serializable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractRegistry<K, V>
		implements Serializable {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private static final long serialVersionUID = 8593887271997933205L;
	private final Marker marker = UtilitiesMarkers.getInstance().getClassMarker(getClass());

	public <VL extends V> RegistryObject<VL> register(K key, VL value) {
		AtomicReference<RegistryObject<VL>> retRef = new AtomicReference<>();
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
			@SuppressWarnings("unchecked") RegistryObject<VL> vc = (RegistryObject<VL>) registryObject; // COMMENT responsibility goes to the caller
			vc.setValue(value);
			retRef.set(vc);
			return registryObject;
		}) == null)
			getData().put(key, retRef.accumulateAndGet(new RegistryObject<>(value), (vp, vn) -> vn));
		return AssertionUtilities.assertNonnull(retRef.get());
	}

	private final ConcurrentMap<K, RegistryObject<? extends V>> data;
	private final boolean overridable;
	private final Logger logger;

	protected AbstractRegistry(boolean overridable, Logger logger, Consumer<? super MapMaker> configuration) {
		this.overridable = overridable;
		this.logger = logger;
		this.data = FunctionUtilities.accept(new MapMaker(), configuration).makeMap();
	}

	public Marker getMarker() { return marker; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<K, RegistryObject<? extends V>> getData() { return data; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public boolean isOverridable() { return overridable; }

	protected Logger getLogger() { return logger; }

	public Optional<? extends RegistryObject<? extends V>> get(K key) { return Optional.ofNullable(getData().get(key)); }

	@SuppressWarnings("SuspiciousMethodCalls")
	public boolean containsKey(Object key) { return getData().containsKey(key); }

	public boolean containsValue(Object value) {
		return getData().values().stream().unordered()
				.map(RegistryObject::getValue)
				.anyMatch(Predicate.isEqual(value));
	}
}
