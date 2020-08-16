package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.utilities.ObjectUtilities;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.Stack;

public class AffineTransformStack implements IAffineTransformStack {
	protected final Stack<AffineTransform> delegated = new Stack<>();
	protected final Object cleanerRef = new Object();

	public AffineTransformStack() {
		delegated.push(new AffineTransform());
		Cleaner.create(cleanerRef, new LeakNotifier(delegated));
	}

	@Override
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Stack<AffineTransform> getDelegated() { return delegated; }

	@Override
	@SuppressWarnings("UnusedReturnValue")
	public AffineTransform push() { return getDelegated().push((AffineTransform) getDelegated().peek().clone()); }

	@Override
	public AffineTransformStack copy() {
		AffineTransformStack ret = new AffineTransformStack();
		ret.getDelegated().clear();
		getDelegated().forEach(t -> ret.getDelegated().add((AffineTransform) t.clone()));
		return ret;
	}

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
}
