package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.DynamicUtilities.getCallerStackTraceElement;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

public enum PreconditionUtilities {
	;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker(PreconditionUtilities.class);
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static void requireRunOnceOnly()
			throws IllegalStateException {
		Throwable t = ThrowableUtilities.create();

		Optional<StackTraceElement> st = getCallerStackTraceElement();
		if (!st.isPresent()) {
			UtilitiesConfiguration.getInstance().getLogger()
					.atWarn()
					.addMarker(getClassMarker())
					.log(() -> getResourceBundle().getString("run_once_only.stacktrace.null"));
			return;
		}
		@Nullable Throwable t1;
		synchronized (st.toString().intern()) {
			t1 = RAN_ONCE.put(st.get(), t);
		}
		if (t1 != null) {
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(PreconditionUtilities::getClassMarker)
							.addMessages(() -> getResourceBundle().getString("run_once_only.illegal"))
							.build(),
					t1
			);
		}

		UtilitiesConfiguration.getInstance().getLogger().atDebug()
				.addMarker(getClassMarker())
				.setCause(t)
				.log(() -> getResourceBundle().getString("run_once_only.first"));
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	private static final Map<StackTraceElement, Throwable> RAN_ONCE = MapUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_LARGE).makeMap();


	public static void checkArgumentTypes(Class<?>[] types, Object... args) {
		int typesLength = types.length;
		checkElementIndex(typesLength - 1, args.length);
		for (int i = 0; i < typesLength; ++i) {
			@Nullable Object arg = args[i];
			checkArgument(arg == null || types[i].isInstance(arg));
		}
	}

	public static void checkArrayContentType(Class<?> type, Object... array) {
		for (@Nullable Object o : array)
			checkArgument(o == null || type.isInstance(o));
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
