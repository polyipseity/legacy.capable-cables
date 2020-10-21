package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.AbstractDuplexFunctionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.AbstractClassTuple2Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.RegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import jakarta.xml.bind.JAXBElement;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public enum JAXBAdapterRegistries {
	;

	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	public static <L> IDuplexFunction<L, ?> getFromRawAdapter(L rawObject)
			throws IllegalArgumentException {
		return findFromRawAdapter(rawObject)
				.orElseThrow(() ->
						new IllegalArgumentException(
								new LogMessageBuilder()
										.addMarkers(UIMarkers.getInstance()::getMarkerJAXB)
										.addKeyValue("rawObject", rawObject)
										.addMessages(() -> getResourceBundle().getString("adapter.from.find.not_found"))
										.build()
						));
	}

	@SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
	public static <L> Optional<? extends IDuplexFunction<L, ?>> findFromRawAdapter(L rawObject) {
		if (rawObject instanceof JAXBElement)
			return (Optional<? extends IDuplexFunction<L, ?>>) // COMMENT should be safe
					Element.getInstance().getWithLeftChecked(((JAXBElement<?>) rawObject).getDeclaredType())
							.map(RegistryObject::getValue);
		return (Optional<? extends IDuplexFunction<L, ?>>) // COMMENT should be safe
				Object.getInstance().getWithLeftChecked(rawObject.getClass())
						.map(RegistryObject::getValue);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static <R> IDuplexFunction<?, R> getToRawAdapter(R object)
			throws IllegalArgumentException, IllegalStateException {
		return findToRawAdapter(object)
				.orElseThrow(() ->
						new IllegalArgumentException(
								new LogMessageBuilder()
										.addMarkers(UIMarkers.getInstance()::getMarkerJAXB)
										.addKeyValue("object", object)
										.addMessages(() -> getResourceBundle().getString("adapter.to.find.not_found"))
										.build()
						));
	}

	@SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
	public static <R> Optional<? extends IDuplexFunction<?, R>> findToRawAdapter(R object)
			throws IllegalStateException {
		Optional<? extends IDuplexFunction<?, R>> toRaw =
				(Optional<? extends IDuplexFunction<?, R>>)
						Object.getInstance().getWithRightChecked(object.getClass())
								.map(RegistryObject::getValue);
		Optional<? extends IDuplexFunction<JAXBElement<?>, R>> toElement =
				(Optional<? extends IDuplexFunction<JAXBElement<?>, R>>)
						Element.getInstance().getWithRightChecked(object.getClass())
								.map(RegistryObject::getValue);
		if (toRaw.isPresent() && toElement.isPresent())
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerJAXB)
							.addKeyValue("object", object)
							.addMessages(() -> getResourceBundle().getString("adapter.to.find.ambiguous"))
							.build()
			);
		return toRaw.isPresent() ? toRaw : toElement;
	}

	public static final class Object
			extends AbstractDuplexFunctionRegistry {
		private static final Supplier<Object> INSTANCE = Suppliers.memoize(Object::new);
		private static final long serialVersionUID = 8313852600800208719L;

		private Object() {
			super(true, UIConfiguration.getInstance().getLogger(),
					builder -> builder
							.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
							.initialCapacity(CapacityUtilities.getInitialCapacityMedium()));
		}

		public static Object getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
	}

	public static final class Element
			extends AbstractClassTuple2Registry<IDuplexFunction<JAXBElement<?>, ?>> {
		private static final Supplier<Element> INSTANCE = Suppliers.memoize(Element::new);
		private static final long serialVersionUID = -1898139702002928271L;

		private Element() {
			super(true, UIConfiguration.getInstance().getLogger(),
					builder -> builder
							.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
							.initialCapacity(CapacityUtilities.getInitialCapacityMedium()));
		}

		public static Element getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

		public <L, R, VL extends IDuplexFunction<JAXBElement<L>, R>> RegistryObject<VL> registerChecked(ITuple2<Class<L>, Class<R>> key, VL value) {
			return CastUtilities.castUnchecked(register(ITuple2.upcast(key), CastUtilities.castUnchecked(value)));
		}

		@Override
		@Deprecated
		public <VL extends IDuplexFunction<JAXBElement<?>, ?>> RegistryObject<VL> register(ITuple2<Class<?>, Class<?>> key, VL value) {
			return super.register(key, value);
		}

		@Override
		@Deprecated
		public Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> getWithLeft(Class<?> key) {
			return super.getWithLeft(key);
		}

		public <L, R> Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<L>, R>>> getChecked(ITuple2<Class<L>, Class<R>> key) {
			return CastUtilities.castUnchecked(get(ITuple2.upcast(key)));
		}

		public <L> Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<L>, ?>>> getWithLeftChecked(Class<L> key) {
			return CastUtilities.castUnchecked(getWithLeft(key));
		}

		@SuppressWarnings("unchecked")
		public <R> Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<?>, R>>> getWithRightChecked(Class<R> key) {
			return (Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<?>, R>>>) getWithRight(key);
		}

		@Override
		@Deprecated
		protected ConcurrentMap<ITuple2<Class<?>, Class<?>>, RegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> getData() {
			return super.getData();
		}

		@Override
		@Deprecated
		public Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> get(ITuple2<Class<?>, Class<?>> key) {
			return super.get(key);
		}
	}
}
