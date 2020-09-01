package $group__.ui.mvvm.structures;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.utilities.CastUtilities;
import $group__.utilities.ObjectUtilities;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.Stack;

public class AffineTransformStack
		implements IAffineTransformStack {
	protected final Stack<AffineTransform> data = new Stack<>();
	protected final Object cleanerRef = new Object();

	public AffineTransformStack() {
		data.push(new AffineTransform());
		Cleaner.create(getCleanerRef(), new LeakNotifier(data));
	}

	protected final Object getCleanerRef() { return cleanerRef; }

	@Override
	public AffineTransformStack copy() {
		AffineTransformStack ret = new AffineTransformStack();
		ret.getData().clear();
		getData().stream().sequential()
				.map(AffineTransform::clone)
				.map(CastUtilities::<AffineTransform>castUnchecked)
				.forEachOrdered(ret.getData()::add);
		return ret;
	}

	@Override
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Stack<AffineTransform> getData() { return data; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
}
