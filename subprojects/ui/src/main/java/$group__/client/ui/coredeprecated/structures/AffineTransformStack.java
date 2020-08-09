package $group__.client.ui.coredeprecated.structures;

import $group__.utilities.ObjectUtilities;
import $group__.utilities.interfaces.ICopyable;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.Stack;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class AffineTransformStack implements ICopyable {
	public static final ImmutableList<Function<AffineTransformStack, Object>> OBJECT_VARIABLES = ImmutableList.of(AffineTransformStack::getDelegated);
	public static final ImmutableMap<String, Function<AffineTransformStack, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("delegated"), OBJECT_VARIABLES));
	private static final Logger LOGGER = LogManager.getLogger();
	protected final Stack<AffineTransform> delegated = new Stack<>();

	public AffineTransformStack() {
		getDelegated().push(new AffineTransform());
		{
			Stack<AffineTransform> delegatedRef = getDelegated();
			// TODO see if the lambda will have an implicit ref to this
			Cleaner.create(this, () -> {
				if (!isClean(delegatedRef))
					LOGGER.warn("Stack not clean, content:{}{}", System.lineSeparator(), delegatedRef);
			});
		}
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	public Stack<AffineTransform> getDelegated() { return delegated; }

	protected static boolean isClean(Stack<AffineTransform> delegated) { return delegated.size() == 1 && delegated.get(0).isIdentity(); }

	public static void popMultiple(final AffineTransformStack stack, int times) {
		for (; times > 0; --times)
			stack.getDelegated().pop();
	}

	@SuppressWarnings("UnusedReturnValue")
	public AffineTransform push() { return getDelegated().push((AffineTransform) getDelegated().peek().clone()); }

	@Override
	public AffineTransformStack copy() {
		AffineTransformStack ret = new AffineTransformStack();
		ret.getDelegated().clear();
		getDelegated().forEach(t -> ret.getDelegated().add((AffineTransform) t.clone()));
		return ret;
	}

	public boolean isClean() { return isClean(getDelegated()); }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
}
