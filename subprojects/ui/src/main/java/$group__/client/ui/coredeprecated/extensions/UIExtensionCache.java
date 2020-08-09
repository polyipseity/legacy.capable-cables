package $group__.client.ui.coredeprecated.extensions;

import $group__.client.ui.coredeprecated.IUIExtension;
import $group__.client.ui.coredeprecated.events.EventUIComponentHierarchyChanged;
import $group__.client.ui.mvvm.views.domlike.UIContainerDOMLikeComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentManagerDOMLike;
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
	public static final Registry.RegistryObject<IUIExtension.IType<UIExtensionCache, IUIComponentDOMLike>> TYPE = IUIExtension.Reg.INSTANCE.register(KEY, new IUIExtension.IType<UIExtensionCache, IUIComponentDOMLike>() {
		@Override
		public Optional<UIExtensionCache> get(IUIComponentDOMLike component) { return component.getExtension(KEY).map(CastUtilities::castUnchecked); }

		@Override
		public ResourceLocation getKey() { return KEY; }
	});
	protected final Cache<ResourceLocation, Object> delegated =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	@Override
	public void onExtensionAdd(IUIComponentDOMLike container) {}

	@Override
	public void onExtensionRemove() {}

	@Override
	public IUIExtension.IType<?, ?> getType() { return TYPE.getValue(); }

	public Cache<ResourceLocation, Object> getDelegated() { return delegated; }

	@OnlyIn(Dist.CLIENT)
	public enum CacheUniversal {
		;
		public static final Registry.RegistryObject<UIExtensionCache.IType<IUIComponentManagerDOMLike, IUIComponentDOMLike>> MANAGER =
				Reg.INSTANCE.register(new ResourceLocation("manager"),
						(Function<? super ResourceLocation, ? extends IType<IUIComponentManagerDOMLike, IUIComponentDOMLike>>)
								k -> new IType.Impl<IUIComponentManagerDOMLike, IUIComponentDOMLike>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											invalidate(event.getComponent());
									}

									@Override
									public Optional<IUIComponentManagerDOMLike> get(IUIComponentDOMLike component) {
										return TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> IUIComponentDOMLike.getYoungestParentInstanceOf(component, IUIComponentManagerDOMLike.class)),
												getLogger()
										).map(CastUtilities::castUnchecked));
									}

									@Override
									public void invalidate(IUIComponentDOMLike component) {
										super.invalidate(component);
										if (component instanceof UIContainerDOMLikeComponentDOMLike)
											((UIContainerDOMLikeComponentDOMLike) component).getChildrenView().forEach(this::invalidate);
									}
								});
		public static final Registry.RegistryObject<UIExtensionCache.IType<Integer, IUIComponentDOMLike>> Z =
				Reg.INSTANCE.register(new ResourceLocation("z"),
						(Function<? super ResourceLocation, ? extends IType<Integer, IUIComponentDOMLike>>)
								k -> new IType.Impl<Integer, IUIComponentDOMLike>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											invalidate(event.getComponent());
									}

									@Override
									public Optional<Integer> get(IUIComponentDOMLike component) {
										return TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> {
													int ret = -1;
													for (Optional<? extends IUIComponentDOMLike> parent = Optional.of(component);
													     parent.isPresent();
													     parent = parent.flatMap(IUIComponentDOMLike::getParent))
														++ret;
													return ret;
												}), getLogger()).flatMap(CastUtilities::castUnchecked));
									}
								});
	}

	@OnlyIn(Dist.CLIENT)
	public interface IType<T, C extends IUIComponentDOMLike> {
		Optional<T> get(C component);

		@OverridingMethodsMustInvokeSuper
		default void invalidate(C component) { TYPE.getValue().get(component).ifPresent(c -> c.getDelegated().invalidate(getKey())); }

		ResourceLocation getKey();

		@OnlyIn(Dist.CLIENT)
		abstract class Impl<T, C extends IUIComponentDOMLike> implements IType<T, C> {
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
