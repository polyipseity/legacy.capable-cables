package $group__.ui.core.mvvm.views.components.rendering;

import $group__.ui.core.mvvm.binding.IHasBinding;
import $group__.ui.core.mvvm.binding.IHasBindingMap;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public interface IUIComponentRenderer<C extends IUIComponent>
		extends IHasBinding, IHasBindingMap, IHasGenericClass<C> {
	class RegRenderer<R extends IUIComponentRenderer<?>>
			extends Registry<INamespacePrefixedString, Supplier<? extends R>>
			implements IHasGenericClass<R> {
		public static final INamespacePrefixedString DEFAULT_KEY = new NamespacePrefixedString("default");
		protected static final ConcurrentMap<Class<? extends IUIComponent>, RegRenderer<?>> INSTANCES =
				MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
		protected final Class<R> variant;

		protected RegRenderer(Class<R> variant, Supplier<? extends R> defaultValue, Logger logger) {
			super(true, logger);
			this.variant = variant;

			delegated.put(DEFAULT_KEY, new RegistryObject<>(defaultValue));
		}

		public static <R extends IUIComponentRenderer<?>> RegistryObject<? extends Supplier<? extends R>> getDefault(RegRenderer<R> registry) {
			return registry.get(DEFAULT_KEY)
					.orElseThrow(InternalError::new);
		}

		@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "unchecked"})
		public static <C extends IUIComponent, R extends IUIComponentRenderer<? super C>> RegRenderer<R> createInstance(Class<C> clazz, Class<R> variant, Supplier<? extends R> defaultValue, Logger logger) {
			RegRenderer<?> ret;
			synchronized (clazz) {
				ret = INSTANCES.computeIfAbsent(clazz, k ->
						new RegRenderer<>(variant, defaultValue, logger));
			}
			if (!ret.getGenericClass().equals(variant))
				throw BecauseOf.illegalArgument("Wrong 'IUIComponentRenderer' variant",
						"variant", variant,
						"ret.getGenericClass()", ret.getGenericClass(),
						"clazz", clazz);
			return (RegRenderer<R>) ret; // COMMENT checked
		}

		@Override
		public Class<R> getGenericClass() { return variant; }

		@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "unchecked"})
		public static <C extends IUIComponent, R extends IUIComponentRenderer<? super C>> Optional<? extends RegRenderer<? extends R>> getInstance(Class<C> clazz, Class<R> variantUpperBound) {
			@Nullable RegRenderer<?> ret;
			synchronized (clazz) {
				ret = INSTANCES.get(clazz);
			}
			if (ret != null && !variantUpperBound.isAssignableFrom(ret.getGenericClass()))
				throw BecauseOf.illegalArgument("Wrong 'IUIComponentRenderer' variant upper bound",
						"variantUpperBound", variantUpperBound,
						"ret.getGenericClass()", ret.getGenericClass(),
						"clazz", clazz);
			return Optional.ofNullable((RegRenderer<? extends R>) ret); // COMMENT checked
		}

		@Override
		public <VL extends Supplier<? extends R>> RegistryObject<VL> register(INamespacePrefixedString key, VL value) {
			if (DEFAULT_KEY.equals(key))
				throw BecauseOf.illegalArgument("Default entry is not replaceable",
						"DEFAULT_KEY", DEFAULT_KEY,
						"key", key,
						"value", value);
			return super.register(key, value);
		}
	}
}
