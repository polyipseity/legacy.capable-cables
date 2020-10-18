package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LoopUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IObjectStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.MarkersTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;
import java.util.ResourceBundle;

public interface IAffineTransformStack
		extends IObjectStack.ICopyPushable<AffineTransform>, ICopyable, AutoCloseable {
	static void popNTimes(IAffineTransformStack stack, int times) {
		LoopUtilities.doNTimes(times, stack::pop);
	}

	static boolean isClean(Deque<AffineTransform> data) {
		return Optional.ofNullable(data.peek()).filter(AffineTransform::isIdentity).isPresent();
	}

	@Override
	IAffineTransformStack copy();

	@Override
	void close();

	boolean isClean();

	Runnable createCleaner();

	enum StaticHolder {
		;

		private static final Marker CLASS_MARKER = MarkersTemplate.addReferences(UIMarkers.getInstance().getClassMarker(),
				UIMarkers.getInstance().getMarkerStructure());

		public static Marker getClassMarker() {
			return CLASS_MARKER;
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
			if (!isClean(getData()))
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
			this.throwable = UIConstants.getBuildType().isDebug() ? ThrowableUtilities.create() : null;
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Deque<AffineTransform> getData() { return data; }

		protected Optional<Throwable> getThrowable() { return Optional.ofNullable(throwable); }

		protected Logger getLogger() { return logger; }
	}
}
