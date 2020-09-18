package $group__.ui.core.parsers.adapters;

import $group__.ui.UIConfiguration;
import $group__.utilities.CastUtilities;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.functions.IDuplexFunction;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.StandardDuplexFunctionRegistry;
import jakarta.xml.bind.JAXBElement;

import java.util.Optional;
import java.util.function.Function;

public enum JAXBAdapterRegistries {
	;

	public static <L> IDuplexFunction<L, ?> getAdapter(L jaxbObj) {
		return findAdapter(jaxbObj)
				.orElseThrow(() ->
						ThrowableUtilities.BecauseOf.illegalArgument("Cannot find JAXB adapter",
								"jaxbObj.getClass()", jaxbObj.getClass(),
								"jaxbObj", jaxbObj));
	}

	@SuppressWarnings("unchecked")
	public static <L> Optional<? extends IDuplexFunction<L, ?>> findAdapter(L jaxbObj) {
		if (jaxbObj instanceof JAXBElement)
			return (Optional<? extends IDuplexFunction<L, ?>>) // COMMENT should be safe
					Element.INSTANCE.getSafe(((JAXBElement<?>) jaxbObj).getDeclaredType())
							.map(Registry.RegistryObject::getValue);
		return (Optional<? extends IDuplexFunction<L, ?>>) // COMMENT should be safe
				Object.INSTANCE.getSafe(jaxbObj.getClass())
						.map(Registry.RegistryObject::getValue);
	}

	public static final class Object
			extends StandardDuplexFunctionRegistry {
		public static final Object INSTANCE = new Object();

		protected Object() {
			super(true, UIConfiguration.INSTANCE.getLogger());
			PreconditionUtilities.requireRunOnceOnly(UIConfiguration.INSTANCE.getLogger());
		}
	}

	public static final class Element
			extends Registry<Class<?>, IDuplexFunction<JAXBElement<?>, ?>> {
		public static final Element INSTANCE = new Element();

		protected Element() {
			super(true, UIConfiguration.INSTANCE.getLogger());
			PreconditionUtilities.requireRunOnceOnly(UIConfiguration.INSTANCE.getLogger());
		}

		public <L, VL extends IDuplexFunction<JAXBElement<L>, ?>> RegistryObject<VL> registerSafe(Class<L> key, VL value) {
			return CastUtilities.castUnchecked(register(key, CastUtilities.castUnchecked(value))); // COMMENT should be safe
		}

		public <L, VL extends IDuplexFunction<JAXBElement<L>, ?>> RegistryObject<VL> registerApplySafe(Class<L> key, Function<? super Class<L>, ? extends VL> value) {
			return CastUtilities.castUnchecked(registerApply(key, CastUtilities.castUnchecked(value))); // COMMENT should be safe
		}

		@Override
		@Deprecated
		public <VL extends IDuplexFunction<JAXBElement<?>, ?>> RegistryObject<VL> registerApply(Class<?> key, Function<? super Class<?>, ? extends VL> value) { return super.registerApply(key, value); }

		@Override
		@Deprecated
		public <VL extends IDuplexFunction<JAXBElement<?>, ?>> RegistryObject<VL> register(Class<?> key, VL value) { return super.register(key, value); }

		@Override
		@Deprecated
		public Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> get(Class<?> key) { return super.get(key); }

		public <L> Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<L>, ?>>> getSafe(Class<L> key) {
			return CastUtilities.castUnchecked(get(key)); // COMMENT should be safe
		}
	}
}
