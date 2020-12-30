package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewCoordinator;

import java.util.Iterator;

public interface IUIAnimationController
		extends IUIViewCoordinator {
	void update();

	void render();

	boolean add(Iterator<? extends IUIAnimationControllable> elements);

	boolean remove(Iterator<? extends IUIAnimationControllable> elements);
}
