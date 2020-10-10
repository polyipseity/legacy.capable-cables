package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

@Deprecated
public class TimeSymmetricUIStandardAnimationControl
		extends BiDirectionalSimpleUIStandardAnimationControl {
	protected TimeSymmetricUIStandardAnimationControl(IUIAnimationTarget target, ITicker ticker, boolean autoPlay, long duration, long startDelay, long endDelay, long loop) {
		super(target, ticker, autoPlay, duration, startDelay, endDelay, loop);
	}
}
