package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ObjectFactory;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.function.Function;

public enum UIJAXBUtilities {
	;

	public static <T> QName getQName(Function<@Nullable T, @Nonnull ? extends JAXBElement<?>> factory) {
		return factory.apply(null).getName();
	}

	public enum ObjectFactories {
		;

		private static final ObjectFactory DEFAULT_UI_OBJECT_FACTORY = new ObjectFactory();

		public static ObjectFactory getDefaultUIObjectFactory() { return DEFAULT_UI_OBJECT_FACTORY; }
	}
}
