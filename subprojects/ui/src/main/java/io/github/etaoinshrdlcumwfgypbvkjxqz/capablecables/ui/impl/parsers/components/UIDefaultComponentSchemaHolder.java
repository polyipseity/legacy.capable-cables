package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.JAXBContextRegisterBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.jetbrains.annotations.NonNls;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;

public enum UIDefaultComponentSchemaHolder {
	;

	@NonNls
	public static final String CONTEXT_PATH = UIConstants.Local.XJC_MAIN_COMPONENTS_CONTEXT_PATH;
	@NonNls
	public static final String SCHEMA_LOCATION = "components.xsd";
	@NonNls
	public static final String SCHEMA_NAMESPACE_URI = "https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/schemas/ui/components";
	private static final Schema SCHEMA;
	private static final JAXBContext CONTEXT;

	static {
		InputStream res = AssertionUtilities.assertNonnull(UIDefaultComponentSchemaHolder.class.getResourceAsStream(getSchemaLocation()));
		try {
			SCHEMA = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
		} catch (SAXException e) {
			throw ThrowableUtilities.propagate(e);
		} finally {
			ThrowableUtilities.runQuietly(res::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
		}
		try {
			JAXBContextRegisterBusEvent registerEvent = new JAXBContextRegisterBusEvent();
			registerEvent.addClassesToBeBound(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory().getClass());
			UIEventBusEntryPoint.getEventBus().onNext(registerEvent);
			CONTEXT = JAXBContext.newInstance(registerEvent.getClassesToBeBoundView().toArray(new Class<?>[0]));
		} catch (JAXBException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	public static String getContextPath() { return CONTEXT_PATH; }

	public static String getSchemaLocation() { return SCHEMA_LOCATION; }

	public static Schema getSchema() { return SCHEMA; }

	public static String getSchemaNamespaceURI() { return getSchemaNamespaceUri(); }

	public static String getSchemaNamespaceUri() {
		return SCHEMA_NAMESPACE_URI;
	}

	public static JAXBContext getContext() { return CONTEXT; }
}
