package $group__.ui.structures;

import $group__.ui.core.structures.IAffineTransformStack;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.ObjectUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;

public class ArrayAffineTransformStack
		implements IAffineTransformStack {
	public static final ImmutableList<Function<? super ArrayAffineTransformStack, ?>> OBJECT_VARIABLES = ObjectUtilities.extendsObjectVariables(IAffineTransformStack.OBJECT_VARIABLES,
			ImmutableList.of(ArrayAffineTransformStack::getCleanerRef));
	public static final ImmutableMap<String, Function<? super ArrayAffineTransformStack, ?>> OBJECT_VARIABLES_MAP = ObjectUtilities.extendsObjectVariablesMap(OBJECT_VARIABLES,
			IAffineTransformStack.OBJECT_VARIABLES_MAP,
			ImmutableList.of("cleanerRef"));
	protected final Deque<AffineTransform> data;
	protected final Object cleanerRef = new Object();

	public ArrayAffineTransformStack() { this(CapacityUtilities.INITIAL_CAPACITY_MEDIUM); }

	public ArrayAffineTransformStack(int initialCapacity) {
		this.data = new ArrayDeque<>(initialCapacity);
		this.data.push(new AffineTransform());
		Cleaner.create(getCleanerRef(), new LeakNotifier(this.data));
	}

	protected final Object getCleanerRef() { return cleanerRef; }

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

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
}
