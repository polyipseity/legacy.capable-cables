package $group__.client.ui.mvvm.core.structures;

import $group__.utilities.interfaces.ICopyable;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.awt.geom.AffineTransform;
import java.util.Stack;
import java.util.function.Function;

public interface IAffineTransformStack
		extends ICopyable {
	ImmutableList<Function<IAffineTransformStack, Object>> OBJECT_VARIABLES = ImmutableList.of(
			IAffineTransformStack::getDelegated);
	ImmutableMap<String, Function<IAffineTransformStack, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("delegated"),
			OBJECT_VARIABLES));

	static void popMultiple(final IAffineTransformStack stack, int times) {
		for (; times > 0; --times)
			stack.getDelegated().pop();
	}

	Stack<AffineTransform> getDelegated();

	@SuppressWarnings("UnusedReturnValue")
	AffineTransform push();

	default boolean isClean() { return isClean(getDelegated()); }

	static boolean isClean(Stack<AffineTransform> delegated) { return delegated.size() == 1 && delegated.get(0).isIdentity(); }
}
