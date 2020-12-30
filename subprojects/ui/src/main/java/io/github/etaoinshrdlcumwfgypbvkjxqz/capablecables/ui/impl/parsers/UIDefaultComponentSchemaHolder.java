package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers;

import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.config.UISchemas;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.JAXBContextRegisterBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.common.JAXBCommonAdapters;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.JAXBDefaultAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.JAXBDefaultElementAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.JAXBDefaultObjectAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.JAXBUIAdapters;
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
	public static final String CONTEXT_PATH = UIConstants.Local.XJC_MAIN_UI_CONTEXT_PATH;
	@NonNls
	public static final String SCHEMA_NAMESPACE_URI = "https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/schemas/ui";
	private static final Schema SCHEMA;
	private static final JAXBContext CONTEXT;
	private static final IJAXBAdapterRegistry ADAPTER_REGISTRY;

	static {
		{
			InputStream res = AssertionUtilities.assertNonnull(UIDefaultComponentSchemaHolder.class.getResourceAsStream(UISchemas.getSchemaLocation(getSchemaNamespaceURI())));
			try {
				SCHEMA = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
			} catch (SAXException e) {
				throw ThrowableUtilities.propagate(e);
			} finally {
				ThrowableUtilities.runQuietly(res::close, IOException.class, UIConfiguration.getInstance().getThrowableHandler());
			}
		}

		{
			JAXBContextRegisterBusEvent registerEvent = new JAXBContextRegisterBusEvent();
			registerEvent.addClassesToBeBound(Iterators.singletonIterator(JAXBUIUtilities.ObjectFactories.getDefaultUIObjectFactory().getClass()));
			UIEventBusEntryPoint.getBusSubscriber().onNext(registerEvent);
			try {
				CONTEXT = JAXBContext.newInstance(registerEvent.getClassesToBeBoundView().toArray(new Class<?>[0]));
			} catch (JAXBException e) {
				throw ThrowableUtilities.propagate(e);
			}
		}

		{
			ADAPTER_REGISTRY = new JAXBDefaultAdapterRegistry(new JAXBDefaultObjectAdapterRegistry(), new JAXBDefaultElementAdapterRegistry());
			JAXBCommonAdapters.registerAll(getAdapterRegistry());
			JAXBUIAdapters.registerAll(getAdapterRegistry());
		}
	}

	public static IJAXBAdapterRegistry getAdapterRegistry() {
		return ADAPTER_REGISTRY;
	}

	public static String getContextPath() { return CONTEXT_PATH; }

	public static Schema getSchema() { return SCHEMA; }

	public static String getSchemaNamespaceURI() { return SCHEMA_NAMESPACE_URI; }

	public static JAXBContext getContext() { return CONTEXT; }
}
