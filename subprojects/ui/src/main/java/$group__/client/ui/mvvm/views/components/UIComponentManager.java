package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.IUIComponentShapeAnchorController;
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
import java.awt.*;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class UIComponentManager<S extends Shape>
		extends UIComponentContainer
		implements IUIComponentManager<S> {
	protected final IUIComponentPathResolver<IUIComponent> pathResolver = new PathResolver();
	protected final IUIComponentShapeAnchorController shapeAnchorController = new UIComponentShapeAnchorController();

	public UIComponentManager(IShapeDescriptor<S> shapeDescriptor, Map<ResourceLocation, IUIPropertyMappingValue> mapping) { super(shapeDescriptor, mapping); }

	@Override
	public IUIComponentPathResolver<IUIComponent> getPathResolver() { return pathResolver; }

	@Override
	public boolean reshape(Function<? super IShapeDescriptor<? super S>, ? extends Boolean> action) throws ConcurrentModificationException { return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action); }

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<S> getShapeDescriptor() {
		return (IShapeDescriptor<S>) super.getShapeDescriptor(); // COMMENT should be safe
	}

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
	public List<IUIComponent> getChildrenFlatView() { return ImmutableList.copyOf(CacheManager.CHILDREN_FLAT.getValue().get(this).orElseThrow(InternalError::new)); }

	public enum CacheManager {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		@SuppressWarnings("UnstableApiUsage")
		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIComponentManager<?>>> CHILDREN_FLAT =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() -> {
									@SuppressWarnings("unchecked") @Nullable List<WeakReference<IUIComponent>> cv =
											(List<WeakReference<IUIComponent>>) cache.getDelegated().getIfPresent(t.getKey());
									List<IUIComponent> ret;
									if (cv == null) {
										ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
										TreeUtilities.<IUIComponent, Object>visitNodesDepthFirst(i,
												ret::add,
												p -> p instanceof IUIComponentContainer ?
														((IUIComponentContainer) p).getChildrenView() : ImmutableSet.of(), null, null);
										cache.getDelegated().put(t.getKey(),
												ret.stream().sequential()
														.map(WeakReference::new)
														.collect(Collectors.toList()));
									} else {
										cv.removeIf(wr ->
												wr.get() == null);
										ret = cv.stream().sequential()
												.map(Reference::get)
												.filter(Objects::nonNull)
												.collect(ImmutableList.toImmutableList());
									}
									return ret;
								}, LOGGER).map(CastUtilities::castUnchecked)),
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
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() -> {
									@SuppressWarnings("unchecked") @Nullable List<WeakReference<IUIComponent>> cv =
											(List<WeakReference<IUIComponent>>) cache.getDelegated().getIfPresent(t.getKey());
									List<IUIComponent> ret;
									if (cv == null) {
										ret = i.getChildrenFlatView().stream().sequential()
												.filter(IUIComponent::isFocusable)
												.collect(ImmutableList.toImmutableList());
										cache.getDelegated().put(t.getKey(),
												ret.stream().sequential()
														.map(WeakReference::new)
														.collect(Collectors.toList()));
									} else {
										cv.removeIf(wr ->
												wr.get() == null);
										ret = cv.stream().sequential()
												.map(Reference::get)
												.filter(Objects::nonNull)
												.collect(ImmutableList.toImmutableList());
									}
									return ret;
								}, LOGGER).map(CastUtilities::castUnchecked)),
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

	@Override
	public IUIComponentShapeAnchorController getShapeAnchorController() { return shapeAnchorController; }

	protected class PathResolver extends UIComponentPathResolver {
		protected PathResolver() { super(UIComponentManager.this); }
	}
}
