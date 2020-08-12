package $group__.utilities.extensions;

import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.ThrowableUtilities;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IExtension<K, C extends IExtensionContainer<? super K, ?>> extends IHasGenericClass<C> {
	String AREA_UI = "ui";

	default void onExtensionAdded(C container) {}

	default void onExtensionRemoved() {}

	IType<? extends K, ?, ? extends C> getType();

	interface IType<K, V, I extends IExtensionContainer<?, ?>> {
		Optional<V> get(I instance);

		K getKey();

		class Impl<K, V, I extends IExtensionContainer<?, ?>>
				implements IType<K, V, I> {
			protected final K key;
			protected final BiFunction<? super IType<K, V, I>, ? super I, ? extends Optional<? extends V>> getter;

			public Impl(K key, BiFunction<? super IType<K, V, I>, ? super I, ? extends Optional<? extends V>> getter) {
				this.key = key;
				this.getter = getter;
			}

			@Override
			public Optional<V> get(I instance) { return getGetter().apply(this, instance).map(Function.identity()); }

			@Override
			public K getKey() { return key; }

			protected BiFunction<? super IType<K, V, I>, ? super I, ? extends Optional<? extends V>> getGetter() { return getter; }
		}
	}

	final class RegExtension extends Registry<ResourceLocation, IType<? extends ResourceLocation, ?, ?>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final RegExtension INSTANCE = Singleton.getSingletonInstance(RegExtension.class, LOGGER);

		@SuppressWarnings("unused")
		protected RegExtension() { super(true, LOGGER); }

		public static void checkExtensionRegistered(IExtension<? extends ResourceLocation, ?> extension) {
			if (!INSTANCE.containsValue(extension.getType())) {
				throw ThrowableUtilities.BecauseOf.illegalArgument("Unregistered UI extension type",
						"extension.getType()", extension.getType(),
						"extension", extension);
			}
		}
	}
}
