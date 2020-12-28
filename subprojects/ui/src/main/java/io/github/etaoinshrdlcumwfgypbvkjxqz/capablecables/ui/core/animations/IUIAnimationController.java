package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewCoordinator;

public interface IUIAnimationController
		extends IUIViewCoordinator {
	void update();

	void render();

	boolean add(Iterable<? extends IUIAnimationControllable> elements);

	boolean add(IUIAnimationControllable... elements);

	boolean remove(Iterable<? extends IUIAnimationControllable> elements);

	boolean remove(IUIAnimationControllable... elements);
}
