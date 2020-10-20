package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;

public interface IUIRenderer<C>
		extends IHasBinding, IHasBindingMap, IHasGenericClass<C> {
	void onRendererAdded(C container);

	void onRendererRemoved();

	/* TODO this class does nothing for now, useful for user custom theming later
		class RegRenderer<R extends IUIComponentRenderer<?>>
			extends Registry<INamespacePrefixedString, Class<? extends R>>
			implements IHasGenericClass<R> {
		public static final INamespacePrefixedString DEFAULT_KEY = new ImmutableNamespacePrefixedString("default");
		protected static final ConcurrentMap<Class<? extends IUIComponent>, RegRenderer<?>> INSTANCES =
				MapUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
		private final Class<R> variant;

		protected RegRenderer(Class<R> variant, Class<? extends R> defaultValue, Logger logger) {
			super(true, logger);
			this.variant = variant;

			data.put(DEFAULT_KEY, new RegistryObject<>(defaultValue));
		}

		public static <R extends IUIComponentRenderer<?>> RegistryObject<? extends Class<? extends R>> getDefaultFactory(RegRenderer<R> registry) {
			return registry.get(DEFAULT_KEY)
					.orElseThrow(InternalError::new);
		}

		@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "unchecked"})
		public static <C extends IUIComponent, R extends IUIComponentRenderer<? super C>> RegRenderer<R> createInstance(Class<C> clazz, Class<R> variant, Class<? extends R> defaultValue, Logger logger) {
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
		public <VL extends Class<? extends R>> RegistryObject<VL> register(INamespacePrefixedString key, VL value) {
			if (DEFAULT_KEY.equals(key))
				throw BecauseOf.illegalArgument("Default entry is not replaceable",
						"DEFAULT_KEY", DEFAULT_KEY,
						"key", key,
						"value", value);
			return super.register(key, value);
		}
	}
	 */
}
