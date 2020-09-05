package $group__.ui.mvvm.views.components.paths;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPath;
import $group__.ui.mvvm.structures.AffineTransformStack;
import $group__.ui.mvvm.views.paths.UINodePath;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.CastUtilities;
import com.google.common.collect.Iterators;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.function.Consumer;

public class UIComponentPath<T extends IUIComponent>
		extends UINodePath<T>
		implements IUIComponentPath {
	protected final IAffineTransformStack transformStack = new AffineTransformStack();
	protected final Object cleanerRef = new Object();

	public UIComponentPath(List<? extends T> data) {
		super(data);

		Consumer<IUIComponentContainer> transformer = c -> c.transformChildren(transformStack);
		for (IUIComponent c : this.data) {
			transformStack.push();
			CastUtilities.castChecked(IUIComponentContainer.class, c).ifPresent(transformer);
		}
		transformStack.pop();

		Cleaner.create(getCleanerRef(), transformStack.createCleaner());
	}

	protected final Object getCleanerRef() { return cleanerRef; }

	@Override
	public IAffineTransformStack getTransformStackView() { return getTransformStack().copy(); }

	@Override
	public AffineTransform getTransformView(int depth) {
		return (AffineTransform) Iterators.get(
				AssertionUtilities.assertNonnull(getTransformStack().getData().descendingIterator()),
				Math.floorMod(depth, sizeOfTransformStack()))
				.clone();
	}

	protected IAffineTransformStack getTransformStack() { return transformStack; }
}
