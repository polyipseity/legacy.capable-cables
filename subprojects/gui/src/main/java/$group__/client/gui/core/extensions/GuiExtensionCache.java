package $group__.client.gui.core.extensions;

import $group__.client.gui.core.IGuiComponent;
import $group__.client.gui.core.IGuiExtension;
import $group__.client.gui.core.events.EventGuiComponentHierarchyChanged;
import $group__.client.gui.mvvm.views.components.GuiContainer;
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
public class GuiExtensionCache implements IGuiExtension {
	public static final ResourceLocation KEY = new ResourceLocation("cache");
	public static final Registry.RegistryObject<IGuiExtension.IType<GuiExtensionCache, IGuiComponent<?, ?, ?>>> TYPE = IGuiExtension.Reg.INSTANCE.register(KEY, new IGuiExtension.IType<GuiExtensionCache, IGuiComponent<?, ?, ?>>() {
		@Override
		public Optional<GuiExtensionCache> get(IGuiComponent<?, ?, ?> component) { return component.getExtension(KEY).map(CastUtilities::castUnchecked); }

		@Override
		public ResourceLocation getKey() { return KEY; }
	});
	protected final Cache<ResourceLocation, Object> delegated =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	@Override
	public void onExtensionAdd(IGuiComponent<?, ?, ?> container) {}

	@Override
	public void onExtensionRemove() {}

	@Override
	public IGuiExtension.IType<?, ?> getType() { return TYPE.getValue(); }

	public Cache<ResourceLocation, Object> getDelegated() { return delegated; }

	@OnlyIn(Dist.CLIENT)
	public enum CacheUniversal {
		;
		public static final Registry.RegistryObject<GuiExtensionCache.IType<IGuiComponent.IManager<?, ?, ?, ?, ?>, IGuiComponent<?, ?, ?>>> MANAGER =
				Reg.INSTANCE.register(new ResourceLocation("manager"),
						(Function<? super ResourceLocation, ? extends IType<IGuiComponent.IManager<?, ?, ?, ?, ?>, IGuiComponent<?, ?, ?>>>)
								k -> new IType.Impl<IGuiComponent.IManager<?, ?, ?, ?, ?>, IGuiComponent<?, ?, ?>>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventGuiComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											invalidate(event.getComponent());
									}

									@Override
									public Optional<IGuiComponent.IManager<?, ?, ?, ?, ?>> get(IGuiComponent<?, ?, ?> component) {
										return TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> IGuiComponent.getYoungestParentInstanceOf(component, IGuiComponent.IManager.class)),
												getLogger()
										).map(CastUtilities::castUnchecked));
									}

									@Override
									public void invalidate(IGuiComponent<?, ?, ?> component) {
										super.invalidate(component);
										if (component instanceof GuiContainer)
											((GuiContainer<?, ?, ?, ?>) component).getChildrenView().forEach(this::invalidate);
									}
								});
		public static final Registry.RegistryObject<GuiExtensionCache.IType<Integer, IGuiComponent<?, ?, ?>>> Z =
				Reg.INSTANCE.register(new ResourceLocation("z"),
						(Function<? super ResourceLocation, ? extends IType<Integer, IGuiComponent<?, ?, ?>>>)
								k -> new IType.Impl<Integer, IGuiComponent<?, ?, ?>>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onParentChanged(EventGuiComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											invalidate(event.getComponent());
									}

									@Override
									public Optional<Integer> get(IGuiComponent<?, ?, ?> component) {
										return TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> {
													int ret = -1;
													for (Optional<? extends IGuiComponent<?, ?, ?>> parent = Optional.of(component);
													     parent.isPresent();
													     parent = parent.flatMap(IGuiComponent::getParent))
														++ret;
													return ret;
												}), getLogger()).flatMap(CastUtilities::castUnchecked));
									}
								});
	}

	@OnlyIn(Dist.CLIENT)
	public interface IType<T, C extends IGuiComponent<?, ?, ?>> {
		Optional<T> get(C component);

		@OverridingMethodsMustInvokeSuper
		default void invalidate(C component) { TYPE.getValue().get(component).ifPresent(c -> c.getDelegated().invalidate(getKey())); }

		ResourceLocation getKey();

		@OnlyIn(Dist.CLIENT)
		abstract class Impl<T, C extends IGuiComponent<?, ?, ?>> implements IType<T, C> {
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
