package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.Serializable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Registry<K, V> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
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

	private final ConcurrentMap<K, RegistryObject<? extends V>> data = MapBuilderUtilities.newMapMakerNormalThreaded().makeMap();
	private final boolean overridable;
	private final Logger logger;

	protected Registry(boolean overridable, Logger logger) {
		this.overridable = overridable;
		this.logger = logger;
	}

	public <VL extends V> RegistryObject<VL> registerApply(K key, Function<? super K, ? extends VL> value) { return register(key, AssertionUtilities.assertNonnull(value.apply(key))); }

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

	public static final class RegistryObject<V>
			implements Serializable, Supplier<V> {
		private static final long serialVersionUID = -7426757514591663232L;
		@SuppressWarnings("NonSerializableFieldInSerializableClass")
		private V value;

		public RegistryObject(V value) { this.value = value;}

		public V getValue() { return value; }

		protected void setValue(V value) { this.value = value; }

		@Override
		public V get() { return getValue(); }
	}
}
