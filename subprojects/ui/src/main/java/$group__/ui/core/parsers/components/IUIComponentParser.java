package $group__.ui.core.parsers.components;

import $group__.ui.UIConfiguration;
import $group__.ui.core.parsers.IUIResourceParser;
import $group__.ui.parsers.components.UIDefaultComponentParser;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.functions.FunctionUtilities;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import jakarta.xml.bind.JAXBContext;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public interface IUIComponentParser<T, R>
		extends IUIResourceParser<T, R> {

	<H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super IParserContext, ?, ? super H> handler);

	@Immutable
	enum EnumHandlerType {
		VIEW_HANDLER(false) {
			@Override
			public EnumHandlerType getVariant(boolean element) { return element ? VIEW_ELEMENT_HANDLER : VIEW_HANDLER; }
		},
		COMPONENT_HANDLER(false) {
			@Override
			public EnumHandlerType getVariant(boolean element) { return element ? COMPONENT_ELEMENT_HANDLER : COMPONENT_HANDLER; }
		},
		VIEW_ELEMENT_HANDLER(true, VIEW_HANDLER),
		COMPONENT_ELEMENT_HANDLER(true, COMPONENT_HANDLER),
		;

		@Nullable
		protected final EnumHandlerType variantCounterpart;

		public static final ImmutableSet<EnumHandlerType> ALL = Sets.immutableEnumSet(EnumSet.allOf(EnumHandlerType.class));
		@SuppressWarnings("UnstableApiUsage")
		public static final ImmutableSet<EnumHandlerType> ELEMENTS_ONLY = ALL.stream().unordered()
				.filter(EnumHandlerType::isElement)
				.collect(Sets.toImmutableEnumSet());
		@SuppressWarnings("UnstableApiUsage")
		public static final ImmutableSet<EnumHandlerType> OBJECTS_ONLY = ALL.stream().unordered()
				.filter(FunctionUtilities.notPredicate(EnumHandlerType::isElement))
				.collect(Sets.toImmutableEnumSet());
		protected final boolean element;

		EnumHandlerType(boolean element) { this(element, null); }

		EnumHandlerType(boolean element, @Nullable EnumHandlerType variantCounterpart) {
			this.element = element;
			this.variantCounterpart = variantCounterpart;
		}

		public EnumHandlerType getVariant(boolean element) {
			return getVariantCounterpart().
					orElseThrow(AssertionError::new)
					.getVariant(element);
		}

		public boolean isElement() { return element; }

		private Optional<? extends EnumHandlerType> getVariantCounterpart() { return Optional.ofNullable(variantCounterpart); }
	}

	enum SchemaHolder {
		;

		public static final String COMPONENTS_CONTEXT_PATH = "${xjcMainComponentsContextPath}";
		@SuppressWarnings("HardcodedFileSeparator")
		public static final INamespacePrefixedString SCHEMA_LOCATION = new NamespacePrefixedString(UIConfiguration.INSTANCE.getModID(), "ui/schemas/components.xsd");
		public static final Schema SCHEMA;
		public static final String SCHEMA_NAMESPACE_URI = "https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/schemas/ui/components";
		public static final JAXBContext CONTEXT;

		static {
			SCHEMA = ThrowableUtilities.Try.call(() -> {
				try (InputStream res = ResourceUtilities.getInputStream(SCHEMA_LOCATION)) {
					return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
				}
			}, UIConfiguration.INSTANCE.getLogger())
					.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
			CONTEXT = ThrowableUtilities.Try.call(() ->
					JAXBContext.newInstance(COMPONENTS_CONTEXT_PATH, UIDefaultComponentParser.class.getClassLoader()), UIConfiguration.INSTANCE.getLogger()).orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow);
		}
	}
}
