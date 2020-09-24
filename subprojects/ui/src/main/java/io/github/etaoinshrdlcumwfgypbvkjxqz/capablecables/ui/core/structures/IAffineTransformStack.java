package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.MarkersTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public interface IAffineTransformStack
		extends ICopyable, AutoCloseable {

	@Override
	IAffineTransformStack copy();

	@SuppressWarnings("UnusedReturnValue")
	default AffineTransform pop() { return getData().pop(); }

	default AffineTransform push() {
		AffineTransform ret = (AffineTransform) AssertionUtilities.assertNonnull(getData().element()).clone();
		getData().push(ret);
		return ret;
	}

	@Override
	default void close() { createCleaner().run(); }

	default boolean isClean() { return StaticHolder.isClean(getData()); }

	Deque<AffineTransform> getData();

	default Runnable createCleaner() {
		return () -> StaticHolder.popMultiple(this, getData().size() - 1);
	}

	default AffineTransform element() { return AssertionUtilities.assertNonnull(getData().element()); }

	enum StaticHolder {
		;

		private static final Marker CLASS_MARKER = MarkersTemplate.addReferences(UIMarkers.getInstance().getClassMarker(),
				UIMarkers.getInstance().getMarkerStructure());
		private static final ImmutableList<Function<? super IAffineTransformStack, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IAffineTransformStack::getData);
		@NonNls
		private static final ImmutableMap<String, Function<? super IAffineTransformStack, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(getObjectVariables().size(),
				ImmutableList.of("data"),
				getObjectVariables()));

		public static void popMultiple(IAffineTransformStack stack, int times) {
			for (; times > 0; --times)
				stack.pop();
		}

		public static boolean isClean(Deque<AffineTransform> data) {
			return Optional.ofNullable(data.peek()).filter(AffineTransform::isIdentity).isPresent();
		}

		public static Marker getClassMarker() {
			return CLASS_MARKER;
		}

		public static ImmutableList<Function<? super IAffineTransformStack, ?>> getObjectVariables() {
			return OBJECT_VARIABLES;
		}

		public static ImmutableMap<String, Function<? super IAffineTransformStack, ?>> getObjectVariablesMap() {
			return OBJECT_VARIABLES_MAP;
		}
	}

	class LeakNotifier
			implements Runnable {
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		private final Deque<AffineTransform> data;
		private final Logger logger;
		@Nullable
		private final Throwable throwable;

		@Override
		public void run() {
			if (!StaticHolder.isClean(getData()))
				getLogger().atWarn()
						.addMarker(StaticHolder.getClassMarker())
						.addArgument(this::getData)
						.setCause(getThrowable().orElse(null))
						.log(() -> getResourceBundle().getString("stack.clean.not"));
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public LeakNotifier(Deque<AffineTransform> data, Logger logger) {
			this.data = data;
			this.logger = logger;
			this.throwable = UIConstants.BUILD_TYPE.isDebug() ? ThrowableUtilities.create() : null;
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Deque<AffineTransform> getData() { return data; }

		protected Optional<Throwable> getThrowable() { return Optional.ofNullable(throwable); }

		protected Logger getLogger() { return logger; }
	}
}
