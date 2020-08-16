package $group__.client.ui.mvvm.views.components.paths;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.paths.IUIComponentPath;
import $group__.client.ui.mvvm.structures.AffineTransformStack;
import $group__.client.ui.mvvm.views.paths.UINodePath;
import sun.misc.Cleaner;

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

		Cleaner.create(cleanerRef, transformStack.createCleaner());
	}

	@Override
	public IAffineTransformStack getTransformStackView() { return getTransformStack().copy(); }

	@Override
	public AffineTransform getTransformView(int depth) { return (AffineTransform) getTransformStack().getDelegated().get(Math.floorMod(depth, sizeOfTransformStack())).clone(); }

	protected IAffineTransformStack getTransformStack() { return transformStack; }
}
