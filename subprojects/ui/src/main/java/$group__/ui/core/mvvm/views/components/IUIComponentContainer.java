package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.paths.IUINode;
import $group__.utilities.CastUtilities;

import java.util.List;
import java.util.function.Supplier;

public interface IUIComponentContainer
		extends IUIComponent {
	static void runWithStackTransformed(IUIComponentContainer self, IAffineTransformStack stack, Runnable call) {
		getWithStackTransformed(self, stack, () -> {
			call.run();
			return null;
		});
	}

	static <R> R getWithStackTransformed(IUIComponentContainer self, IAffineTransformStack stack, Supplier<R> call) {
		stack.push();
		self.transformChildren(stack);
		R ret = call.get();
		stack.getDelegated().pop();
		return ret;
	}

	void transformChildren(IAffineTransformStack stack);

	boolean addChildren(Iterable<? extends IUIComponent> components);

	boolean addChildAt(int index, IUIComponent component);

	boolean removeChildren(Iterable<? extends IUIComponent> components);

	boolean moveChildTo(int index, IUIComponent component);

	boolean moveChildToTop(IUIComponent component);

	@Override
	default List<IUINode> getChildNodes() { return CastUtilities.castUnchecked(getChildrenView()); }

	List<IUIComponent> getChildrenView();
}
