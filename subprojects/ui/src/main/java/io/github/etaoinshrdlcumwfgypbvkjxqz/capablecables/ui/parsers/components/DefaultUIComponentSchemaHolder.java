package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.UIJAXBObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
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

public enum DefaultUIComponentSchemaHolder {
	;

	@NonNls
	private static final String CONTEXT_PATH = UIConstants.Local.XJC_MAIN_COMPONENTS_CONTEXT_PATH;
	@NonNls
	private static final String SCHEMA_LOCATION = "components.xsd";
	private static final Schema SCHEMA;
	@NonNls
	private static final String SCHEMA_NAMESPACE_URI = "https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/schemas/ui/components";
	private static final JAXBContext CONTEXT;

	static {
		InputStream res = AssertionUtilities.assertNonnull(DefaultUIComponentSchemaHolder.class.getResourceAsStream(getSchemaLocation()));
		try {
			SCHEMA = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
		} catch (SAXException e) {
			throw ThrowableUtilities.propagate(e);
		} finally {
			ThrowableUtilities.runQuietly(res::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
		}
		try {
			DefaultUIJAXBContextRegisterEvent registerEvent = new DefaultUIJAXBContextRegisterEvent();
			registerEvent.addClassesToBeBound(UIJAXBObjectFactories.getDefaultComponentObjectFactory().getClass());
			UIEventBusEntryPoint.getEventBus().onNext(registerEvent);
			CONTEXT = JAXBContext.newInstance(registerEvent.getClassesToBeBound().toArray(new Class<?>[0]));
		} catch (JAXBException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	public static String getContextPath() { return CONTEXT_PATH; }

	public static String getSchemaLocation() { return SCHEMA_LOCATION; }

	public static Schema getSchema() { return SCHEMA; }

	public static String getSchemaNamespaceURI() { return SCHEMA_NAMESPACE_URI; }

	public static JAXBContext getContext() { return CONTEXT; }
}
