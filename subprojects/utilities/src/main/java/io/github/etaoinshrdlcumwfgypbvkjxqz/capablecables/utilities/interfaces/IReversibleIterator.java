package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces;

import java.util.Iterator;

public interface IReversibleIterator<E>
		extends Iterator<E> {
	boolean hasPrevious();

	E previous();
}
