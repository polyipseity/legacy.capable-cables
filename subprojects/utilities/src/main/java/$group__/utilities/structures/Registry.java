package $group__.utilities.structures;

import $group__.utilities.CastUtilities;
import $group__.utilities.LoggerUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.StreamUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class Registry<K, V> {
	protected final ConcurrentMap<K, RegistryObject<? extends V>> delegated = MapUtilities.getMapMakerMultiThreaded().makeMap();
	protected final boolean overridable;
	protected final Logger logger;

	protected Registry(boolean overridable, Logger logger) {
		this.overridable = overridable;
		this.logger = logger;
	}

	public <VL extends V> RegistryObject<VL> registerApply(K key, Function<? super K, ? extends VL> value) {
		return register(key, value.apply(key));
	}

	public <VL extends V> RegistryObject<VL> register(K key, VL value) {
		AtomicReference<RegistryObject<VL>> retRef = new AtomicReference<>();
		if (getDelegated().computeIfPresent(key, (k, v) -> {
			if (isOverridable())
				getLogger().info(() ->
						LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{}: Overriding key '{}': Replacing value '{}' with '{}'", getClass().getName(), key, v, value));
			else
				throw BecauseOf.illegalArgument(getClass().getName() + ": Cannot override entry", "key", key);
			RegistryObject<VL> vc = CastUtilities.castUnchecked(v); // COMMENT responsibility goes to the caller
			vc.setValue(value);
			retRef.set(vc);
			return v;
		}) == null)
			getDelegated().put(key, retRef.accumulateAndGet(new RegistryObject<>(value), (vp, vn) -> vn));
		@Nullable RegistryObject<VL> ret = retRef.get();
		assert ret != null;
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<K, RegistryObject<? extends V>> getDelegated() { return delegated; }

	public boolean isOverridable() { return overridable; }

	public Logger getLogger() { return logger; }

	public Optional<RegistryObject<? extends V>> get(K key) { return Optional.ofNullable(getDelegated().get(key)); }

	public boolean containsKey(K key) { return getDelegated().containsKey(key); }

	public boolean containsValue(V value) { return StreamUtilities.streamSmart(getDelegated().values(), 1).anyMatch(o -> o.getValue().equals(value)); }

	public static final class RegistryObject<V> {
		protected V value;

		public RegistryObject(V value) { this.value = value;}

		public V getValue() {
			return value;
		}

		protected void setValue(V value) { this.value = value; }
	}
}
