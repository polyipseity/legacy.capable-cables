package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.reactivex.rxjava3.processors.PublishProcessor;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
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
	private final Processor<Map.@Immutable Entry<K, V>, Map.@Immutable Entry<K, V>> processor = PublishProcessor.create();

	public AbstractRegistry(boolean overridable) {
		this.overridable = overridable;
	}

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "ObjectEquality"})
	@Override
	public <VE extends V> IRegistryObject<VE> register(K key, VE value) {
		AtomicReference<IRegistryObjectInternal<VE>> retRef = new AtomicReference<>();
		IRegistryObjectInternal<? extends V> registryObject = getData().computeIfAbsent(key, key1 -> new DefaultRegistryObject<>(value));
		synchronized (registryObject) {
			// COMMENT sync to ensure ordering

			@SuppressWarnings("unchecked") IRegistryObjectInternal<VE> uncheckedRegistryObject = (IRegistryObjectInternal<VE>) registryObject; // COMMENT responsibility goes to the caller

			VE previousValue = uncheckedRegistryObject.getValue();
			if (previousValue != value) /* COMMENT check identity */ {
				if (isOverridable())
					getLogger().atInfo()
							.addMarker(getMarker())
							.addKeyValue("key", key).addKeyValue("value", value).addKeyValue("uncheckedRegistryObject", uncheckedRegistryObject)
							.log(() -> getResourceBundle().getString("override.allowed"));
				else
					throw new IllegalArgumentException(
							new LogMessageBuilder()
									.addMarkers(this::getMarker)
									.addKeyValue("key", key).addKeyValue("value", value).addKeyValue("uncheckedRegistryObject", uncheckedRegistryObject)
									.addMessages(() -> getResourceBundle().getString("override.disabled"))
									.build()
					);
				uncheckedRegistryObject.setValue(value);
			}
			getProcessor().onNext(Maps.immutableEntry(key, value));
			return uncheckedRegistryObject;
		}
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

	@Override
	public Publisher<Map.@Immutable Entry<K, V>> getPublisher() {
		return getProcessor();
	}

	@Override
	public @Immutable Map<K, IRegistryObject<? extends V>> asMapView() {
		return ImmutableMap.copyOf(getData());
	}

	protected Processor<Map.@Immutable Entry<K, V>, Map.@Immutable Entry<K, V>> getProcessor() {
		return processor;
	}
}
