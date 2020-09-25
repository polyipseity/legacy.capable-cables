package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CleanerUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;

public class ArrayAffineTransformStack
		implements IAffineTransformStack {
	private final Deque<AffineTransform> data;

	public ArrayAffineTransformStack() { this(CapacityUtilities.INITIAL_CAPACITY_MEDIUM); }

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public ArrayAffineTransformStack(int initialCapacity) {
		this.data = new ArrayDeque<>(initialCapacity);
		this.data.push(new AffineTransform());
		Cleaner.create(CleanerUtilities.getCleanerReferent(this), new LeakNotifier(this.data, UIConfiguration.getInstance().getLogger()));
	}

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, StaticHolder.getObjectVariables()); }

	@Override
	public ArrayAffineTransformStack copy() {
		ArrayAffineTransformStack ret = new ArrayAffineTransformStack();
		ret.getData().clear();
		getData().stream().sequential()
				.map(AffineTransform::clone)
				.map(AffineTransform.class::cast)
				.forEachOrdered(ret.getData()::add);
		return ret;
	}

	@Override
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Deque<AffineTransform> getData() { return data; }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, StaticHolder.getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }
}
