package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

@Deprecated
public class UITimeSymmetricStandardAnimationControl
		extends UIBiDirectionalSimpleStandardAnimationControl {
	protected UITimeSymmetricStandardAnimationControl(IUIAnimationTarget target, ITicker ticker, boolean autoPlay, long duration, long startDelay, long endDelay, long loop) {
		super(target, ticker, autoPlay, duration, startDelay, endDelay, loop);
	}
}
