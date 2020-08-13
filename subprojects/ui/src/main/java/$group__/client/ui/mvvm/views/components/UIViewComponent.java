package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.IUIViewComponent;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.client.ui.mvvm.views.UIView;
import io.reactivex.rxjava3.core.ObservableSource;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;
import java.util.Optional;

public class UIViewComponent<SD extends IShapeDescriptor<?>, M extends IUIComponentManager<? extends SD>>
		extends UIView<SD>
		implements IUIViewComponent<SD, M> {
	protected final M manager;
	protected WeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = new WeakReference<>(null);

	public UIViewComponent(M manager) { this.manager = manager; }

	@Override
	public IUIEventTarget getTargetAtPoint(Point2D point) { return getManager().getComponentAtPoint(point); }

	@Override
	public M getManager() { return manager; }

	@Override
	public IAffineTransformStack getCleanTransformStack() { return getManager().getCleanTransformStack(); }

	@Override
	public Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) { return getManager().changeFocus(currentFocus, next); }

	@Override
	public SD getShapeDescriptor() { return getManager().getShapeDescriptor(); }

	@Override
	public ObservableSource<IBinderAction> getBinderNotifier() {
		return o -> {
			super.getBinderNotifier().subscribe(o);
			getManager().getBinderNotifier().subscribe(o);
		};
	}

	@Override
	public Optional<IUIInfrastructure<?, ?, ?>> getInfrastructure() { return Optional.ofNullable(infrastructure.get()); }

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) { this.infrastructure = new WeakReference<>(infrastructure); }
}
