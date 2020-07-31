package $group__.client.gui.structures;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.utilities.CastUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.ThrowableCatcher;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_LARGE;
import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiCache {
	public final Cache<String, Object> delegated =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(MapUtilities.CACHE_EXPIRATION_ACCESS_DURATION, MapUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	@OnlyIn(CLIENT)
	public static abstract class CacheKey<C extends GuiComponent<?>, T> {
		public static final CacheKey<GuiComponent<?>, GuiRoot<?, ?>> ROOT = new CacheKey<GuiComponent<?>, GuiRoot<?, ?>>("root") {
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
				return ThrowableUtilities.Try.call(() -> component.data.cache.delegated.get(key, () -> component.getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected)), component.data.logger.get()).map(CastUtilities::<GuiRoot<?, ?>>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}
		};
		public static final CacheKey<GuiComponent<?>, IGuiLifecycleHandler> LIFECYCLE_HANDLER = new CacheKey<GuiComponent<?>, IGuiLifecycleHandler>("lifecycle_handler") {
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
				return ThrowableUtilities.Try.call(() -> component.data.cache.delegated.get(key, () -> component.getNearestParentThatIs(IGuiLifecycleHandler.class).orElseThrow(BecauseOf::unexpected)), component.data.logger.get()).map(CastUtilities::<IGuiLifecycleHandler>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}
		};
		public static final CacheKey<GuiComponent<?>, IGuiReshapeHandler> RESHAPE_HANDLER = new CacheKey<GuiComponent<?>, IGuiReshapeHandler>("reshape_handler") {
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
				return ThrowableUtilities.Try.call(() -> component.data.cache.delegated.get(key, () -> component.getNearestParentThatIs(IGuiReshapeHandler.class).orElseThrow(BecauseOf::unexpected)), component.data.logger.get()).map(CastUtilities::<IGuiReshapeHandler>castUnchecked).orElseThrow(ThrowableCatcher::rethrow);
			}
		};
		public static final CacheKey<GuiComponent<?>, Integer> Z = new CacheKey<GuiComponent<?>, Integer>("z") {
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

		public final String key;
		protected final Set<C> initialized = new HashSet<>(INITIAL_CAPACITY_LARGE);

		public CacheKey(String key) { this.key = NamespaceUtilities.getNamespacePrefixedString(":", DynamicUtilities.getCallerClass().getName(), key); }

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
