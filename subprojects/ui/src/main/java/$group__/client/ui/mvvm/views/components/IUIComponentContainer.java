package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.mvvm.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.structures.IUIDataMouseButtonClick;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IUIComponentContainer
		extends IUIComponent {
	static boolean onChangeFocusWithThisFocusable(IUIComponentContainer self, Function<Boolean, Boolean> superMethod, boolean next) {
		return next ? !self.getFocus().isPresent() && !isBeingFocused(self) || superMethod.apply(true)
				: superMethod.apply(false) || !isBeingFocused(self);
	}

	Optional<IUIComponent> getFocus();

	static void runWithStackTransformed(IUIComponentContainer self, final AffineTransformStack stack, Runnable call) {
		getWithStackTransformed(self, stack, () -> {
			call.run();
			return null;
		});
	}

	static <R> R getWithStackTransformed(IUIComponentContainer self, final AffineTransformStack stack, Supplier<R> call) {
		stack.push();
		self.transformChildren(stack);
		R ret = call.get();
		stack.getDelegated().pop();
		return ret;
	}

	void transformChildren(final AffineTransformStack stack);

	void setFocus(final AffineTransformStack stack, @Nullable IUIComponent focus);

	Optional<IUIComponent> getMouseHover();

	Map<Integer, IUIDataKeyboardKeyPress.Tracked> getKeyboardKeysBeingPressedView();

	Map<Integer, IUIDataMouseButtonClick.Tracked> getMouseButtonsBeingPressedView();

	List<IUIComponent> getChildrenView();

	boolean addChildren(Iterable<? extends IUIComponent> components);

	boolean addChildAt(int index, IUIComponent component);

	boolean removeChildren(Iterable<? extends IUIComponent> components);

	boolean moveChildTo(int index, IUIComponent component);

	boolean moveChildToTop(IUIComponent component);

	Optional<IUIComponent> getChildAtPoint(final AffineTransformStack stack, Point2D point);
}
