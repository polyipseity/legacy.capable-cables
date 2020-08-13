package $group__.client.ui.mvvm.views.components.extensions;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.IUIExtensionCache;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CastUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIExtensionCache
		extends IHasGenericClass.Impl<IExtensionContainer<ResourceLocation, ?>>
		implements IUIExtensionCache {
	public static final Registry.RegistryObject<IUIExtension.IType<ResourceLocation, IUIExtensionCache, IExtensionContainer<ResourceLocation, ?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IUIExtension.IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	protected final Cache<ResourceLocation, Object> cache =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	public UIExtensionCache() {
		super(CastUtilities.castUnchecked(IExtensionContainer.class)); // COMMENT should not matter in this case
	}

	@Override
	public Cache<ResourceLocation, Object> getDelegated() { return cache; }

	@Override
	public IExtension.IType<? extends ResourceLocation, ?, ? extends IExtensionContainer<ResourceLocation, ?>> getType() { return TYPE.getValue(); }

	public enum CacheUniversal {
		;
		private static final Logger LOGGER = LogManager.getLogger();

		public static final Registry.RegistryObject<IType<IUIComponentManager<?>, IUIComponent>> MANAGER =
				RegUICache.INSTANCE.registerApply(generateKey("manager"),
						k -> new IType.Impl<>(k,
								(t, i) ->
										TYPE.getValue().get(i).flatMap(cache -> Try.call(() -> cache.getDelegated()
												.get(t.getKey(), () ->
														IUIComponent.getYoungestParentInstanceOf(i, IUIComponentManager.class)), LOGGER)
												.map(CastUtilities::castUnchecked)),
								(t, i) -> {
									IType.invalidate(i, t.getKey());
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
		public static final Registry.RegistryObject<IType<Integer, IUIComponent>> Z =
				RegUICache.INSTANCE.registerApply(generateKey("z"),
						k -> new IType.Impl<>(k,
								(t, i) -> TYPE.getValue().get(i).flatMap(cache -> Try.call(() -> cache.getDelegated().get(t.getKey(),
										() -> {
											int ret = -1;
											for (Optional<? extends IUIComponent> parent = Optional.of(i);
											     parent.isPresent();
											     parent = parent.flatMap(IUIComponent::getParent))
												++ret;
											return ret;
										}), LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> {
									IType.invalidate(i, t.getKey());
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

		private static ResourceLocation generateKey(String name) { return new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, "universal." + name); }
	}
}
