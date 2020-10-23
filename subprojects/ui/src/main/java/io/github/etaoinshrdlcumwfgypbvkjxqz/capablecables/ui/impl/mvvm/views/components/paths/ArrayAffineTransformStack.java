package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CleanerUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

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
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Deque<AffineTransform> getData() { return data; }

	private static final long DATA_FIELD_OFFSET;

	static {
		try {
			DATA_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(ArrayAffineTransformStack.class.getDeclaredField("data"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public ArrayAffineTransformStack clone() {
		ArrayAffineTransformStack result = (ArrayAffineTransformStack) super.clone();
		DynamicUtilities.getUnsafe().putObjectVolatile(result, getDataFieldOffset(),
				result.data.stream()
						.map(AffineTransform::clone)
						.map(AffineTransform.class::cast)
						.collect(Collectors.toCollection(ArrayDeque::new)));
		return result;
	}

	protected static long getDataFieldOffset() {
		return DATA_FIELD_OFFSET;
	}
}
