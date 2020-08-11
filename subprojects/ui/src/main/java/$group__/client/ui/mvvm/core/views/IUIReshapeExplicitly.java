package $group__.client.ui.mvvm.core.views;

import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

@FunctionalInterface
public interface IUIReshapeExplicitly<SD extends IShapeDescriptor<?, ?>> {
	static boolean refresh(IUIReshapeExplicitly<?> trait) { return trait.reshape(s -> true); }

	boolean reshape(Function<? super SD, Boolean> action)
			throws ConcurrentModificationException;
}
