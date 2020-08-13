package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.utilities.ObjectUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.Stack;

public class AffineTransformStack implements IAffineTransformStack {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final Stack<AffineTransform> delegated = new Stack<>();

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public AffineTransformStack() {
		getDelegated().push(new AffineTransform());
		{
			Stack<AffineTransform> delegatedRef = getDelegated();
			// TODO see if the lambda will have an implicit ref to this
			Cleaner.create(this, () -> {
				if (!IAffineTransformStack.isClean(delegatedRef))
					LOGGER.warn("Stack not clean, content:{}{}", System.lineSeparator(), delegatedRef);
			});
		}
	}

	@Override
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public final Stack<AffineTransform> getDelegated() { return delegated; }

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
