package $group__.client.ui.mvvm.core.extensions;

import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.specific.ThrowableUtilities;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IUIExtension<C extends IExtensionContainer<?, ?>>
		extends IExtension<C> {
	IType<?, ?> getType();

	interface IType<V extends IUIExtension<I>, I extends IExtensionContainer<?, ?>> {
		Optional<V> get(I instance);

		ResourceLocation getKey();

		class Impl<V extends IUIExtension<I>, I extends IExtensionContainer<?, ?>>
				implements IType<V, I> {
			protected final ResourceLocation key;
			protected final BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getter;

			public Impl(ResourceLocation key, BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getter) {
				this.key = key;
				this.getter = getter;
			}

			@Override
			public Optional<V> get(I instance) { return getGetter().apply(this, instance).map(Function.identity()); }

			@Override
			public ResourceLocation getKey() { return key; }

			protected BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getGetter() { return getter; }
		}
	}

	final class RegUIExtension extends Registry<ResourceLocation, IType<?, ?>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final RegUIExtension INSTANCE = Singleton.getSingletonInstance(RegUIExtension.class, LOGGER);

		@SuppressWarnings("unused")
		protected RegUIExtension() { super(true, LOGGER); }

		public static void checkExtensionRegistered(IUIExtension<?> extension) {
			if (!INSTANCE.containsValue(extension.getType())) {
				throw ThrowableUtilities.BecauseOf.illegalArgument("Unregistered UI extension type",
						"extension.getType()", extension.getType(),
						"extension", extension);
			}
		}
	}
}
