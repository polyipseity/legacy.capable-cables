package $group__.client.ui.mvvm.views.components.extensions.caches;

import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.IUIExtensionCache;
import $group__.client.ui.mvvm.core.views.components.parsers.UIExtensionConstructor;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CastUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Optional;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIExtensionCache
		extends ExtensionContainerAware<ResourceLocation, IExtensionContainer<ResourceLocation, ?>, IExtensionContainer<ResourceLocation, ?>>
		implements IUIExtensionCache {
	protected final Cache<ResourceLocation, Object> cache =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	@UIExtensionConstructor(type = UIExtensionConstructor.ConstructorType.NO_ARGS)
	public UIExtensionCache() {
		super(CastUtilities.castUnchecked(IExtensionContainer.class), CastUtilities.castUnchecked(IExtensionContainer.class)); // COMMENT should not matter in this case
	}

	@Override
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getDelegated().invalidateAll();
	}

	@Override
	public Cache<ResourceLocation, Object> getDelegated() { return cache; }

	@Override
	public IExtension.IType<? extends ResourceLocation, ?, ? extends IExtensionContainer<ResourceLocation, ?>> getType() { return IUIExtensionCache.TYPE.getValue(); }

	public enum CacheUniversal {
		;
		private static final Logger LOGGER = LogManager.getLogger();

		public static final Registry.RegistryObject<IUIExtensionCache.IType<IUIComponentManager<?>, IUIComponent>> MANAGER =
				RegUICache.INSTANCE.registerApply(generateKey("manager"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) ->
										IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache ->
												Try.call(() -> {
													@SuppressWarnings("unchecked") @Nullable WeakReference<IUIComponentManager<?>> cv =
															(WeakReference<IUIComponentManager<?>>) cache.getDelegated().getIfPresent(t.getKey());
													@Nullable IUIComponentManager<?> ret;
													if (cv == null || (ret = cv.get()) == null) {
														ret = IUIComponent.getYoungestParentInstanceOf(i, IUIComponentManager.class)
																.orElseThrow(IllegalStateException::new);
														cache.getDelegated().put(t.getKey(),
																new WeakReference<>(ret));
													}
													return ret;
												}, LOGGER)
														.map(CastUtilities::castUnchecked)),
								(t, i) -> {
									IUIExtensionCache.IType.invalidate(i, t.getKey());
									if (i instanceof IUIComponentContainer)
										((IUIComponentContainer) i).getChildrenView().forEach(t::invalidate);
								},
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											t.invalidate(event.getComponent());
									}
								})));
		public static final Registry.RegistryObject<IUIExtensionCache.IType<Integer, IUIComponent>> Z =
				RegUICache.INSTANCE.registerApply(generateKey("z"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> Try.call(() -> cache.getDelegated().get(t.getKey(),
										() -> {
											int ret = -1;
											for (Optional<? extends IUIComponent> parent = Optional.of(i);
											     parent.isPresent();
											     parent = parent.flatMap(IUIComponent::getParent))
												++ret;
											return ret;
										}), LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> {
									IUIExtensionCache.IType.invalidate(i, t.getKey());
									if (i instanceof IUIComponentContainer)
										((IUIComponentContainer) i).getChildrenView().forEach(t::invalidate);
								},
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											t.invalidate(event.getComponent());
									}
								})));

		private static ResourceLocation generateKey(String name) { return new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, "universal." + name); }
	}
}
