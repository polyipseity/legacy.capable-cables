package $group__.utilities.structures;

import $group__.utilities.*;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import static $group__.utilities.DynamicUtilities.IMPL_LOOKUP;
import static $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import static $group__.utilities.ThrowableUtilities.Try;
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
			throw ThrowableUtilities.logAndThrow(
					new IllegalStateException(
							new LogMessageBuilder()
									.addMarkers(this::getMarker)
									.addMessages(() -> ResourceBundleHolder.getResourceBundle().getString("construct.illegal"))
									.build(),
							vo.getValue()
					),
					UtilitiesConfiguration.getInstance().getLogger()
			);

		logger.debug(getMarker(),
				ResourceBundleHolder.getResourceBundle().getString("construct.new"), throwable);
	}

	private static Map<Class<?>, Map.Entry<? extends Singleton, Throwable>> getInstances() { return INSTANCES; }

	@SuppressWarnings("unchecked")
	public static <T extends Singleton> T getSingletonInstance(Class<T> clazz, @Nullable Logger logger) {
		return tryGetSingletonInstance(clazz, true, t ->
				Try.withLogging(() ->
								Try.call(() -> (T) IMPL_LOOKUP.findConstructor(t, methodType(void.class)).invoke(), logger),
						logger))
				.orElseThrow(ThrowableCatcher::rethrow);
	}

	public static <T extends Singleton> Optional<T> tryGetSingletonInstance(Class<T> clazz, boolean instantiate,
	                                                                        Function<Class<T>, ? extends Optional<T>> instantiation) {
		@SuppressWarnings("unchecked") Optional<T> r = (Optional<T>) Optional.ofNullable(getInstances().get(clazz))
				.map(Map.Entry::getKey);
		if (!r.isPresent() && instantiate)
			r = instantiation.apply(clazz);
		return r;
	}

	public final Marker getMarker() { return marker; }

	public enum ResourceBundleHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
