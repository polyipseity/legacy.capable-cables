package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.ObjectFactory;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.function.Function;

public enum UIJAXBUtilities {
	;

	public static <T> QName getQName(Function<T, ? extends JAXBElement<?>> factory) {
		return AssertionUtilities.assertNonnull(factory.apply(null)).getName();
	}

	public enum ObjectFactories {
		;

		private static final ObjectFactory DEFAULT_COMPONENT_OBJECT_FACTORY = new ObjectFactory();

		public static ObjectFactory getDefaultComponentObjectFactory() { return DEFAULT_COMPONENT_OBJECT_FACTORY; }
	}
}
