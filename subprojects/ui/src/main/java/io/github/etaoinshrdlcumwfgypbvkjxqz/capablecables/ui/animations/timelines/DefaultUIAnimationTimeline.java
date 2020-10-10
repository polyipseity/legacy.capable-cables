package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.timelines;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControl;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

import java.util.function.LongUnaryOperator;

public class DefaultUIAnimationTimeline
		extends AbstractUIAnimationTimeline {
	private final boolean autoPlay;
	@SuppressWarnings("UnstableApiUsage")
	private final Multimap<Long, IUIAnimationControl> keyframes =
			MultimapBuilder.hashKeys(CapacityUtilities.INITIAL_CAPACITY_SMALL).hashSetValues(CapacityUtilities.INITIAL_CAPACITY_TINY).build();
	private long offset = 0;
	private long duration = 0;

	public DefaultUIAnimationTimeline(ITicker ticker, boolean autoPlay) {
		super(ticker);
		this.autoPlay = autoPlay;
	}

	@Override
	public boolean append(IUIAnimationControl control, LongUnaryOperator offsetFunction) {
		long start = offsetFunction.applyAsLong(offset);
		long end = start + control.getDuration();
		boolean ret = getKeyframes().put(start, control);
		if (ret) {
			setOffset(end);
			setDuration(Math.max(getDuration(), end));
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Multimap<Long, IUIAnimationControl> getKeyframes() {
		return keyframes;
	}

	protected long getDuration() {
		return duration;
	}

	protected void setDuration(long duration) {
		this.duration = duration;
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

	@Override
	protected long updateElapsed(long previousElapsed, long difference) {
		return previousElapsed + difference;
	}

	@Override
	protected EnumUpdateResult update0() {
		long elapsed = getElapsed();
		long[] keys = getKeyframes().keySet().stream().unordered()
				.mapToLong(key -> key)
				.filter(key -> key >= elapsed)
				.toArray();
		for (long key : keys) {
			getKeyframes().removeAll(key)
					.forEach(IUIAnimationControl::play);
		}
		return getElapsed() < getDuration()
				? EnumUpdateResult.NORMAL
				: EnumUpdateResult.END;
	}

	protected boolean isAutoPlay() {
		return autoPlay;
	}

	protected long getOffset() {
		return offset;
	}

	protected void setOffset(long offset) {
		this.offset = offset;
	}
}
