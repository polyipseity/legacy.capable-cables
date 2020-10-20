package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

public class UIReversedMonoDirectionalSimpleStandardAnimationControl
		extends UIReversedMonoDirectionalStandardAnimationControl {
	protected UIReversedMonoDirectionalSimpleStandardAnimationControl(IUIAnimationTarget target,
	                                                                  ITicker ticker,
	                                                                  boolean autoPlay,
	                                                                  long duration,
	                                                                  long startDelay,
	                                                                  long endDelay,
	                                                                  long loop) {
		super(ImmutableSet.of(target), ticker, autoPlay,
				(target2, index, size) -> duration,
				(target2, index, size) -> startDelay,
				(target2, index, size) -> endDelay,
				(target2, index, size) -> loop);
	}
}
