package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.structures.IAffineTransformStack;
import $group__.ui.core.structures.IUIComponentContext;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IUIComponentContainer
		extends IUIComponent {
	static void runAsChildren(IUIComponentContainer self, IUIComponentContext context, Runnable call) {
		getAsChildren(self, context, () -> {
			call.run();
			return null;
		});
	}

	@SuppressWarnings("UnusedReturnValue")
	static <R> Optional<R> getAsChildren(IUIComponentContainer self, IUIComponentContext context, Supplier<R> call) {
		IAffineTransformStack transformStack = context.getTransformStack();
		transformStack.push();
		self.transformChildren(transformStack);
		@Nullable R ret = call.get();
		transformStack.pop();
		return Optional.ofNullable(ret);
	}

	void transformChildren(IAffineTransformStack stack);

	@SuppressWarnings("UnusedReturnValue")
	boolean addChildren(Iterable<? extends IUIComponent> components);

	boolean addChildAt(int index, IUIComponent component);

	@SuppressWarnings("UnusedReturnValue")
	boolean removeChildren(Iterable<? extends IUIComponent> components);

	boolean moveChildTo(int index, IUIComponent component);

	@SuppressWarnings("UnusedReturnValue")
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
