package $group__.client.ui.mvvm.views.domlike.components;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.structures.UIKeyboardKeyPressData;
import $group__.client.ui.coredeprecated.structures.UIMouseButtonPressData;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IUIComponentContainerDOMLike
		extends IUIComponentDOMLike {
	static boolean onChangeFocusWithThisFocusable(IUIComponentContainerDOMLike self, Function<Boolean, Boolean> superMethod, boolean next) {
		return next ? !self.getFocus().isPresent() && !IUIComponentDOMLike.isBeingFocused(self) || superMethod.apply(true)
				: superMethod.apply(false) || !IUIComponentDOMLike.isBeingFocused(self);
	}

	Optional<IUIComponentDOMLike> getFocus();

	static void runWithStackTransformed(IUIComponentContainerDOMLike self, final AffineTransformStack stack, Runnable call) {
		getWithStackTransformed(self, stack, () -> {
			call.run();
			return null;
		});
	}

	static <R> R getWithStackTransformed(IUIComponentContainerDOMLike self, final AffineTransformStack stack, Supplier<R> call) {
		stack.push();
		self.transformChildren(stack);
		R ret = call.get();
		stack.getDelegated().pop();
		return ret;
	}

	void transformChildren(final AffineTransformStack stack);

	void setFocus(final AffineTransformStack stack, @Nullable IUIComponentDOMLike focus);

	Optional<IUIComponentDOMLike> getMouseHover();

	Map<Integer, UIKeyboardKeyPressData.Tracked> getKeyboardKeysBeingPressedView();

	Map<Integer, UIMouseButtonPressData.Tracked> getMouseButtonsBeingPressedView();

	List<IUIComponentDOMLike> getChildrenView();

	boolean addChildren(Iterable<? extends IUIComponentDOMLike> components);

	boolean addChildAt(int index, IUIComponentDOMLike component);

	boolean removeChildren(Iterable<? extends IUIComponentDOMLike> components);

	boolean moveChildTo(int index, IUIComponentDOMLike component);

	boolean moveChildToTop(IUIComponentDOMLike component);

	Optional<IUIComponentDOMLike> getChildAtPoint(final AffineTransformStack stack, Point2D point);
}
