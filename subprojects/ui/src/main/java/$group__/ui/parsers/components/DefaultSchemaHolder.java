package $group__.ui.parsers.components;

import $group__.ui.UIConfiguration;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.ThrowableUtilities;
import jakarta.xml.bind.JAXBContext;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

public enum DefaultSchemaHolder {
	;

	public static final String COMPONENTS_CONTEXT_PATH = "${xjcMainComponentsContextPath}";
	public static final String SCHEMA_LOCATION = "components.xsd";
	public static final Schema SCHEMA;
	public static final String SCHEMA_NAMESPACE_URI = "https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/schemas/ui/components";
	public static final JAXBContext CONTEXT;

	static {
		SCHEMA = ThrowableUtilities.Try.call(() -> {
			try (InputStream res = AssertionUtilities.assertNonnull(DefaultSchemaHolder.class.getResourceAsStream(SCHEMA_LOCATION))) {
				return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
			}
		}, UIConfiguration.INSTANCE.getLogger())
				.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
		CONTEXT = ThrowableUtilities.Try.call(() ->
				JAXBContext.newInstance(COMPONENTS_CONTEXT_PATH, UIDefaultComponentParser.class.getClassLoader()), UIConfiguration.INSTANCE.getLogger()).orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
	}
}
