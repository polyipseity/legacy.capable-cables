package $group__.ui.mvvm.views.components.paths;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPath;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import $group__.ui.mvvm.views.paths.UINodePathResolver;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.collections.ManualLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class UIComponentPathResolver
		extends UINodePathResolver<IUIComponent>
		implements IUIComponentPathResolver<IUIComponent> {
	protected final WeakReference<IUIComponentManager<?>> manager;
	protected final LoadingCache<IUIComponent, List<Consumer<? super IAffineTransformStack>>> childrenTransformers =
			ManualLoadingCache.newNestedLoadingCacheCollection(CacheUtilities.newCacheBuilderSingleThreaded()
					.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL)
					.weakKeys()
					.build(CacheLoader.from(() -> new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL))));

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

			ret.add(m);
			@Nullable IUIComponent current = m;
			while (current != null) {
				current = CastUtilities.castChecked(IUIComponentContainer.class, current)
						.map(container -> {
							AffineTransform transform = stack.push();
							container.transformChildren(stack);
							Optional.ofNullable(getChildrenTransformers().getIfPresent(container))
									.ifPresent(ts -> ts.forEach(t -> t.accept(stack)));
							++popTimes[0];

							@Nullable IUIComponent r = null;
							childrenLoop:
							for (IUIComponent child : Lists.reverse(container.getChildrenView())) {
								@Nullable List<IUIComponent> cVe = getVirtualElements().getIfPresent(child);
								if (cVe != null) {
									for (IUIComponent childVirtualElement : Lists.reverse(cVe)) {
										if (transform.createTransformedShape(
												childVirtualElement.getShapeDescriptor().getShapeOutput()).contains(point)) {
											r = virtual ? childVirtualElement : child;
											break childrenLoop;
										}
									}
								}
								if (transform.createTransformedShape(
										child.getShapeDescriptor().getShapeOutput()).contains(point)) {
									r = child;
									break;
								}
							}

							if (r != null)
								ret.add(r);
							return r;
						})
						.orElse(null);
			}
			IAffineTransformStack.popMultiple(stack, popTimes[0]);

			return new UIComponentPath<>(ret);
		}).orElseThrow(IllegalStateException::new);
	}

	protected LoadingCache<IUIComponent, List<Consumer<? super IAffineTransformStack>>> getChildrenTransformers() { return childrenTransformers; }

	@Override
	public boolean addChildrenTransformer(IUIComponent element, Consumer<? super IAffineTransformStack> transformer) { return getChildrenTransformers().getUnchecked(element).add(transformer); }

	protected Optional<? extends IUIComponentManager<?>> getManager() { return Optional.ofNullable(manager.get()); }

	@Override
	public boolean removeChildrenTransformer(IUIComponent element, Consumer<? super IAffineTransformStack> transformer) {
		boolean ret = Optional.ofNullable(getChildrenTransformers().getIfPresent(element))
				.filter(ts -> ts.remove(transformer))
				.isPresent();
		if (ret)
			getChildrenTransformers().cleanUp();
		return ret;
	}
}
