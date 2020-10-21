package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IRecordCandidate;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ITuple
		extends IRecordCandidate {
	Object get(int index)
			throws IndexOutOfBoundsException;
}
