package $group__.utilities.structures;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.specific.MapUtilities;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static $group__.utilities.DynamicUtilities.IMPL_LOOKUP;
import static $group__.utilities.specific.LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.utilities.specific.ThrowableUtilities.*;
import static com.google.common.collect.Maps.immutableEntry;
import static java.lang.System.lineSeparator;
import static java.lang.invoke.MethodType.methodType;

public abstract class Singleton {
	protected static final Map<Class<?>, Map.Entry<? extends Singleton, String>> INSTANCES = Collections.synchronizedMap(MapUtilities.getMapMakerSingleThread().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap());


	protected Singleton(Logger logger) {
		Class<? extends Singleton> clazz = getClass();
		String classGS = clazz.toGenericString(),
				sts = getCurrentStackTraceString();

		Map.Entry<? extends Singleton, String> v = immutableEntry(this, sts);
		@Nullable Map.Entry<? extends Singleton, String> vo = INSTANCES.put(clazz, v);
		if (vo != null) {
			logger.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("The singleton instance of '{}' already created, previous stacktrace:{}{}", classGS, lineSeparator(), vo.getValue()));
			throw BecauseOf.instantiation();
		}

		logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("The singleton instance of '{}' created, stacktrace:{}{}", classGS, lineSeparator(), sts));
	}


	public static <T extends Singleton> T getSingletonInstance(Class<T> clazz, @Nullable Logger logger) { return tryGetSingletonInstance(clazz, true, t -> Try.withLogging(() -> Try.call(() -> IMPL_LOOKUP.findConstructor(t, methodType(void.class)).invoke(), logger), logger).map(CastUtilities::castUnchecked)).orElseThrow(ThrowableCatcher::rethrow); }

	public static <T extends Singleton> Optional<T> tryGetSingletonInstance(Class<T> clazz, boolean instantiate,
	                                                                        Function<Class<T>, ? extends Optional<T>> instantiation) {
		Optional<T> r = Optional.ofNullable(INSTANCES.get(clazz)).map(Map.Entry::getKey).map(CastUtilities::castUnchecked);
		if (!r.isPresent() && instantiate) r = instantiation.apply(clazz);
		return r;
	}
}
