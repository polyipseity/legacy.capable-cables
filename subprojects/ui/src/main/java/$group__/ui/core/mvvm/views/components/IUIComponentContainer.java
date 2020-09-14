package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IUIComponentContainer
		extends IUIComponent {
	static void runWithStackTransformed(IUIComponentContainer self, IAffineTransformStack stack, Runnable call) {
		getWithStackTransformed(self, stack, () -> {
			call.run();
			return null;
		});
	}

	static <R> R getWithStackTransformed(IUIComponentContainer self, IAffineTransformStack stack, Supplier<R> call) {
		stack.push();
		self.transformChildren(stack);
		R ret = call.get();
		stack.pop();
		return ret;
	}

	void transformChildren(IAffineTransformStack stack);

	boolean addChildren(Iterable<? extends IUIComponent> components);

	boolean addChildAt(int index, IUIComponent component);

	boolean removeChildren(Iterable<? extends IUIComponent> components);

	boolean moveChildTo(int index, IUIComponent component);

	boolean moveChildToTop(IUIComponent component);

	@Override
	default List<? extends IUIComponent> getChildNodes() { return getChildrenView(); }

	@SuppressWarnings("UnstableApiUsage")
	default Map<String, IUIComponent> getNamedChildrenMapView() {
		return getChildrenView().stream().unordered()
				.filter(c -> c.getID().isPresent())
				.collect(ImmutableMap.toImmutableMap(c -> {
					Optional<? extends String> id = c.getID();
					assert id.isPresent();
					return id.get();
				}, Function.identity()));
	}

	List<IUIComponent> getChildrenView();
}