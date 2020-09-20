package $group__.ui.core.structures;

import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.ICopyable;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.templates.MarkerUtilitiesTemplate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

		private static final Marker CLASS_MARKER = MarkerUtilitiesTemplate.addReferences(UIMarkers.getInstance().getClassMarker(IAffineTransformStack.class),
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

		@Override
		public void run() {
			if (!StaticHolder.isClean(getData()))
				logger.warn(StaticHolder.getClassMarker(),
						getResourceBundle().getString("stack.clean.not"), getData(),
						getThrowable());
		}

		protected final Deque<AffineTransform> data;
		protected final Logger logger;
		@Nullable
		protected final Throwable throwable;

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public LeakNotifier(Deque<AffineTransform> data, Logger logger) {
			this.data = data;
			this.logger = logger;
			this.throwable = ThrowableUtilities.createIfDebug().orElse(null);
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Deque<AffineTransform> getData() { return data; }

		protected Optional<Throwable> getThrowable() { return Optional.ofNullable(throwable); }
	}
}
