package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.traits.IUIReshapeExplicitly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;

public interface IUIComponentManager
		extends IUIComponent, IUIReshapeExplicitly {
	AffineTransformStack getCleanTransformStack();

	@SuppressWarnings("UnusedReturnValue")
	<CL extends IUIComponent, R> Optional<R> callAsComponent(CL component, BiFunction<? super CL, ? super AffineTransformStack, ? extends R> action);

	@Override
	default void onIndexMove(int previous, int next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	default void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

			/*
			TODO
			@SuppressWarnings("ObjectAllocationInLoop")
			static List<IGuiComponent<?, ?, ?>> getComponentsAtPointAndTransformStack(IGuiComponent<?, ?, ?> self, final AffineTransformStack stack, Point2D point) {
				List<IGuiComponent<?, ?, ?>> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
				Predicate<IGuiComponent<?, ?, ?>> isWithChildren = IContainer.class::isInstance;

				Optional<IGuiComponent<?, ?, ?>> current = Optional.of(self);
				while (current.isPresent()) {
					current = current.filter(isWithChildren).flatMap(r ->  {
						IContainer<?, ?, ?, ?> rwc = (IContainer<?, ?, ?, ?>) r;
						Optional<IGuiComponent<?, ?, ?>> c = rwc.getChildAtPoint(stack, point);
						if (c.isPresent()) {
							stack.push();
							rwc.transformChildren(stack);
							ret.add(c.get());
						}
						return c;
					});
				}
				return ret;
			}
			 */
}
