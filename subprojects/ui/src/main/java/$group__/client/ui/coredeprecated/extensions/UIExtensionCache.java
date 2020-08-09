package $group__.client.ui.coredeprecated.extensions;

import $group__.client.ui.coredeprecated.IUIExtension;
import $group__.client.ui.coredeprecated.events.EventUIComponentHierarchyChanged;
import $group__.client.ui.mvvm.views.UIContainerComponent;
import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.client.ui.mvvm.views.components.IUIComponentManager;
import $group__.utilities.CastUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Function;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@OnlyIn(Dist.CLIENT)
public class UIExtensionCache implements IUIExtension {
	public static final ResourceLocation KEY = new ResourceLocation("cache");
	public static final Registry.RegistryObject<IUIExtension.IType<UIExtensionCache, IUIComponent>> TYPE = IUIExtension.Reg.INSTANCE.register(KEY, new IUIExtension.IType<UIExtensionCache, IUIComponent>() {
		@Override
		public Optional<UIExtensionCache> get(IUIComponent component) { return component.getExtension(KEY).map(CastUtilities::castUnchecked); }

		@Override
		public ResourceLocation getKey() { return KEY; }
	});
	protected final Cache<ResourceLocation, Object> delegated =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	@Override
	public void onExtensionAdd(IUIComponent container) {}

	@Override
	public void onExtensionRemove() {}

	@Override
	public IUIExtension.IType<?, ?> getType() { return TYPE.getValue(); }

	public Cache<ResourceLocation, Object> getDelegated() { return delegated; }

	@OnlyIn(Dist.CLIENT)
	public enum CacheUniversal {
		;
		public static final Registry.RegistryObject<UIExtensionCache.IType<IUIComponentManager, IUIComponent>> MANAGER =
				Reg.INSTANCE.register(new ResourceLocation("manager"),
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
										if (component instanceof UIContainerComponent)
											((UIContainerComponent) component).getChildrenView().forEach(this::invalidate);
									}
								});
		public static final Registry.RegistryObject<UIExtensionCache.IType<Integer, IUIComponent>> Z =
				Reg.INSTANCE.register(new ResourceLocation("z"),
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

	@OnlyIn(Dist.CLIENT)
	public interface IType<T, C extends IUIComponent> {
		Optional<T> get(C component);

		@OverridingMethodsMustInvokeSuper
		default void invalidate(C component) { TYPE.getValue().get(component).ifPresent(c -> c.getDelegated().invalidate(getKey())); }

		ResourceLocation getKey();

		@OnlyIn(Dist.CLIENT)
		abstract class Impl<T, C extends IUIComponent> implements IType<T, C> {
			protected final ResourceLocation key;
			protected final Logger logger = LogManager.getLogger();

			public Impl(ResourceLocation key) { this.key = key; }

			@Override
			public ResourceLocation getKey() { return key; }

			public Logger getLogger() { return logger; }
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static final class Reg extends Registry<ResourceLocation, IType<?, ?>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final Reg INSTANCE = Singleton.getSingletonInstance(Reg.class, LOGGER);

		@SuppressWarnings("unused")
		protected Reg() { super(false, LOGGER); }
	}
}
