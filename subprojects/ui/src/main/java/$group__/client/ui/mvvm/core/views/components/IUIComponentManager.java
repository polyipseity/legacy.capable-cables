package $group__.client.ui.mvvm.core.views.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.utilities.CapacityUtilities;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface IUIComponentManager<SD extends IShapeDescriptor<?>>
		extends IUIComponent, IUIReshapeExplicitly<SD> {
	@SuppressWarnings("ObjectAllocationInLoop")
	static List<IUIComponent> getComponentPathAtPointAndTransformStack(IUIComponent self, final IAffineTransformStack stack, Point2D point, final AtomicInteger popTimes) {
		popTimes.set(0);
		List<IUIComponent> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		Predicate<IUIComponent> isWithChildren = IUIComponentContainer.class::isInstance;
		ret.add(self);

		Optional<IUIComponent> current = Optional.of(self);
		while (current.isPresent()) {
			current = current.filter(isWithChildren).flatMap(r -> {
				IUIComponentContainer rwc = (IUIComponentContainer) r;
				Optional<IUIComponent> c = rwc.getChildAtPoint(stack, point);
				if (c.isPresent()) {
					stack.push();
					rwc.transformChildren(stack);
					ret.add(c.get());
					popTimes.incrementAndGet();
				}
				return c;
			});
		}
		return ret;
	}

	IAffineTransformStack getCleanTransformStack();

	<CL extends IUIComponent, R> Optional<R> callAsComponent(CL component, BiFunction<? super CL, ? super IAffineTransformStack, ? extends R> action);

	List<IUIComponent> getChildrenFlat();

	Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);

	IUIComponent getComponentAtPoint(Point2D point);

	@Override
	SD getShapeDescriptor();

	@Override
	default void onIndexMove(int previous, int next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	default void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	default boolean isFocusable() { return true; }
}
