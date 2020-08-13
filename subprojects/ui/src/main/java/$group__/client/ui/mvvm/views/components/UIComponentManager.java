package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.IUIExtensionCache;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.client.ui.mvvm.structures.AffineTransformStack;
import $group__.client.ui.mvvm.views.components.extensions.UIExtensionCache;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.specific.ThrowableUtilities;
import $group__.utilities.structures.Registry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class UIComponentManager<SD extends IShapeDescriptor<?>>
		extends UIComponentContainer
		implements IUIComponentManager<SD> {
	public UIComponentManager(Map<String, IUIPropertyMappingValue> propertyMapping) { super(propertyMapping); }

	@Override
	protected abstract SD createShapeDescriptor();

	@SuppressWarnings("unchecked")
	@Override
	public SD getShapeDescriptor() {
		return (SD) super.getShapeDescriptor(); // COMMENT should be safe
	}

	@Override
	public boolean reshape(Function<? super SD, ? extends Boolean> action) throws ConcurrentModificationException { return getShapeDescriptor().modify(getShapeDescriptor(), action); }

	@Override
	public Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) {
		if (currentFocus instanceof IUIComponent) {
			@SuppressWarnings("UnstableApiUsage") ImmutableList<IUIComponent> focs = getChildrenFlat().stream().sequential().filter(IUIComponent::isFocusable).collect(ImmutableList.toImmutableList()); // TODO cache this maybe
			assert !focs.isEmpty();
			return Optional.ofNullable(focs.get(Math.max(focs.indexOf(currentFocus), 0) + (next ? 1 : -1) % focs.size()));
		}
		return Optional.of(this);
	}

	@Override
	public IAffineTransformStack getCleanTransformStack() { return new AffineTransformStack(); }


	@Override
	public <CL extends IUIComponent, R> Optional<R> callAsComponent(CL component, BiFunction<? super CL, ? super IAffineTransformStack, ? extends R> action) {
		IAffineTransformStack stack = getCleanTransformStack();
		ImmutableList<IUIComponent> path = IUIComponent.computeComponentPath(component);
		if (!path.contains(this))
			return Optional.empty();
		int popTimes = 0;
		for (IUIComponent element : path) {
			if (element instanceof IUIComponentContainer) {
				((IUIComponentContainer) element).transformChildren(stack);
				++popTimes;
			}
		}
		IAffineTransformStack.popMultiple(stack, popTimes);
		return Optional.ofNullable(action.apply(component, stack));
	}

	@Override
	public List<IUIComponent> getChildrenFlat() { return CacheManager.CHILDREN_FLAT.getValue().get(this).orElseThrow(InternalError::new); }

	public enum CacheManager {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIComponent>> CHILDREN_FLAT =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> UIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() ->
										cache.getDelegated().get(t.getKey(), () -> {
											List<IUIComponent> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
											TreeUtilities.<IUIComponent, Object>visitNodesDepthFirst(i,
													ret::add,
													p -> p instanceof IUIComponentContainer ?
															((IUIComponentContainer) p).getChildrenView() : ImmutableSet.of(), null, null);
											return ret;
										}), LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> {
									IUIExtensionCache.IType.invalidate(i, t.getKey());
									if (i instanceof IUIComponentContainer)
										((IUIComponentContainer) i).getChildrenView().forEach(t::invalidate);
								},
								t -> new Object() {
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											t.invalidate(event.getComponent());
									}
								}));

		private static ResourceLocation generateKey(@SuppressWarnings("SameParameterValue") String name) { return new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, "manager." + name); }
	}

	@Override
	public IUIComponent getComponentAtPoint(Point2D point) {
		IAffineTransformStack stack = getCleanTransformStack();
		AtomicInteger popTimes = new AtomicInteger();
		List<IUIComponent> path = IUIComponentManager.getComponentPathAtPointAndTransformStack(this, stack, point, popTimes);
		assert !path.isEmpty();
		IAffineTransformStack.popMultiple(stack, popTimes.get());
		return path.get(path.size() - 1);
	}
}
