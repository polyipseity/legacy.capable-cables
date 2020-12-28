package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IRecordCandidate;

public interface ITuple
		extends IRecordCandidate {
	Object get(int index)
			throws IndexOutOfBoundsException;

	int size();

	@Override
	int hashCode();

	@Override
	boolean equals(@Nullable Object obj);

	@Override
	String toString();
}
