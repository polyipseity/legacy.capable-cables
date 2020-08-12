package $group__.client.ui.mvvm.core.views.components.extensions.caches;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.views.components.extensions.UIExtensionCache;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import com.google.common.cache.Cache;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Cleaner;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IUIExtensionCache
		extends IUIExtension<ResourceLocation, IExtensionContainer<ResourceLocation, ?>> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT, AREA_UI + ".cache");

	Cache<ResourceLocation, Object> getDelegated();

	interface IType<V, I extends IExtensionContainer<ResourceLocation, ?>> {
		Optional<V> get(I instance);

		default void invalidate(I instance) { invalidate(instance, getKey()); }

		static void invalidate(IExtensionContainer<ResourceLocation, ?> instance, ResourceLocation key) {
			UIExtensionCache.TYPE.getValue().get(instance).ifPresent(c -> c.getDelegated().invalidate(key));
		}

		ResourceLocation getKey();

		class Impl<V, I extends IExtensionContainer<ResourceLocation, ?>> implements IType<V, I> {
			protected final ResourceLocation key;
			protected final BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getter;
			protected final BiConsumer<? super IType<V, I>, ? super I> invalidator;
			protected final Object eventListener;

			public Impl(ResourceLocation key,
			            BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getter,
			            BiConsumer<? super IType<V, I>, ? super I> invalidator,
			            Function<? super IType<V, I>, ?> eventListenerFunction) {
				this.key = key;
				this.getter = getter;
				this.invalidator = invalidator;
				this.eventListener = eventListenerFunction.apply(this);
				EventBusEntryPoint.INSTANCE.register(getEventListener());
				{
					Object eventListenerRef = getEventListener();
					Cleaner.create(this, () -> EventBusEntryPoint.INSTANCE.unregister(eventListenerRef));
				}
			}

			protected final Object getEventListener() { return eventListener; }

			@Override
			public Optional<V> get(I instance) { return getGetter().apply(this, instance).map(Function.identity()); }

			@Override
			public void invalidate(I instance) { getInvalidator().accept(this, instance); }

			@Override
			public ResourceLocation getKey() { return key; }

			protected BiConsumer<? super IType<V, I>, ? super I> getInvalidator() { return invalidator; }

			protected BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getGetter() { return getter; }
		}
	}

	final class RegUICache extends Registry<ResourceLocation, IType<?, ?>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final RegUICache INSTANCE = Singleton.getSingletonInstance(RegUICache.class, LOGGER);

		@SuppressWarnings("unused")
		protected RegUICache() { super(true, LOGGER); }
	}
}
