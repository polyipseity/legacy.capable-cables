package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

public interface IUIAnimationController {
	void update();

	boolean add(Iterable<? extends IUIAnimationControllable> elements);

	boolean remove(Iterable<? extends IUIAnimationControllable> elements);
}
