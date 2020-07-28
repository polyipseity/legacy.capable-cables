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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static $group__.utilities.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.Capacities.INITIAL_CAPACITY_3;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiCache {
	public final Cache<ResourceLocation, Object> delegated =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_2)
					.expireAfterAccess(Maps.CACHE_EXPIRATION_ACCESS_DURATION, Maps.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(Concurrency.SINGLE_THREAD_THREAD_COUNT).build();

	@OnlyIn(CLIENT)
	public static abstract class CacheKey<C extends GuiComponent<?>, T> {
		public static final CacheKey<GuiComponent<?>, GuiRoot<?, ?>> ROOT = new CacheKey<GuiComponent<?>, GuiRoot<?, ?>>(getKey("root")) {
			@Override
			public void invalidate(GuiComponent<?> component) {
				super.invalidate(component);
				if (component instanceof GuiContainer)
					((GuiContainer<?>) component).getChildrenView().forEach(this::invalidate);
			}

			@Override
			public void initialize(GuiComponent<?> component) {
				super.initialize(component);
				component.data.events.cAdded.add(par -> invalidate(component));
				component.data.events.cRemoved.add(par -> invalidate(component));
			}

			@Override
			public GuiRoot<?, ?> get0(GuiComponent<?> component) {
				return ThrowableUtilities.Try.call(() -> component.data.cache.delegated.get(key, () -> component.getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected)), component.data.logger.get()).flatMap(Casts::<GuiRoot<?, ?>>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}
		};
		public static final CacheKey<GuiComponent<?>, IGuiLifecycleHandler> LIFECYCLE_HANDLER = new CacheKey<GuiComponent<?>, IGuiLifecycleHandler>(getKey("lifecycle_handler")) {
			@Override
			public void invalidate(GuiComponent<?> component) {
				super.invalidate(component);
				if (component instanceof GuiContainer)
					((GuiContainer<?>) component).getChildrenView().forEach(this::invalidate);
			}

			@Override
			public void initialize(GuiComponent<?> component) {
				super.initialize(component);
				component.data.events.cAdded.add(par -> invalidate(component));
				component.data.events.cRemoved.add(par -> invalidate(component));
			}

			@Override
			public IGuiLifecycleHandler get0(GuiComponent<?> component) {
				return ThrowableUtilities.Try.call(() -> component.data.cache.delegated.get(key, () -> component.getNearestParentThatIs(IGuiLifecycleHandler.class).orElseThrow(BecauseOf::unexpected)), component.data.logger.get()).flatMap(Casts::<IGuiLifecycleHandler>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}
		};
		public static final CacheKey<GuiComponent<?>, IGuiReshapeHandler> RESHAPE_HANDLER = new CacheKey<GuiComponent<?>, IGuiReshapeHandler>(getKey("reshape_handler")) {
			@Override
			public void invalidate(GuiComponent<?> component) {
				super.invalidate(component);
				if (component instanceof GuiContainer)
					((GuiContainer<?>) component).getChildrenView().forEach(this::invalidate);
			}

			@Override
			public void initialize(GuiComponent<?> component) {
				super.initialize(component);
				component.data.events.cAdded.add(par -> invalidate(component));
				component.data.events.cRemoved.add(par -> invalidate(component));
			}

			@Override
			public IGuiReshapeHandler get0(GuiComponent<?> component) {
				return ThrowableUtilities.Try.call(() -> component.data.cache.delegated.get(key, () -> component.getNearestParentThatIs(IGuiReshapeHandler.class).orElseThrow(BecauseOf::unexpected)), component.data.logger.get()).flatMap(Casts::<IGuiReshapeHandler>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}
		};
		public static final CacheKey<GuiComponent<?>, Integer> Z = new CacheKey<GuiComponent<?>, Integer>(getKey("z")) {
			@Override
			protected void initialize(GuiComponent<?> component) {
				super.initialize(component);
				component.data.events.cAdded.add(par -> invalidate(par.component));
				component.data.events.cRemoved.add(par -> invalidate(par.component));
			}

			@Override
			protected Integer get0(GuiComponent<?> component) {
				int ret = -1;
				for (Optional<? extends GuiComponent<?>> parent = Optional.of(component);
				     parent.isPresent();
				     parent = parent.flatMap(GuiComponent::getParent))
					++ret;
				return ret;
			}
		};

		public final ResourceLocation key;
		protected final Set<C> initialized = new HashSet<>(INITIAL_CAPACITY_3);

		public CacheKey(ResourceLocation key) { this.key = key; }

		private static ResourceLocation getKey(String path) { return new ResourceLocation(GuiCache.class.getSimpleName().toLowerCase(Locale.ROOT), path); }

		public T get(C component) {
			if (initialized.add(component)) initialize(component);
			return get0(component);
		}

		@OverridingMethodsMustInvokeSuper
		protected void initialize(C component) { component.data.events.cDestroyed.add(par -> initialized.remove(component)); }

		protected abstract T get0(C component);

		@OverridingMethodsMustInvokeSuper
		public void invalidate(C component) { component.data.cache.delegated.invalidate(key); }
	}
}
