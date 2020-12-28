package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.UIImmutableAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongLists;

import java.util.Optional;
import java.util.function.ToLongBiFunction;
import java.util.stream.IntStream;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;

public abstract class UIAbstractStandardAnimationControl
		extends UIAbstractAnimationControl {
	private final boolean autoPlay;
	private final LongList localDurations;
	private final LongList startDelays;
	private final LongList endDelays;
	private final LongList loops;
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
		this.localDurations = LongLists.unmodifiable(new LongArrayList(generateListFromFunction(targets, durationFunction)));
		this.startDelays = LongLists.unmodifiable(new LongArrayList(generateListFromFunction(targets, startDelayFunction)));
		this.endDelays = LongLists.unmodifiable(new LongArrayList(generateListFromFunction(targets, endDelayFunction)));
		this.loops = LongLists.unmodifiable(new LongArrayList(generateListFromFunction(targets, loopFunction)));
	}

	protected static <T extends UIAbstractStandardAnimationControl> IUIAnimationTime calculateTotalDuration(T self, ToLongBiFunction<? super T, ? super Integer> calculateTotalDurationFunction) {
		if (IntStream.range(0, self.getTargets().size())
				.mapToLong(index -> self.getLoops().getLong(index))
				.filter(loop -> loop == UIStandardAnimationControlFactory.getInfiniteLoop())
				.findAny()
				.isPresent())
			return UIImmutableAnimationTime.getInfinity();
		return UIImmutableAnimationTime.of(
				IntStream.range(0, self.getTargets().size())
						.mapToLong(index -> calculateTotalDurationFunction.applyAsLong(self, suppressBoxing(index)))
						.max()
						.orElse(0)
		);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected LongList getLoops() {
		return loops;
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

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected LongList getLocalDurations() {
		return localDurations;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected LongList getStartDelays() {
		return startDelays;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected LongList getEndDelays() {
		return endDelays;
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
