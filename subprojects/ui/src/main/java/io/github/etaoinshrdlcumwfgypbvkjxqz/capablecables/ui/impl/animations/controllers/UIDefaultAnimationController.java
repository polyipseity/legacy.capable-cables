package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controllers;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations.IUIAnimationControllable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class UIDefaultAnimationController
		extends UIAbstractAnimationController {
	private final Set<IUIAnimationControllable> controllable = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).makeMap()
	);
	private final Set<IUIAnimationControllable> endingControllable = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).makeMap()
	);

	@Override
	public void update() {
		getControllable().removeAll(getEndingControllable());
		getEndingControllable().clear();
		getControllable().stream().unordered()
				.filter(animation -> animation.update() == IUIAnimationControllable.EnumUpdateResult.END)
				.forEach(getEndingControllable()::add);
	}

	@Override
	public void render() {
		getControllable()
				.forEach(IUIAnimationControllable::render);
	}

	@Override
	public boolean add(Iterator<? extends IUIAnimationControllable> elements) {
		return Iterators.addAll(getControllable(), elements);
	}

	@Override
	public boolean remove(Iterator<? extends IUIAnimationControllable> elements) {
		return getControllable().removeAll(ImmutableSet.copyOf(elements));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IUIAnimationControllable> getControllable() { return controllable; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IUIAnimationControllable> getEndingControllable() { return endingControllable; }
}
