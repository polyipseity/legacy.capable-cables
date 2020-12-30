package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations.IUIAnimationControllable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.def.ITicker;

public abstract class UIAbstractAnimationPlayable
		implements IUIAnimationControllable {
	private final ITicker ticker;
	private long elapsed = 0;
	private long lastUpdateTime = 0;
	private boolean playing = false;

	protected UIAbstractAnimationPlayable(ITicker ticker) {
		this.ticker = ticker;
	}

	public void play() {
		update();
		setPlaying(true);
	}

	@Override
	public EnumUpdateResult update() {
		long currentTime = getTicker().read();
		if (isPlaying())
			setElapsed(updateElapsed(getElapsed(), currentTime - getLastUpdateTime()));
		setLastUpdateTime(currentTime);
		return update0();
	}

	protected ITicker getTicker() {
		return ticker;
	}

	protected boolean isPlaying() { return playing; }

	protected abstract long updateElapsed(long previousElapsed, long difference);

	protected long getElapsed() { return elapsed; }

	protected void setElapsed(long elapsed) { this.elapsed = elapsed; }

	protected long getLastUpdateTime() { return lastUpdateTime; }

	protected abstract EnumUpdateResult update0();

	protected void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }

	protected void setPlaying(boolean playing) { this.playing = playing; }
}
