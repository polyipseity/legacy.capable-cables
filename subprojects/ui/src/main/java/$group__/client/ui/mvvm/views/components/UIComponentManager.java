package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.core.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.IUIExtensionCache;
import $group__.client.ui.mvvm.core.views.components.paths.IUIComponentPathResolver;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.client.ui.mvvm.structures.AffineTransformStack;
import $group__.client.ui.mvvm.views.components.paths.UIComponentPathResolver;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.*;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.Registry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public abstract class UIComponentManager<SD extends IShapeDescriptor<?>>
		extends UIComponentContainer
		implements IUIComponentManager<SD> {
	protected final IUIComponentPathResolver<IUIComponent> pathResolver = new PathResolver();

	public UIComponentManager(Map<String, IUIPropertyMappingValue> propertyMapping) { super(propertyMapping); }

	@Override
	protected abstract SD createShapeDescriptor();

	@Override
	public IUIComponentPathResolver<IUIComponent> getPathResolver() { return pathResolver; }

	@SuppressWarnings("unchecked")
	@Override
	public SD getShapeDescriptor() {
		return (SD) super.getShapeDescriptor(); // COMMENT should be safe
	}

	@Override
	public boolean reshape(Function<? super SD, ? extends Boolean> action) throws ConcurrentModificationException { return getShapeDescriptor().modify(() -> action.apply(getShapeDescriptor())); }

	@Override
	public Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) {
		Optional<IUIEventTarget> ret = Optional.of(this);
		if (currentFocus instanceof IUIComponent) {
			ret = CacheManager.CHILDREN_FLAT_FOCUSABLE.getValue().get(this)
					.filter(f -> !f.isEmpty())
					.map(f -> f.get(Math.floorMod(
							Math.max(f.indexOf(currentFocus), 0)
									+ (next ? 1 : -1),
							f.size())));
		}
		return ret;
	}

	@Override
	public IAffineTransformStack getCleanTransformStack() { return new AffineTransformStack(); }

	@Override
	public List<IUIComponent> getChildrenFlat() { return CacheManager.CHILDREN_FLAT.getValue().get(this).orElseThrow(InternalError::new); }

	public enum CacheManager {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIComponentManager<?>>> CHILDREN_FLAT =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() ->
										cache.getDelegated().get(t.getKey(), () -> {
											List<IUIComponent> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
											TreeUtilities.<IUIComponent, Object>visitNodesDepthFirst(i,
													ret::add,
													p -> p instanceof IUIComponentContainer ?
															((IUIComponentContainer) p).getChildrenView() : ImmutableSet.of(), null, null);
											return ret;
										}), LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST) {
											IUIComponent ec = event.getComponent();
											if (ec instanceof IUIComponentManager)
												t.invalidate((IUIComponentManager<?>) ec);
										}
									}
								})));
		@SuppressWarnings("UnstableApiUsage")
		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIComponentManager<?>>> CHILDREN_FLAT_FOCUSABLE =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat.focusable"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() ->
										cache.getDelegated().get(t.getKey(), () ->
												i.getChildrenFlat().stream().sequential()
														.filter(IUIComponent::isFocusable)
														.collect(ImmutableList.toImmutableList())
										), LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST) {
											IUIComponent ec = event.getComponent();
											if (ec instanceof IUIComponentManager)
												t.invalidate((IUIComponentManager<?>) ec);
										}
									}
								})));

		private static ResourceLocation generateKey(@SuppressWarnings("SameParameterValue") String name) { return new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, "manager." + name); }
	}

	protected class PathResolver extends UIComponentPathResolver {
		protected PathResolver() { super(UIComponentManager.this); }
	}
}