package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.ObjectFactory;

public enum UIJAXBObjectFactories {
	;

	private static final ObjectFactory DEFAULT_COMPONENT_OBJECT_FACTORY = new ObjectFactory();

	public static ObjectFactory getDefaultComponentObjectFactory() { return DEFAULT_COMPONENT_OBJECT_FACTORY; }
}
