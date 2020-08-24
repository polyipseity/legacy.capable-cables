package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.core.mvvm.binding.IBinderAction;
import $group__.client.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.client.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.client.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.client.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.client.ui.mvvm.views.UIView;
import io.reactivex.rxjava3.core.Observer;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UIViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIView<S>
		implements IUIViewComponent<S, M> {
	protected final M manager;

	public UIViewComponent(M manager) { this.manager = manager; }

	@Override
	public IUIEventTarget getTargetAtPoint(Point2D point) { return getManager().getPathResolver().resolvePath(point, true).getPathEnd(); }

	@Override
	public M getManager() { return manager; }

	@Override
	public IAffineTransformStack getCleanTransformStack() { return getManager().getCleanTransformStack(); }

	@Override
	public Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) { return getManager().changeFocus(currentFocus, next); }

	@Override
	public Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() {
		return s -> {
			super.getBinderSubscriber().accept(s);
			getManager().getBinderSubscriber().accept(s);
		};
	}

	@Override
	public boolean reshape(Function<? super IShapeDescriptor<? super S>, ? extends Boolean> action) throws ConcurrentModificationException { return getManager().reshape(action); }
}
