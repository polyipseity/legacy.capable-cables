package $group__.client.gui.structures;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.utilities.Casts;
import $group__.utilities.Concurrency;
import $group__.utilities.specific.Maps;
import $group__.utilities.specific.ThrowableUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.ThrowableCatcher;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;

import static $group__.utilities.Capacities.INITIAL_CAPACITY_2;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiCache {
	public final Cache<ResourceLocation, Object> delegated =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_2)
					.expireAfterAccess(Maps.CACHE_EXPIRATION_ACCESS_DURATION, Maps.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(Concurrency.SINGLE_THREAD_THREAD_COUNT).build();

	@OnlyIn(CLIENT)
	public static abstract class CacheKey<T> {
		protected static final List<CacheKey<?>> KEYS = new ArrayList<>(INITIAL_CAPACITY_2);
		private static final Logger LOGGER = LogManager.getLogger();
		public static final CacheKey<GuiRoot<?>> ROOT = new CacheKey<GuiRoot<?>>(new ResourceLocation(null, "root")) {
			@Override
			public void initialize(GuiComponent component) {
				component.listeners.added.add(c -> invalidate(component));
				component.listeners.removed.add(c -> invalidate(component));
			}

			@Override
			public GuiRoot<?> get(GuiComponent component) {
				return ThrowableUtilities.Try.call(() -> component.cache.delegated.get(key, () -> component.getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected)), LOGGER).flatMap(Casts::<GuiRoot<?>>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}

			@Override
			public void invalidate(GuiComponent component) {
				super.invalidate(component);
				if (component instanceof GuiContainer)
					((GuiContainer) component).getChildrenView().forEach(this::invalidate);
			}
		};
		public static final CacheKey<IGuiLifecycleHandler> LIFECYCLE_HANDLER = new CacheKey<IGuiLifecycleHandler>(new ResourceLocation(null, "lifecycle_handler")) {
			@Override
			public void initialize(GuiComponent component) {
				component.listeners.added.add(c -> invalidate(component));
				component.listeners.removed.add(c -> invalidate(component));
			}

			@Override
			public IGuiLifecycleHandler get(GuiComponent component) {
				return ThrowableUtilities.Try.call(() -> component.cache.delegated.get(key, () -> component.getNearestParentThatIs(IGuiLifecycleHandler.class).orElseThrow(BecauseOf::unexpected)), LOGGER).flatMap(Casts::<IGuiLifecycleHandler>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}

			@Override
			public void invalidate(GuiComponent component) {
				super.invalidate(component);
				if (component instanceof GuiContainer)
					((GuiContainer) component).getChildrenView().forEach(this::invalidate);
			}
		};
		public static final CacheKey<IGuiReshapeHandler> RESHAPE_HANDLER = new CacheKey<IGuiReshapeHandler>(new ResourceLocation(null, "reshape_handler")) {
			@Override
			public void initialize(GuiComponent component) {
				component.listeners.added.add(c -> invalidate(component));
				component.listeners.removed.add(c -> invalidate(component));
			}

			@Override
			public IGuiReshapeHandler get(GuiComponent component) {
				return ThrowableUtilities.Try.call(() -> component.cache.delegated.get(key, () -> component.getNearestParentThatIs(IGuiReshapeHandler.class).orElseThrow(BecauseOf::unexpected)), LOGGER).flatMap(Casts::<IGuiReshapeHandler>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}

			@Override
			public void invalidate(GuiComponent component) {
				super.invalidate(component);
				if (component instanceof GuiContainer)
					((GuiContainer) component).getChildrenView().forEach(this::invalidate);
			}
		};
		public static final CacheKey<Double> SCALE_FACTOR = new CacheKey<Double>(new ResourceLocation(null, "scale_factor")) {
			@Override
			public void initialize(GuiComponent component) { component.listeners.initialize.add((h, i) -> invalidate(component)); }

			@Override
			public Double get(GuiComponent component) {
				return ThrowableUtilities.Try.call(() -> component.cache.delegated.get(key, () -> ROOT.get(component).getScreen().getMinecraft().getMainWindow().getGuiScaleFactor()), LOGGER).flatMap(Casts::<Double>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}
		};
		public final ResourceLocation key;

		public CacheKey(ResourceLocation key) {
			this.key = key;
			KEYS.add(this);
		}

		public static ImmutableList<CacheKey<?>> getKeys() {
			return ImmutableList.copyOf(KEYS);
		}

		public static void initializeAll(GuiComponent component) {
			for (CacheKey<?> key : getKeys())
				key.initialize(component);
		}

		public abstract void initialize(GuiComponent component);

		public abstract T get(GuiComponent component);

		@OverridingMethodsMustInvokeSuper
		public void invalidate(GuiComponent component) { component.cache.delegated.invalidate(key); }
	}
}
