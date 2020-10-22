package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters;

import com.google.common.base.Suppliers;
import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractClassBasedDuplexFunctionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractTuple2Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import jakarta.xml.bind.JAXBElement;
import org.slf4j.Logger;

import javax.xml.namespace.QName;
import java.util.Map;
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
					Element.getInstance().getWithLeftChecked(((JAXBElement<?>) rawObject).getName())
							.map(IRegistryObject::getValue);
		return (Optional<? extends IDuplexFunction<L, ?>>) // COMMENT should be safe
				Object.getInstance().getWithLeftChecked(rawObject.getClass())
						.map(IRegistryObject::getValue);
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
								.map(IRegistryObject::getValue);
		Optional<? extends IDuplexFunction<JAXBElement<?>, R>> toElement =
				(Optional<? extends IDuplexFunction<JAXBElement<?>, R>>)
						Element.getInstance().getWithRightChecked(object.getClass())
								.map(IRegistryObject::getValue);
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
			extends AbstractClassBasedDuplexFunctionRegistry {
		private static final Supplier<Object> INSTANCE = Suppliers.memoize(Object::new);
		private static final long serialVersionUID = 8313852600800208719L;
		private static final MapMaker DATA_BUILDER = MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium());
		private final ConcurrentMap<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObjectInternal<? extends IDuplexFunction<?, ?>>> data;
		private final ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> leftData;
		private final ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> rightData;

		private Object() {
			super(true);
			this.data = getDataBuilder().makeMap();
			this.leftData = getDataBuilder().makeMap();
			this.rightData = getDataBuilder().makeMap();
		}

		protected static MapMaker getDataBuilder() {
			return DATA_BUILDER;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		@Override
		protected ConcurrentMap<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObjectInternal<? extends IDuplexFunction<?, ?>>> getData() {
			return data;
		}

		@Override
		protected Logger getLogger() {
			return UIConfiguration.getInstance().getLogger();
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		@Override
		protected ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> getLeftData() {
			return leftData;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		@Override
		protected ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> getRightData() {
			return rightData;
		}

		public static Object getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
	}

	public static final class Element
			extends AbstractTuple2Registry<QName, Class<?>, IDuplexFunction<JAXBElement<?>, ?>> {
		private static final Supplier<Element> INSTANCE = Suppliers.memoize(Element::new);
		private static final long serialVersionUID = -1898139702002928271L;
		private static final MapMaker DATA_BUILDER = MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium());
		private final ConcurrentMap<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObjectInternal<? extends IDuplexFunction<JAXBElement<?>, ?>>> data;
		private final ConcurrentMap<QName, ITuple2<? extends QName, ? extends Class<?>>> leftData;
		private final ConcurrentMap<Class<?>, ITuple2<? extends QName, ? extends Class<?>>> rightData;

		private Element() {
			super(true);
			this.data = getDataBuilder().makeMap();
			this.leftData = getDataBuilder().makeMap();
			this.rightData = getDataBuilder().makeMap();
		}

		protected static MapMaker getDataBuilder() {
			return DATA_BUILDER;
		}

		public static Element getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		@Override
		protected Map<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObjectInternal<? extends IDuplexFunction<JAXBElement<?>, ?>>> getData() {
			return data;
		}

		@Override
		protected Logger getLogger() {
			return UIConfiguration.getInstance().getLogger();
		}

		@Override
		@Deprecated
		public Optional<? extends IRegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> get(ITuple2<? extends QName, ? extends Class<?>> key) {
			return super.get(key);
		}

		public <L, R, VL extends IDuplexFunction<JAXBElement<L>, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends QName, ? extends Class<R>> key, VL value) {
			return CastUtilities.castUnchecked(register(key, CastUtilities.castUnchecked(value)));
		}

		@Override
		@Deprecated
		public <VL extends IDuplexFunction<JAXBElement<?>, ?>> IRegistryObject<VL> register(ITuple2<? extends QName, ? extends Class<?>> key, VL value) {
			return super.register(key, value);
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		@Override
		protected Map<QName, ITuple2<? extends QName, ? extends Class<?>>> getLeftData() {
			return leftData;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		@Override
		protected Map<Class<?>, ITuple2<? extends QName, ? extends Class<?>>> getRightData() {
			return rightData;
		}

		@Override
		@Deprecated
		public Optional<? extends IRegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> getWithLeft(QName key) {
			return super.getWithLeft(key);
		}

		@Override
		@Deprecated
		public Optional<? extends IRegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> getWithRight(Class<?> key) {
			return super.getWithRight(key);
		}

		public Optional<? extends IRegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> getWithLeftChecked(QName key) {
			return getWithLeft(key);
		}

		public <L, R> Optional<? extends IRegistryObject<? extends IDuplexFunction<JAXBElement<L>, R>>> getChecked(ITuple2<? extends QName, ? extends Class<R>> key) {
			return CastUtilities.castUnchecked(get(key));
		}

		@SuppressWarnings("unchecked")
		public <R> Optional<? extends IRegistryObject<? extends IDuplexFunction<JAXBElement<?>, R>>> getWithRightChecked(Class<R> key) {
			return (Optional<? extends IRegistryObject<? extends IDuplexFunction<JAXBElement<?>, R>>>) getWithRight(key);
		}
	}
}
