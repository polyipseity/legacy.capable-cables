package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.timelines;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControl;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.UIAlreadyInfiniteAnimationTimelineException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.UIImmutableAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.LongUnaryOperator;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;

public class UIDefaultAnimationTimeline
		extends UIAbstractAnimationTimeline {
	private final boolean autoPlay;
	@SuppressWarnings("UnstableApiUsage")
	private final Multimap<Long, IUIAnimationControl> keyframes =
			MultimapBuilder.hashKeys(CapacityUtilities.getInitialCapacitySmall()).hashSetValues(CapacityUtilities.getInitialCapacityTiny()).build();
	private final Set<IUIAnimationControl> playingKeyframes =
			Collections.newSetFromMap(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap());
	private final Set<IUIAnimationControl> endingKeyframes =
			Collections.newSetFromMap(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap());
	private IUIAnimationTime offset = UIImmutableAnimationTime.of(0);
	private IUIAnimationTime duration = UIImmutableAnimationTime.of(0);

	public UIDefaultAnimationTimeline(ITicker ticker, boolean autoPlay) {
		super(ticker);
		this.autoPlay = autoPlay;
	}

	@Override
	public boolean append(IUIAnimationControl control, LongUnaryOperator offsetFunction)
			throws UIAlreadyInfiniteAnimationTimelineException {
		IUIAnimationTime offset = getOffset();
		if (offset.isInfinite())
			throw new UIAlreadyInfiniteAnimationTimelineException();
		long start = offsetFunction.applyAsLong(offset.get());
		IUIAnimationTime duration = control.getDuration();
		IUIAnimationTime end = IUIAnimationTime.max(UIImmutableAnimationTime.of(start), duration);
		@SuppressWarnings("AutoBoxing") boolean ret = getKeyframes().put(start, control);
		if (ret) {
			setOffset(end);
			setDuration(IUIAnimationTime.max(getDuration(), end));
			return true;
		} else {
			return false;
		}
	}

	protected IUIAnimationTime getOffset() {
		return offset;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Multimap<Long, IUIAnimationControl> getKeyframes() {
		return keyframes;
	}

	protected IUIAnimationTime getDuration() {
		return duration;
	}

	protected void setDuration(IUIAnimationTime duration) {
		this.duration = duration;
	}

	protected void setOffset(IUIAnimationTime offset) {
		this.offset = offset;
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

	@Override
	protected long updateElapsed(long previousElapsed, long difference) {
		return previousElapsed + difference;
	}

	@Override
	protected EnumUpdateResult update0() {
		{
			getPlayingKeyframes().removeAll(getEndingKeyframes());
			getEndingKeyframes().clear();
			getPlayingKeyframes().stream().unordered()
					.filter(animation -> animation.update() == EnumUpdateResult.END)
					.forEach(getEndingKeyframes()::add);
		}
		long elapsed = getElapsed();
		for (long key : getKeyframes().keySet().stream().unordered()
				.mapToLong(Long::longValue)
				.filter(key -> key >= elapsed)
				.toArray()) {
			Collection<IUIAnimationControl> keyframes = getKeyframes().removeAll(suppressBoxing(key));
			getPlayingKeyframes().addAll(keyframes);
			keyframes.forEach(IUIAnimationControl::play);
		}
		IUIAnimationTime duration = getDuration();
		if (duration.isFinite())
			return getElapsed() < duration.get()
					? EnumUpdateResult.NORMAL
					: EnumUpdateResult.END;
		else
			return EnumUpdateResult.NORMAL;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IUIAnimationControl> getPlayingKeyframes() { return playingKeyframes; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IUIAnimationControl> getEndingKeyframes() { return endingKeyframes; }

	protected boolean isAutoPlay() {
		return autoPlay;
	}

	@Override
	public void render() {
		getPlayingKeyframes()
				.forEach(IUIAnimationControl::render);
	}
}
