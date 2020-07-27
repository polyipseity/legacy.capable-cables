package $group__.client.gui.structures;

import $group__.utilities.traits.ICopyable;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.AffineTransform;
import java.util.Stack;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class AffineTransformStack implements ICopyable {
	public final Stack<AffineTransform> delegated = new Stack<>();

	public AffineTransformStack() { delegated.push(new AffineTransform()); }

	@SuppressWarnings("UnusedReturnValue")
	public AffineTransform push() { return delegated.push((AffineTransform) delegated.peek().clone()); }

	@Override
	public AffineTransformStack copy() {
		AffineTransformStack ret = new AffineTransformStack();
		ret.delegated.clear();
		delegated.forEach(t -> ret.delegated.add((AffineTransform) t.clone()));
		return ret;
	}
}
