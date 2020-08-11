package $group__.utilities.interfaces;

import java.util.Iterator;

public interface IIteratorReversible<E>
		extends Iterator<E> {
	boolean hasPrevious();

	E previous();
}
