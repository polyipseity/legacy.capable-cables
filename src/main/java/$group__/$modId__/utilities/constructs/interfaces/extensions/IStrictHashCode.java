package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Casts.castChecked;
import static $group__.$modId__.utilities.helpers.MapsExtension.MULTI_THREAD_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.*;
import static $group__.$modId__.utilities.helpers.Throwables.tryCallWithLogging;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

public interface IStrictHashCode {
	/* SECTION static variables */

	ConcurrentMap<Class<?>, BiFunction<Object, Supplier<? extends Integer>, Integer>> FUNCTION_MAP = MULTI_THREAD_MAP_MAKER.makeMap();


	/* SECTION static methods */

	static int getHashCode(Object thisObj, Supplier<? extends Integer> hashCodeSuper) {
		Class<?> tc = thisObj.getClass();
		@Nullable BiFunction<Object, Supplier<? extends Integer>, Integer> func = FUNCTION_MAP.get(tc);
		if (func == null) {
			ArrayList<BiFunction<Object, Supplier<? extends Integer>, Object>> ffs = new ArrayList<>();
			if (tc.getSuperclass() != Object.class) ffs.add((t, hcs) -> hcs.get());

			@SuppressWarnings({"rawtypes", "RedundantSuppression"})
			boolean i = castChecked(thisObj, IImmutablizable.class).map(IImmutablizable::isImmutable).orElse(false);
			getThisAndSuperclasses(tc).forEach(c -> {
				for (Field f : c.getDeclaredFields()) {
					if (isMemberStatic(f) || !(i || isMemberFinal(f))) continue;
					@Nullable MethodHandle fm = unboxOptional(tryCallWithLogging(() -> IMPL_LOOKUP.unreflectGetter(f), LOGGER));
					if (fm == null) continue;

					ffs.add((t, hcs) -> unboxOptional(tryCallWithLogging(() -> fm.invoke(t), LOGGER)));
				}
			});

			FUNCTION_MAP.put(tc, func = (t, hcs) -> ffs.stream().map(ff -> ff.apply(t, hcs)).collect(Collectors.toList()).hashCode());
		}
		return func.apply(thisObj, hashCodeSuper);
	}


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	int hashCode();
}
