package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces;

import java.util.Iterator;

public interface IIteratorReversible<E>
		extends Iterator<E> {
	boolean hasPrevious();

	E previous();
}
