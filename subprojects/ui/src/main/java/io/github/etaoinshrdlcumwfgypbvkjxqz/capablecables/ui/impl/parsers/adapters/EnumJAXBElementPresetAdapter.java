package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.JAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.RegistryObject;
import jakarta.xml.bind.JAXBElement;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public enum EnumJAXBElementPresetAdapter
		implements ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> {
	BOOLEAN(ImmutableTuple2.of(Boolean.class, Boolean.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createBoolean)),
	BYTE(ImmutableTuple2.of(Byte.class, Byte.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createByte)),
	SHORT(ImmutableTuple2.of(Short.class, Short.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createShort)),
	INT(ImmutableTuple2.of(Integer.class, Integer.class), new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createInt)),
	LONG(ImmutableTuple2.of(Long.class, Long.class), new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createLong)),
	FLOAT(ImmutableTuple2.of(Float.class, Float.class), new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createFloat)),
	DOUBLE(ImmutableTuple2.of(Double.class, Double.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createDouble)),
	STRING(ImmutableTuple2.of(String.class, String.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createString)),
	COLOR(ImmutableTuple2.of(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color.class, Color.class),
			new IDuplexFunction.Functional<>(
					left -> JAXBUtilities.getActualValueOptional(left)
							.map(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color::toJava)
							.orElse(null),
					right -> UIJAXBObjectFactories.getDefaultComponentObjectFactory()
							.createColor(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color.fromJava(right)))),
	@SuppressWarnings("UnstableApiUsage") SET(ImmutableTuple2.of(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Set.class, CastUtilities.<Class<Set<?>>>castUnchecked(Set.class)),
			new IDuplexFunction.Functional<>(
					left -> JAXBUtilities.getActualValueOptional(left)
							.map(left2 ->
									left2.getAny().stream().sequential() // COMMENT not unordered even though it is a Set
											.map(leftElement -> JAXBAdapterRegistries.getFromRawAdapter(leftElement)
													.leftToRight(leftElement))
											.collect(ImmutableSet.toImmutableSet())
							)
							.orElse(null),
					right -> {
						io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Set left = new io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Set();
						@Immutable Set<?> leftData = right.stream().sequential() // COMMENT not unordered even though it is a Set
								.map(element -> JAXBAdapterRegistries.getToRawAdapter(element)
										.rightToLeft(element))
								.collect(ImmutableSet.toImmutableSet());
						left.getAny().addAll(leftData);
						return UIJAXBObjectFactories.getDefaultComponentObjectFactory()
								.createSet(left);
					})),
	@SuppressWarnings("UnstableApiUsage") LIST(ImmutableTuple2.of(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.List.class, CastUtilities.<Class<List<?>>>castUnchecked(List.class)),
			new IDuplexFunction.Functional<>(
					left -> JAXBUtilities.getActualValueOptional(left)
							.map(left2 ->
									left2.getAny().stream().sequential()
											.map(leftElement -> JAXBAdapterRegistries.getFromRawAdapter(leftElement)
													.leftToRight(leftElement))
											.collect(ImmutableList.toImmutableList())
							)
							.orElse(null),
					right -> {
						io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.List left = new io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.List();
						@Immutable List<?> leftData = right.stream().sequential()
								.map(element -> JAXBAdapterRegistries.getToRawAdapter(element)
										.rightToLeft(element))
								.collect(ImmutableList.toImmutableList());
						left.getAny().addAll(leftData);
						return UIJAXBObjectFactories.getDefaultComponentObjectFactory()
								.createList(left);
					})),
	;

	private final ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> delegate;

	<L, R, V extends IDuplexFunction<JAXBElement<L>, R> & Serializable> EnumJAXBElementPresetAdapter(ITuple2<? extends Class<L>, ? extends Class<R>> key, V value) {
		RegistryObject<V> value2 = JAXBAdapterRegistries.Element.getInstance().registerChecked(ITuple2.upcast(key), value);
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Override
	public ITuple2<? extends Class<?>, ? extends Class<?>> getLeft() {
		return getDelegate().getLeft();
	}

	protected ITuple2<? extends ITuple2<? extends Class<?>, ? extends Class<?>>, ? extends RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> getDelegate() {
		return delegate;
	}

	@Override
	public RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>> getRight() {
		return getDelegate().getRight();
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}
}
