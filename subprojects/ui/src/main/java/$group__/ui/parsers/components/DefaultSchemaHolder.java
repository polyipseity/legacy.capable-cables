package $group__.ui.parsers.components;

import $group__.ui.UIConfiguration;
import $group__.ui.UIConstants;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.ThrowableUtilities;
import jakarta.xml.bind.JAXBContext;
import org.jetbrains.annotations.NonNls;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

public enum DefaultSchemaHolder {
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
		SCHEMA = ThrowableUtilities.Try.call(() -> {
			try (InputStream res = AssertionUtilities.assertNonnull(DefaultSchemaHolder.class.getResourceAsStream(getSchemaLocation()))) {
				return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
			}
		}, UIConfiguration.getInstance().getLogger())
				.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
		CONTEXT = ThrowableUtilities.Try.call(() ->
				JAXBContext.newInstance(getContextPath(), DefaultUIComponentParser.class.getClassLoader()), UIConfiguration.getInstance().getLogger()).orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
	}

	public static String getContextPath() { return CONTEXT_PATH; }

	public static String getSchemaLocation() { return SCHEMA_LOCATION; }

	public static Schema getSchema() { return SCHEMA; }

	public static String getSchemaNamespaceUri() { return SCHEMA_NAMESPACE_URI; }

	public static JAXBContext getContext() { return CONTEXT; }
}
