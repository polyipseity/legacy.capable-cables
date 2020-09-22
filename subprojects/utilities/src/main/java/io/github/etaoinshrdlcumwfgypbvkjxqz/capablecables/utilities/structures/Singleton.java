package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import static com.google.common.collect.Maps.immutableEntry;
import static java.lang.invoke.MethodType.methodType;

public abstract class Singleton {
	private static final Map<Class<?>, Map.Entry<? extends Singleton, Throwable>> INSTANCES = Collections.synchronizedMap(MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap());
	private final Marker marker = UtilitiesMarkers.getInstance().getClassMarker(getClass());

	protected Singleton(Logger logger) {
		Class<? extends Singleton> clazz = getClass();
		Throwable throwable = ThrowableUtilities.create();

		@SuppressWarnings("ThisEscapedInObjectConstruction") Map.Entry<? extends Singleton, Throwable> v = immutableEntry(this, throwable);
		@Nullable Map.Entry<? extends Singleton, Throwable> vo = getInstances().put(clazz, v);
		if (vo != null)
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(this::getMarker)
							.addMessages(() -> ResourceBundleHolder.getResourceBundle().getString("construct.illegal"))
							.build(),
					vo.getValue()
			);
		logger.debug(getMarker(),
				ResourceBundleHolder.getResourceBundle().getString("construct.new"), throwable);
	}

	@SuppressWarnings("SameReturnValue")
	private static Map<Class<?>, Map.Entry<? extends Singleton, Throwable>> getInstances() { return INSTANCES; }

	@SuppressWarnings("unchecked")
	public static <T extends Singleton> T getSingletonInstance(Class<T> clazz) {
		return tryGetSingletonInstance(clazz, true, t -> {
			try {
				return (T) DynamicUtilities.IMPL_LOOKUP.findConstructor(t, methodType(void.class)).invoke();
			} catch (Throwable throwable) {
				throw ThrowableUtilities.propagate(throwable);
			}
		}).orElseThrow(AssertionError::new);
	}

	public static <T extends Singleton> Optional<T> tryGetSingletonInstance(Class<T> clazz, boolean instantiate,
	                                                                        Function<Class<T>, ? extends T> instantiation) {
		@SuppressWarnings("unchecked") Optional<T> r = (Optional<T>) Optional.ofNullable(getInstances().get(clazz))
				.map(Map.Entry::getKey);
		if (!r.isPresent() && instantiate)
			r = Optional.ofNullable(instantiation.apply(clazz));
		return r;
	}

	public final Marker getMarker() { return marker; }

	public enum ResourceBundleHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
