package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControllable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;

import java.util.Collections;
import java.util.Set;

public class DefaultUIAnimationController
		implements IUIAnimationController {
	private final Set<IUIAnimationControllable> controllable = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerNormalThreaded().makeMap()
	);

	@Override
	public void update() {
		getControllable().removeIf(animation -> animation.update() == IUIAnimationControllable.EnumUpdateResult.END);
	}

	@Override
	public boolean add(Iterable<? extends IUIAnimationControllable> elements) {
		return Iterables.addAll(getControllable(), elements);
	}

	@Override
	public boolean remove(Iterable<? extends IUIAnimationControllable> elements) {
		return getControllable().removeAll(ImmutableSet.copyOf(elements));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IUIAnimationControllable> getControllable() { return controllable; }
}
