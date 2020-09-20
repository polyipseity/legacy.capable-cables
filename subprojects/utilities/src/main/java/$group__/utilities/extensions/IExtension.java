package $group__.utilities.extensions;

import $group__.utilities.interfaces.IHasGenericClass;
import org.jetbrains.annotations.NonNls;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IExtension<K, C extends IExtensionContainer<? super K>> extends IHasGenericClass<C> {
	@NonNls String AREA_UI = "ui"; // TODO move

	default void onExtensionAdded(C container) {}

	default void onExtensionRemoved() {}

	IType<? extends K, ?, ? extends C> getType();

	interface IType<K, V, I extends IExtensionContainer<?>> {
		Optional<? extends V> get(I instance);

		K getKey();

		class Impl<K, V, I extends IExtensionContainer<?>>
				implements IType<K, V, I> {
			protected final K key;
			protected final BiFunction<? super IType<K, V, I>, ? super I, ? extends Optional<? extends V>> getter;

			public Impl(K key, BiFunction<? super IType<K, V, I>, ? super I, ? extends Optional<? extends V>> getter) {
				this.key = key;
				this.getter = getter;
			}

			@Override
			public Optional<? extends V> get(I instance) { return getGetter().apply(this, instance).map(Function.identity()); }

			@Override
			public K getKey() { return key; }

			protected BiFunction<? super IType<K, V, I>, ? super I, ? extends Optional<? extends V>> getGetter() { return getter; }
		}
	}

}
