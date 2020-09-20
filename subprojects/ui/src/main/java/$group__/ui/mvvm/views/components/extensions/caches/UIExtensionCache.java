package $group__.ui.mvvm.views.components.extensions.caches;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUIExtensionCache;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUIExtensionCacheType;
import $group__.ui.core.mvvm.views.components.extensions.caches.UICacheRegistry;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CastUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Optional;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIExtensionCache
		extends ExtensionContainerAware<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>, IExtensionContainer<INamespacePrefixedString>>
		implements IUIExtensionCache {
	protected final Cache<INamespacePrefixedString, Object> cache =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(CacheUtilities.CACHE_EXPIRATION_ACCESS_DURATION, CacheUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.NO_ARGS)
	public UIExtensionCache() {
		super(CastUtilities.castUnchecked(IExtensionContainer.class), CastUtilities.castUnchecked(IExtensionContainer.class)); // COMMENT should not matter in this case
	}

	@Override
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getDelegated().invalidateAll();
	}

	@Override
	public Cache<INamespacePrefixedString, Object> getDelegated() { return cache; }

	@Override
	public IExtension.IType<? extends INamespacePrefixedString, ?, ? extends IExtensionContainer<INamespacePrefixedString>> getType() { return IUIExtensionCache.TYPE.getValue(); }

	public enum CacheUniversal {
		;

		public static final Registry.RegistryObject<IUIExtensionCacheType<IUIComponentManager<?>, IUIComponent>> MANAGER =
				UICacheRegistry.getInstance().registerApply(generateKey("manager"),
						k -> new IUIExtensionCacheType.Impl<>(k,
								(t, i) ->
										IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache ->
												Try.call(() -> {
													@SuppressWarnings("unchecked") @Nullable WeakReference<IUIComponentManager<?>> cv =
															(WeakReference<IUIComponentManager<?>>) cache.getDelegated().getIfPresent(t.getKey());
													@Nullable IUIComponentManager<?> ret;
													if (cv == null || (ret = cv.get()) == null) {
														ret = IUIComponent.getYoungestParentInstanceOf(i, IUIComponentManager.class)
																.orElse(null);
														cache.getDelegated().put(t.getKey(),
																new WeakReference<>(ret));
													}
													return ret;
												}, UIConfiguration.getInstance().getLogger())),
								(t, i) -> {
									IUIExtensionCacheType.invalidateImpl(i, t.getKey());
									CastUtilities.castChecked(IUIComponentContainer.class, i)
											.ifPresent(ic -> IUIExtensionCacheType.invalidateChildrenImpl(ic, t));
								},
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage().isPost())
											t.invalidate(event.getComponent());
									}
								})));
		public static final Registry.RegistryObject<IUIExtensionCacheType<Integer, IUIComponent>> Z =
				UICacheRegistry.getInstance().registerApply(generateKey("z"),
						k -> new IUIExtensionCacheType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> Try.call(() -> cache.getDelegated().get(t.getKey(),
										() -> {
											int ret = -1;
											for (Optional<? extends IUIComponent> parent = Optional.of(i);
											     parent.isPresent();
											     parent = parent.flatMap(IUIComponent::getParent))
												++ret;
											return ret;
										}), UIConfiguration.getInstance().getLogger()).map(CastUtilities::castUnchecked)),
								(t, i) -> {
									IUIExtensionCacheType.invalidateImpl(i, t.getKey());
									CastUtilities.castChecked(IUIComponentContainer.class, i)
											.ifPresent(ic -> IUIExtensionCacheType.invalidateChildrenImpl(ic, t));
								},
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage().isPost())
											t.invalidate(event.getComponent());
									}
								})));

		private static INamespacePrefixedString generateKey(@NonNls String name) { return new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.getDefaultNamespace(), CacheUniversal.class.getName() + '.' + name); /* TODO make this a utility method perhaps */ }
	}
}
