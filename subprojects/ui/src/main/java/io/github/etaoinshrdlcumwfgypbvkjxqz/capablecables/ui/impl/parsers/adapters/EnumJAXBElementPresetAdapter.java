package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.JAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.Registry;
import jakarta.xml.bind.JAXBElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("unused")
public enum EnumJAXBElementPresetAdapter
		implements ITuple2<Class<?>, Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>>> {
	BOOLEAN(Boolean.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createBoolean)),
	BYTE(Byte.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createByte)),
	SHORT(Short.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createShort)),
	INT(Integer.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createInt)),
	LONG(Long.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createLong)),
	FLOAT(Float.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createFloat)),
	DOUBLE(Double.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createDouble)),
	STRING(String.class, new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBObjectFactories.getDefaultComponentObjectFactory()::createString)),
	COLOR(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color.class, new IDuplexFunction.Functional<>(
			left -> JAXBUtilities.getActualValueOptional(left).map(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color::toJava).orElse(null),
			(Color right) -> UIJAXBObjectFactories.getDefaultComponentObjectFactory().createColor(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color.fromJava(right)))),
	;

	private final ITuple2<Class<?>, Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>>> delegate;

	<L, V extends IDuplexFunction<JAXBElement<L>, ?> & Serializable> EnumJAXBElementPresetAdapter(Class<L> key, V value) {
		Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>> value2 = CastUtilities.castUnchecked(JAXBAdapterRegistries.Element.getInstance().registerSafe(key, value));
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Override
	public Class<?> getLeft() {
		return getDelegate().getLeft();
	}

	protected ITuple2<Class<?>, Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>>> getDelegate() {
		return delegate;
	}

	@Override
	public Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>> getRight() {
		return getDelegate().getRight();
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}
}
