package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.UIImmutableAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.ToLongBiFunction;
import java.util.stream.IntStream;

public abstract class UIAbstractStandardAnimationControl
		extends UIAbstractAnimationControl {
	private final boolean autoPlay;
	private final List<Long> localDurations;
	private final List<Long> startDelays;
	private final List<Long> endDelays;
	private final List<Long> loops;
	@Nullable
	private IUIAnimationTime totalDuration = null;

	public UIAbstractStandardAnimationControl(Iterable<? extends IUIAnimationTarget> targets,
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

	protected static <T extends UIAbstractStandardAnimationControl> IUIAnimationTime calculateTotalDuration(T self, ToLongBiFunction<? super T, ? super Integer> calculateTotalDurationFunction) {
		if (IntStream.range(0, self.getTargets().size()).sequential()
				.mapToLong(index -> self.getLoops().get(index))
				.filter(loop -> loop == UIStandardAnimationControlFactory.getInfiniteLoop())
				.findAny()
				.isPresent())
			return UIImmutableAnimationTime.getInfinity();
		return UIImmutableAnimationTime.of(
				IntStream.range(0, self.getTargets().size()).sequential()
						.mapToLong(index -> calculateTotalDurationFunction.applyAsLong(self, index))
						.max()
						.orElse(0)
		);
	}

	@Override
	public EnumUpdateResult update() {
		long currentTime = getTicker().read();
		if (!isPlaying() && isAutoPlay()) {
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
		IUIAnimationTime duration = getDuration();
		if (duration.isFinite())
			return getElapsed() < duration.get() && getElapsed() >= 0
					? EnumUpdateResult.NORMAL
					: EnumUpdateResult.END;
		else
			return EnumUpdateResult.NORMAL;
	}

	@Override
	public IUIAnimationTime getDuration() {
		Optional<? extends IUIAnimationTime> totalDuration = getTotalDuration();
		if (!totalDuration.isPresent()) {
			// COMMENT no need to concern ourselves with concurrency
			setTotalDuration(calculateTotalDuration());
			totalDuration = getTotalDuration();
			assert totalDuration.isPresent();
		}
		return totalDuration.get();
	}

	protected Optional<? extends IUIAnimationTime> getTotalDuration() { return Optional.ofNullable(totalDuration); }

	protected abstract IUIAnimationTime calculateTotalDuration();

	protected void setTotalDuration(IUIAnimationTime totalDuration) { this.totalDuration = totalDuration; }
}
