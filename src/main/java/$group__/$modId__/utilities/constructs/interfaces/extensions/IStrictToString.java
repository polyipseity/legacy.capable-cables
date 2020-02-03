package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.MapsExtension.MULTI_THREAD_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.*;
import static $group__.$modId__.utilities.helpers.Throwables.tryCallWithLogging;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static $group__.$modId__.utilities.variables.Globals.getCaughtThrowableUnboxedStatic;

public interface IStrictToString {
	/* SECTION static variables */

	ConcurrentMap<Class<?>, BiFunction<Object, String, String>> FUNCTION_MAP = MULTI_THREAD_MAP_MAKER.makeMap();
	BiFunction<Object, String, String> SEPARATOR_FUNCTION = (t, ss) -> ", ";


	/* SECTION static methods */

	static String getToStringString(Object thisObj, String stringSuper) {
		Class<?> tc = thisObj.getClass();
		@Nullable BiFunction<Object, String, String> func = FUNCTION_MAP.get(tc);
		if (func == null) {
			final boolean[] first = {true};
			ArrayList<BiFunction<Object, String, String>> sfs = new ArrayList<>();
			getThisAndSuperclasses(tc).forEach(c -> {
				for (Field f : c.getDeclaredFields()) {
					if (isMemberStatic(f)) continue;
					String fnp = f.getName() + '=';
					@Nullable MethodHandle fm = unboxOptional(tryCallWithLogging(() -> IMPL_LOOKUP.unreflectGetter(f), LOGGER));
					Function<Object, String> ff;
					if (fm == null) {
						@Nullable Throwable cau = getCaughtThrowableUnboxedStatic();
						ff = t -> "!!!Thrown{throwable=" + cau + "}!!!";
					} else ff = t -> fnp + unboxOptional(tryCallWithLogging(() -> fm.invoke(t), LOGGER));

					if (!first[0]) sfs.add(SEPARATOR_FUNCTION);
					sfs.add((t, ss) -> fnp + ff.apply(t));
					first[0] = false;
				}
			});
			if (!first[0]) sfs.add(SEPARATOR_FUNCTION);
			sfs.add((t, ss) -> "super=" + ss + '}');

			String tcp = tc.getSimpleName() + '{';
			FUNCTION_MAP.put(tc, func = (t, ss) -> sfs.stream().map(sf -> sf.apply(t, ss)).reduce(tcp, String::concat));
		}
		return func.apply(thisObj, stringSuper);
	}


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	String toString();
}
