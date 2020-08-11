package $group__.client.ui.mvvm.core.views.components.extensions;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.views.components.extensions.UIExtensionCache;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import com.google.common.cache.Cache;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;

public interface IUIExtensionCache<C extends IUIComponent>
		extends IUIExtension<C> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "cache");

	Cache<ResourceLocation, Object> getDelegated();

	@OnlyIn(Dist.CLIENT)
	interface IType<T, C extends IUIComponent> {
		Optional<T> get(C component);

		@OverridingMethodsMustInvokeSuper
		default void invalidate(C component) { UIExtensionCache.TYPE.getValue().get(component).ifPresent(c -> c.getDelegated().invalidate(getKey())); }

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
	final class RegUICache extends Registry<ResourceLocation, IType<?, ?>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final RegUICache INSTANCE = Singleton.getSingletonInstance(RegUICache.class, LOGGER);

		@SuppressWarnings("unused")
		protected RegUICache() { super(true, LOGGER); }
	}
}
