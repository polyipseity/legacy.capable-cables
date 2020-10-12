package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core;

import java.util.NoSuchElementException;

public interface IObjectStack<T> {
	T push(T element);

	T pop();

	T element()
			throws NoSuchElementException;

	int size();

	interface ICopyPushable<T>
			extends IObjectStack<T> {
		T push();
	}
}
