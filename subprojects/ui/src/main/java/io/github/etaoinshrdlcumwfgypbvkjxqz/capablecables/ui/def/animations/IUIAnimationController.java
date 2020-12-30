package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewCoordinator;

import java.util.Iterator;

public interface IUIAnimationController
		extends IUIViewCoordinator {
	void update();

	void render();

	boolean add(Iterator<? extends IUIAnimationControllable> elements);

	boolean remove(Iterator<? extends IUIAnimationControllable> elements);
}
