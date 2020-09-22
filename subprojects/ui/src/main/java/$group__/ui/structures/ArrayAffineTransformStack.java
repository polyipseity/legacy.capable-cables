package $group__.ui.structures;

import $group__.ui.UIConfiguration;
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
	private static final ImmutableList<Function<? super ArrayAffineTransformStack, ?>> OBJECT_VARIABLES = ObjectUtilities.extendsObjectVariables(StaticHolder.getObjectVariables(),
			ImmutableList.of(ArrayAffineTransformStack::getCleanerRef));
	private static final ImmutableMap<String, Function<? super ArrayAffineTransformStack, ?>> OBJECT_VARIABLES_MAP = ObjectUtilities.extendsObjectVariablesMap(getObjectVariables(),
			StaticHolder.getObjectVariablesMap(),
			ImmutableList.of("cleanerRef"));
	private final Deque<AffineTransform> data;
	private final Object cleanerRef = new Object();

	public ArrayAffineTransformStack() { this(CapacityUtilities.INITIAL_CAPACITY_MEDIUM); }

	public ArrayAffineTransformStack(int initialCapacity) {
		this.data = new ArrayDeque<>(initialCapacity);
		this.data.push(new AffineTransform());
		Cleaner.create(getCleanerRef(), new LeakNotifier(this.data, UIConfiguration.getInstance().getLogger()));
	}

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, getObjectVariables()); }

	public static ImmutableList<Function<? super ArrayAffineTransformStack, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

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

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, getObjectVariablesMap()); }

	public static ImmutableMap<String, Function<? super ArrayAffineTransformStack, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
}
