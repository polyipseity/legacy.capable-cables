package $group__.ui.mvvm.views.components.extensions.caches;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUIExtensionCache;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CastUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
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
																.orElse(null);
														cache.getDelegated().put(t.getKey(),
																new WeakReference<>(ret));
													}
													return ret;
												}, LOGGER)),
								(t, i) -> {
									IUIExtensionCache.IType.invalidateImpl(i, t.getKey());
									CastUtilities.castChecked(IUIComponentContainer.class, i)
											.ifPresent(ic -> IUIExtensionCache.IType.invalidateChildrenImpl(ic, t));
								},
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage().isPost())
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
									IUIExtensionCache.IType.invalidateImpl(i, t.getKey());
									CastUtilities.castChecked(IUIComponentContainer.class, i)
											.ifPresent(ic -> IUIExtensionCache.IType.invalidateChildrenImpl(ic, t));
								},
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage().isPost())
											t.invalidate(event.getComponent());
									}
								})));

		private static INamespacePrefixedString generateKey(String name) { return new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, CacheUniversal.class.getName() + '.' + name); }
	}
}
