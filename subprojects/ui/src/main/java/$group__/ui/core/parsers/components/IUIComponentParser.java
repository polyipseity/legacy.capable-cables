package $group__.ui.core.parsers.components;

import $group__.ui.ConfigurationUI;
import $group__.ui.core.parsers.IUIResourceParser;
import $group__.ui.parsers.components.UIDefaultComponentParser;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import jakarta.xml.bind.JAXBContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.concurrent.Immutable;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public interface IUIComponentParser<T, R>
		extends IUIResourceParser<T, R> {

	Map<String, Class<?>> getAliasesView();

	<H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super UIDefaultComponentParser.IParserContext, ?, ? super H> handler);

	@Immutable
	enum EnumHandlerType {
		VIEW_HANDLER,
		COMPONENT_HANDLER,
	}

	enum SchemaHolder {
		;

		public static final String COMPONENTS_CONTEXT_PATH = "${xjcMainComponentsContextPath}";
		@SuppressWarnings("HardcodedFileSeparator")
		public static final INamespacePrefixedString SCHEMA_LOCATION = new NamespacePrefixedString(ConfigurationUI.getModId(), "ui/schemas/components.xsd");
		public static final Schema SCHEMA;
		public static final String SCHEMA_NAMESPACE_URI = "https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/schemas/ui/components";
		public static final JAXBContext CONTEXT;
		private static final Logger LOGGER = LogManager.getLogger();

		static {
			SCHEMA = ThrowableUtilities.Try.call(() -> {
				try (InputStream res = ResourceUtilities.getInputStream(SCHEMA_LOCATION)) {
					return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
				}
			}, LOGGER)
					.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
			CONTEXT = ThrowableUtilities.Try.call(() ->
					JAXBContext.newInstance(COMPONENTS_CONTEXT_PATH, UIDefaultComponentParser.class.getClassLoader()), LOGGER).orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
		}
	}
}
