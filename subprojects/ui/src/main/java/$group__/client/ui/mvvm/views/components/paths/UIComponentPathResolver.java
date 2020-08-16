package $group__.client.ui.mvvm.views.components.paths;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.paths.IUIComponentPath;
import $group__.client.ui.mvvm.core.views.components.paths.IUIComponentPathResolver;
import $group__.client.ui.mvvm.views.paths.UINodePathResolver;
import $group__.utilities.CapacityUtilities;
import com.google.common.collect.Lists;

import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class UIComponentPathResolver
		extends UINodePathResolver<IUIComponent>
		implements IUIComponentPathResolver<IUIComponent> {
	protected final WeakReference<IUIComponentManager<?>> manager;

	public UIComponentPathResolver(IUIComponentManager<?> manager) {
		this.manager = new WeakReference<>(manager);
	}

	@Override
	public IUIComponentPath resolvePath(Point2D point, boolean virtual) {
		return getManager().map(m -> {
			// todo strategy or something
			IAffineTransformStack stack = m.getCleanTransformStack();
			final int[] popTimes = new int[1];
			List<IUIComponent> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
			Predicate<IUIComponent> isWithChildren = IUIComponentContainer.class::isInstance;
			ret.add(m);

			Optional<IUIComponent> current = Optional.of(m);
			while (current.isPresent()) {
				current = current.filter(isWithChildren).flatMap(r -> {
					IUIComponentContainer rwc = (IUIComponentContainer) r;
					List<IUIComponent> rwcCV = rwc.getChildrenView();

					// todo algo
					Optional<IUIComponent> c = Optional.empty();
					rwcCLoop:
					for (IUIComponent rwcC : Lists.reverse(rwcCV)) {
						for (IUIComponent rwcCVE : Lists.reverse(getVirtualElements(rwcC))) {
							if (stack.getDelegated().peek().createTransformedShape(
									rwcCVE.getShapeDescriptor().getShapeOutput()).contains(point)) {
								c = Optional.of(virtual ? rwcCVE : rwcC);
								break rwcCLoop;
							}
						}
						if (stack.getDelegated().peek().createTransformedShape(
								rwcC.getShapeDescriptor().getShapeOutput()).contains(point)) {
							c = Optional.of(rwcC);
							break;
						}
					}

					c.ifPresent(cc -> {
						stack.push();
						rwc.transformChildren(stack);
						ret.add(cc);
						++popTimes[0];
					});
					return c;
				});
			}
			IAffineTransformStack.popMultiple(stack, popTimes[0]);

			return new UIComponentPath<>(ret);
		}).orElseThrow(IllegalStateException::new);
	}

	protected Optional<IUIComponentManager<?>> getManager() { return Optional.ofNullable(manager.get()); }
}
