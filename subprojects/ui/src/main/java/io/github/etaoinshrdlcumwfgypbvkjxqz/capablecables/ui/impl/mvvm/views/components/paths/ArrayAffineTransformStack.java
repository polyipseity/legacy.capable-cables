package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CleanerUtilities;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;

public class ArrayAffineTransformStack
		extends AbstractAffineTransformStack {
	private final Deque<AffineTransform> data;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public ArrayAffineTransformStack(int initialCapacity) {
		this.data = new ArrayDeque<>(initialCapacity);
		this.data.push(new AffineTransform());
		Cleaner.create(CleanerUtilities.getCleanerReferent(this), new LeakNotifier(this.data, UIConfiguration.getInstance().getLogger()));
	}

	@Override
	public ArrayAffineTransformStack copy() { return (ArrayAffineTransformStack) super.copy(); }

	@Override
	protected ArrayAffineTransformStack newInstanceForCopying() { return new ArrayAffineTransformStack(getData().size()); }

	@Override
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Deque<AffineTransform> getData() { return data; }
}
