package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ArrayUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple;

public abstract class AbstractTuple
		implements ITuple {
	private final Object[] data;

	protected AbstractTuple(Object... data) {
		this.data = data.clone();
		assert ArrayUtilities.isAllElementsNonnull(this.data);
	}

	@Override
	public Object get(int index)
			throws IndexOutOfBoundsException {
		return getData()[index];
	}

	@Override
	public int size() {
		return getData().length;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Object[] getData() { return data; }
}
