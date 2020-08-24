package $group__.ui.mvvm.views.components.paths;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPath;
import $group__.ui.mvvm.structures.AffineTransformStack;
import $group__.ui.mvvm.views.paths.UINodePath;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.util.List;

public class UIComponentPath<T extends IUIComponent>
		extends UINodePath<T>
		implements IUIComponentPath {
	protected final IAffineTransformStack transformStack = new AffineTransformStack();
	protected final Object cleanerRef = new Object();

	public UIComponentPath(List<? extends T> delegated) {
		super(delegated);

		for (IUIComponent c : delegated) {
			transformStack.push();
			if (c instanceof IUIComponentContainer)
				((IUIComponentContainer) c).transformChildren(transformStack);
		}
		transformStack.getDelegated().pop();

		Cleaner.create(getCleanerRef(), transformStack.createCleaner());
	}

	protected final Object getCleanerRef() { return cleanerRef; }

	@Override
	public IAffineTransformStack getTransformStackView() { return getTransformStack().copy(); }

	@Override
	public AffineTransform getTransformView(int depth) {
		@Nullable AffineTransform t = getTransformStack().getDelegated().get(Math.floorMod(depth, sizeOfTransformStack()));
		assert t != null;
		return (AffineTransform) t.clone();
	}

	protected IAffineTransformStack getTransformStack() { return transformStack; }
}
