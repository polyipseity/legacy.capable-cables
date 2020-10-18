package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.UIJAXBObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.JAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import jakarta.xml.bind.JAXBElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("unused")
public enum EnumJAXBElementPresetAdapter
		implements Map.Entry<Class<?>, Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>>> {
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

	private final Class<?> key;
	private final Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>> value;

	<L, V extends IDuplexFunction<JAXBElement<L>, ?> & Serializable> EnumJAXBElementPresetAdapter(Class<L> key, V value) {
		this.key = key;
		this.value = CastUtilities.castUnchecked(JAXBAdapterRegistries.Element.getInstance().registerSafe(key, value));
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Nonnull
	@Override
	public Class<?> getKey() { return key; }

	@Nonnull
	@Override
	public Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>> getValue() { return value; }

	@Nullable
	@Override
	public Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>> setValue(Registry.RegistryObject<IDuplexFunction<JAXBElement<?>, ?>> value)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }
}
