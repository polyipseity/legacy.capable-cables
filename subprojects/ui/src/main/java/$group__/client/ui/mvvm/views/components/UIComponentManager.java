package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.extensions.IUIExtensionCache;
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
import net.minecraftforge.fml.common.Mod;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class UIComponentManager<SD extends IShapeDescriptor<?, ?>>
		extends UIComponentContainer
		implements IUIComponentManager<SD> {
	protected IUIComponent focus = this;

	public UIComponentManager(Map<String, IUIPropertyMappingValue> propertyMapping) { super(propertyMapping); }

	@Override
	protected abstract SD createShapeDescriptor();

	@SuppressWarnings("unchecked")
	@Override
	public SD getShapeDescriptor() {
		return (SD) super.getShapeDescriptor(); // COMMENT should be safe
	}

	@Override
	public boolean reshape(Function<? super SD, Boolean> action) throws ConcurrentModificationException { return getShapeDescriptor().modify(getShapeDescriptor(), action); }

	public enum CacheManager {
		;

		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIComponentManager>> CHILDREN_FLAT =
				IUIExtensionCache.RegUICache.INSTANCE.register(new ResourceLocation(
								NamespaceUtilities.getNamespacePrefixedString(".", "manager", "children_flat")),
						(Function<? super ResourceLocation, ? extends IUIExtensionCache.IType<List<IUIComponent>, IUIComponentManager>>)
								k -> new IUIExtensionCache.IType.Impl<List<IUIComponent>, IUIComponentManager>(k) {
									{ Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventUIComponentHierarchyChanged.Parent event) {
										// TODO probably wrong event
										if (event.getStage() == EnumEventHookStage.POST)
											invalidate(event.getComponent());
									}

									@Override
									public Optional<List<IUIComponent>> get(IUIComponentManager component) {
										return UIExtensionCache.TYPE.getValue().get(component).flatMap(cache -> ThrowableUtilities.Try.call(() ->
												cache.getDelegated().get(getKey(), () -> {
													List<IUIComponent> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
													TreeUtilities.<IUIComponent, Object>visitNodesDepthFirst(component,
															ret::add,
															p -> p instanceof IUIComponentContainer ?
																	((IUIComponentContainer) p).getChildrenView() : ImmutableSet.of(), null, null);
													return ret;
												}), getLogger()).map(CastUtilities::castUnchecked));
									}
								});
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
	public IUIComponent getFocus() { return focus; }

	@Override
	public void setFocus(IUIComponent focus) {
		// TODO focus events
		this.focus = focus;
	}

	@Override
	public List<IUIComponent> getChildrenFlat() { return CacheManager.CHILDREN_FLAT.getValue().get(this).orElseThrow(InternalError::new); }

	@Override
	public boolean changeFocus(boolean next) {
		ImmutableList<IUIComponent> focs = getChildrenFlat().stream().sequential().filter(IUIComponent::isFocusable).collect(ImmutableList.toImmutableList()); // TODO cache this
		assert !focs.isEmpty();
		IUIComponent foc = focs.get(Math.max(focs.indexOf(getFocus()), 0) + (next ? 1 : -1) % focs.size());
		if (foc.equals(getFocus()))
			return false;
		setFocus(foc);
		return true;
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
