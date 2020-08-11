package $group__.client.ui.mvvm.views.components.extensions;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.extensions.IUIExtensionCache;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CastUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Optional;
import java.util.function.Function;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@OnlyIn(Dist.CLIENT)
public class UIExtensionCache<C extends IUIComponent>
		extends IHasGenericClass.Impl<C>
		implements IUIExtensionCache<C> {
	public static final Registry.RegistryObject<IUIExtension.IType<IUIExtensionCache<IUIComponent>, IUIComponent>> TYPE = RegUIExtension.INSTANCE.register(KEY, new IUIExtension.IType<IUIExtensionCache<IUIComponent>, IUIComponent>() {
		@Override
		public Optional<IUIExtensionCache<IUIComponent>> get(IUIComponent component) { return component.getExtension(KEY).map(CastUtilities::castUnchecked); }

		@Override
		public ResourceLocation getKey() { return KEY; }
	});
	protected final Cache<ResourceLocation, Object> cache =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	public UIExtensionCache(Class<C> genericClass) { super(genericClass); }

	@Override
	public IUIExtension.IType<?, ?> getType() { return TYPE.getValue(); }

	@Override
	public Cache<ResourceLocation, Object> getDelegated() { return cache; }

	@OnlyIn(Dist.CLIENT)
	public enum CacheUniversal {
		;
		public static final Registry.RegistryObject<IType<IUIComponentManager, IUIComponent>> MANAGER =
				RegUICache.INSTANCE.register(new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "manager"),
						(Function<? super ResourceLocation, ? extends IType<IUIComponentManager, IUIComponent>>)
								k -> new IType.Impl<IUIComponentManager, IUIComponent>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											invalidate(event.getComponent());
									}

									@Override
									public Optional<IUIComponentManager> get(IUIComponent component) {
										return TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> IUIComponent.getYoungestParentInstanceOf(component, IUIComponentManager.class)),
												getLogger()
										).map(CastUtilities::castUnchecked));
									}

									@Override
									public void invalidate(IUIComponent component) {
										super.invalidate(component);
										if (component instanceof IUIComponentContainer)
											((IUIComponentContainer) component).getChildrenView().forEach(this::invalidate);
									}
								});
		public static final Registry.RegistryObject<IType<Integer, IUIComponent>> Z =
				RegUICache.INSTANCE.register(new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "z"),
						(Function<? super ResourceLocation, ? extends IType<Integer, IUIComponent>>)
								k -> new IType.Impl<Integer, IUIComponent>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											invalidate(event.getComponent());
									}

									@Override
									public Optional<Integer> get(IUIComponent component) {
										return TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> {
													int ret = -1;
													for (Optional<? extends IUIComponent> parent = Optional.of(component);
													     parent.isPresent();
													     parent = parent.flatMap(IUIComponent::getParent))
														++ret;
													return ret;
												}), getLogger()).flatMap(CastUtilities::castUnchecked));
									}
								});
	}
}
