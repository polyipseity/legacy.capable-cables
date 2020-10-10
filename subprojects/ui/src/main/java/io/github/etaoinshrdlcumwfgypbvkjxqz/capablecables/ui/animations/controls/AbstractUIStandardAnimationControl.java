package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.ToLongBiFunction;
import java.util.stream.IntStream;

public abstract class AbstractUIStandardAnimationControl
		extends AbstractUIAnimationControl {
	private static final int INFINITE_LOOP = -1;

	private final boolean autoPlay;
	private final List<Long> localDurations;
	private final List<Long> startDelays;
	private final List<Long> endDelays;
	private final List<Long> loops;
	@Nullable
	private Long totalDuration = null;

	public AbstractUIStandardAnimationControl(Iterable<? extends IUIAnimationTarget> targets,
	                                          ITicker ticker,
	                                          boolean autoPlay,
	                                          IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> durationFunction,
	                                          IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> startDelayFunction,
	                                          IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> endDelayFunction,
	                                          IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> loopFunction) {
		super(targets, ticker);
		this.autoPlay = autoPlay;
		this.localDurations = generateListFromFunction(targets, durationFunction);
		this.startDelays = generateListFromFunction(targets, startDelayFunction);
		this.endDelays = generateListFromFunction(targets, endDelayFunction);
		this.loops = generateListFromFunction(targets, loopFunction);
	}

	public static int getInfiniteLoop() { return INFINITE_LOOP; }

	protected static <T extends AbstractUIStandardAnimationControl> long calculateTotalDuration(T self, ToLongBiFunction<? super T, ? super Integer> calculateTotalDurationFunction) {
		return IntStream.range(0, self.getTargets().size()).sequential()
				.mapToLong(index -> calculateTotalDurationFunction.applyAsLong(self, index))
				.max()
				.orElse(0);
	}

	@Override
	public EnumUpdateResult update() {
		long currentTime = getTicker().read();
		if (isAutoPlay()) {
			setPlaying(true);
			setLastUpdateTime(currentTime);
		}
		return super.update();
	}

	public boolean isAutoPlay() { return autoPlay; }

	protected List<Long> getLocalDurations() {
		return localDurations;
	}

	protected List<Long> getStartDelays() {
		return startDelays;
	}

	protected List<Long> getEndDelays() {
		return endDelays;
	}

	protected List<Long> getLoops() {
		return loops;
	}

	@Override
	protected EnumUpdateResult getResult() {
		return getElapsed() < getDuration() && getElapsed() >= 0
				? EnumUpdateResult.NORMAL
				: EnumUpdateResult.END;
	}

	@Override
	public long getDuration() {
		OptionalLong totalDuration = getTotalDuration();
		if (!totalDuration.isPresent()) {
			// COMMENT no need to concern ourselves with concurrency
			setTotalDuration(calculateTotalDuration());
			totalDuration = getTotalDuration();
			assert totalDuration.isPresent();
		}
		return 0;
	}

	protected OptionalLong getTotalDuration() { return Optional.ofNullable(totalDuration).map(OptionalLong::of).orElseGet(OptionalLong::empty); }

	protected abstract long calculateTotalDuration();

	protected void setTotalDuration(Long totalDuration) { this.totalDuration = totalDuration; }
}
