package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.def.ITicker;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;

public class UIMonoDirectionalSimpleStandardAnimationControl
		extends UIMonoDirectionalStandardAnimationControl {
	protected UIMonoDirectionalSimpleStandardAnimationControl(IUIAnimationTarget target,
	                                                          ITicker ticker,
	                                                          boolean autoPlay,
	                                                          long duration,
	                                                          long startDelay,
	                                                          long endDelay,
	                                                          long loop) {
		super(ImmutableSet.of(target), ticker, autoPlay,
				(target2, index, size) -> suppressBoxing(duration),
				(target2, index, size) -> suppressBoxing(startDelay),
				(target2, index, size) -> suppressBoxing(endDelay),
				(target2, index, size) -> suppressBoxing(loop));
	}
}
