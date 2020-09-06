package $group__.ui.core.mvvm.structures;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.LoggerUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.ICopyable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;
import java.util.function.Function;

public interface IAffineTransformStack
		extends ICopyable, AutoCloseable {
	Logger LOGGER = LogManager.getLogger();
	ImmutableList<Function<? super IAffineTransformStack, ?>> OBJECT_VARIABLES = ImmutableList.of(
			IAffineTransformStack::getData);
	ImmutableMap<String, Function<? super IAffineTransformStack, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("data"),
			OBJECT_VARIABLES));

	@Override
	IAffineTransformStack copy();

	default AffineTransform pop() { return getData().pop(); }

	static void popMultiple(IAffineTransformStack stack, int times) {
		for (; times > 0; --times)
			stack.pop();
	}

	static boolean isClean(Deque<AffineTransform> data) {
		return Optional.ofNullable(data.peek()).filter(AffineTransform::isIdentity).isPresent();
	}

	default AffineTransform push() {
		AffineTransform ret = (AffineTransform) AssertionUtilities.assertNonnull(getData().element()).clone();
		getData().push(ret);
		return ret;
	}

	@Override
	default void close() { createCleaner().run(); }

	default boolean isClean() { return isClean(getData()); }

	Deque<AffineTransform> getData();

	default Runnable createCleaner() {
		return () -> IAffineTransformStack.popMultiple(this, getData().size() - 1);
	}

	default AffineTransform element() { return AssertionUtilities.assertNonnull(getData().element()); }

	class LeakNotifier
			implements Runnable {
		protected final Deque<AffineTransform> data;
		@Nullable
		protected final Throwable throwable;

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public LeakNotifier(Deque<AffineTransform> data) {
			this.data = data;
			this.throwable = ThrowableUtilities.createIfDebug().orElse(null);
		}

		@Override
		public void run() {
			if (!isClean(getData()))
				LOGGER.warn(() -> LoggerUtilities.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(
						LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Stack not clean, content:{}{}", System.lineSeparator(), getData()),
						getThrowable().orElse(null)
				));
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Deque<AffineTransform> getData() { return data; }

		protected Optional<Throwable> getThrowable() { return Optional.ofNullable(throwable); }
	}
}
