package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.controls;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.AbstractUIAnimationPlayable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControl;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractUIAnimationControl
		extends AbstractUIAnimationPlayable
		implements IUIAnimationControl {
	private final List<IUIAnimationTarget> targets;
	private final List<Consumer<? super IUIAnimationControl>> endActions = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	private boolean reversed = false;

	public AbstractUIAnimationControl(Iterable<? extends IUIAnimationTarget> targets, ITicker ticker) {
		super(ticker);
		this.targets = ImmutableList.copyOf(targets);
	}

	@Immutable
	protected static <R, TH extends Throwable> List<R> generateListFromFunction(Iterable<? extends IUIAnimationTarget> targets,
	                                                                            IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends R, ? extends TH> function)
			throws TH {
		@Immutable List<IUIAnimationTarget> targetsList = ImmutableList.copyOf(targets);
		final int[] index = {0};
		int size = targetsList.size();
		ImmutableList.Builder<R> ret = ImmutableList.builder();
		targetsList.forEach(IThrowingConsumer.executeNow(target -> {
			ret.add(AssertionUtilities.assertNonnull(function.apply(target, index[0], size)));
			++index[0];
		}));
		return ret.build();
	}

	@Override
	public void render() {
		int[] index = {0};
		int size = getTargets().size();
		getTargets().forEach(target -> {
			target.accept(getProgressForTarget(target, index[0], size));
			++index[0];
		});
	}

	@Override
	protected long updateElapsed(long previousElapsed, long difference) {
		return getElapsed() + (isReversed() ? -1 : 1) * difference;
	}

	@Override
	public EnumUpdateResult update0() {
		EnumUpdateResult result = getResult();
		if (result == EnumUpdateResult.END)
			getEndActions().forEach(action -> action.accept(this));
		return result;
	}

	protected List<? extends IUIAnimationTarget> getTargets() { return targets; }

	protected abstract double getProgressForTarget(IUIAnimationTarget target, int index, int size);

	protected abstract EnumUpdateResult getResult();

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<Consumer<? super IUIAnimationControl>> getEndActions() {
		return endActions;
	}

	protected boolean isReversed() { return reversed; }

	protected void setReversed(boolean reversed) { this.reversed = reversed; }

	@Override
	public void pause() {
		update();
		setPlaying(false);
	}

	@Override
	public void reverse() {
		update();
		setReversed(!isReversed());
	}

	@Override
	public void reset() {
		update();
		setPlaying(false);
		setReversed(false);
		setElapsed(0);
	}

	@Override
	public void seek(long progress) {
		setElapsed(progress);
	}

	@Override
	public void onEnd(Consumer<? super IUIAnimationControl> action) {
		getEndActions().add(action);
	}
}
