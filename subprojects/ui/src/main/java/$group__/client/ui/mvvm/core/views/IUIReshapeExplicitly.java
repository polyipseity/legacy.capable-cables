package $group__.client.ui.mvvm.core.views;

import $group__.client.ui.core.IShapeDescriptor;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIReshapeExplicitly<SD extends IShapeDescriptor<?>> {
	static boolean refresh(IUIReshapeExplicitly<?> trait) { return trait.reshape(s -> false); }

	boolean reshape(Function<? super SD, ? extends Boolean> action)
			throws ConcurrentModificationException;
}
