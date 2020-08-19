package $group__.client.ui.mvvm.views.components.parsers;

import $group__.client.ui.ConfigurationUI;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.parsers.IUIResourceParser;
import $group__.client.ui.mvvm.structures.UIPropertyMappingValue;
import $group__.utilities.*;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.NamedNodeMapMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class UIXMLDOMComponentParser<T extends IUIComponentManager<?>>
		extends IHasGenericClass.Impl<T>
		implements IUIResourceParser<T, Document> {
	@SuppressWarnings("HardcodedFileSeparator")
	public static final ResourceLocation SCHEMA_LOCATION = new ResourceLocation(ConfigurationUI.getModId(), "ui/schemas/components.xsd");
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Schema SCHEMA;

	static {
		SCHEMA = Try.call(() -> {
			try (InputStream res = ResourceUtilities.getResource(SCHEMA_LOCATION).getInputStream()) {
				return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
			}
		}, LOGGER).orElseThrow(ThrowableCatcher::rethrow);
	}

	protected ConcurrentMap<String, String> aliases = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	@Nullable
	protected UIComponentPrototype prototype;

	public UIXMLDOMComponentParser(Class<T> managerClass) { super(managerClass); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void parseResource(Document resource) throws IOException, SAXException, ClassNotFoundException {
		SCHEMA.newValidator().validate(new DOMSource(resource));
		reset();
		Element root = resource.getDocumentElement();
		@Nullable String namespaceURI = root.getNamespaceURI();

		DOMUtilities.getChildrenByTagNameNS(root, namespaceURI, "alias").forEach(al ->
				getAliases().put(DOMUtilities.getAttributeValue(al, "name").orElseThrow(InternalError::new),
						DOMUtilities.getAttributeValue(al, "class").orElseThrow(InternalError::new)));
		Node managerNode = DOMUtilities.getChildrenByTagNameNS(root, namespaceURI, "component").stream().unordered().findAny().orElseThrow(() ->
				BecauseOf.illegalArgument("Component manager not found",
						"resource", resource));

		{
			String managerClassName = getClassFromMaybeAlias(getAliases(), DOMUtilities.getAttributeValue(managerNode, "class").orElseThrow(InternalError::new));
			Class<?> managerClass = Class.forName(managerClassName);
			if (!getGenericClass().equals(managerClass))
				throw BecauseOf.illegalArgument("Component manager type does not match",
						"managerClass", managerClass,
						"getGenericClass()", getGenericClass(),
						"resource", resource);
		}

		setPrototype(TreeUtilities.visitNodesDepthFirst(managerNode,
				n -> {
					@Nullable NamedNodeMap as = n.getAttributes();
					assert as != null;
					Map<ResourceLocation, IUIPropertyMappingValue> mappingC = new HashMap<>(as.getLength() + n.getChildNodes().getLength());
					ComponentBasedPrototype.constructMapping(mappingC, n, namespaceURI);

					// COMMENT adds the default UI event, they are specified as attributes instead of elements
					NamedNodeMapMap.wrap(as).forEach((k, v) -> mappingC.put(new ResourceLocation(k),
							new UIPropertyMappingValue(null, Optional.ofNullable(v.getNodeValue()).map(ResourceLocation::new).orElse(null))));

					UIComponentPrototype ret = new UIComponentPrototype(
							getClassFromMaybeAlias(getAliases(), DOMUtilities.getAttributeValue(n, "class").orElseThrow(InternalError::new)),
							mappingC);
					ret.addExtensions(
							DOMUtilities.getChildrenByTagNameNS(n, namespaceURI, "extension").stream().sequential()
									.map(e -> {
										Map<ResourceLocation, IUIPropertyMappingValue> mappingE = new HashMap<>(e.getChildNodes().getLength());
										ComponentBasedPrototype.constructMapping(mappingE, e, namespaceURI);
										return new UIExtensionPrototype(
												getClassFromMaybeAlias(getAliases(), DOMUtilities.getAttributeValue(e, "class").orElseThrow(InternalError::new)),
												mappingE);
									})
									.collect(ImmutableList.toImmutableList()));
					return ret;
				},
				n -> DOMUtilities.getChildrenByTagNameNS(n, namespaceURI, "component"),
				(p, c) -> {
					p.addChildren(c);
					return p;
				}, n -> {
					throw BecauseOf.illegalArgument("Cyclic hierarchy detected",
							"n", n,
							"resource", resource);
				})
				.orElseThrow(InternalError::new));
	}

	@Override
	public void reset() {
		setPrototype(null);
		getAliases().clear();
	}

	@Override
	public T createUI() {
		List<Runnable> queue = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		return getPrototype()
				.map(cp -> Try.call(() -> {
					IUIComponent c = cp.createComponent(queue);
					queue.forEach(Runnable::run);
					return c;
				}, LOGGER).orElseThrow(ThrowableCatcher::rethrow))
				.map(CastUtilities::<T>castUnchecked)
				.orElseThrow(() -> new IllegalStateException("Prototype has not been created"));
	}

	protected Optional<UIComponentPrototype> getPrototype() { return Optional.ofNullable(prototype); }

	protected void setPrototype(@Nullable UIComponentPrototype prototype) { this.prototype = prototype; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, String> getAliases() { return aliases; }

	protected static String getClassFromMaybeAlias(ConcurrentMap<String, String> aliases, String maybeAlias) { return aliases.getOrDefault(maybeAlias, maybeAlias); }

	protected static abstract class ComponentBasedPrototype {
		protected final String className;
		protected final Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping;

		protected ComponentBasedPrototype(String className, Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping) {
			this.className = className;
			this.propertyMapping = ImmutableMap.copyOf(propertyMapping);
		}

		protected static void constructMapping(Map<ResourceLocation, IUIPropertyMappingValue> mapping, Node node, @Nullable String namespaceURI) {
			DOMUtilities.getChildrenByTagNameNS(node, namespaceURI, "property").forEach(p ->
					mapping.put(
							DOMUtilities.getAttributeValue(p, "key").map(ResourceLocation::new).orElseThrow(InternalError::new),
							new UIPropertyMappingValue(p.getFirstChild(), null)));
			DOMUtilities.getChildrenByTagNameNS(node, namespaceURI, "binding").forEach(p ->
					mapping.put(
							DOMUtilities.getAttributeValue(p, "key").map(ResourceLocation::new).orElseThrow(InternalError::new),
							new UIPropertyMappingValue(
									p.getFirstChild(),
									DOMUtilities.getAttributeValue(p, "binding").map(ResourceLocation::new).orElse(null))));
		}

		protected String getClassName() { return className; }

		protected Map<ResourceLocation, IUIPropertyMappingValue> getPropertyMapping() { return propertyMapping; }
	}

	protected static class UIExtensionPrototype
			extends ComponentBasedPrototype {
		private static final Logger LOGGER = LogManager.getLogger();

		protected UIExtensionPrototype(String className, Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping) { super(className, propertyMapping); }

		protected IUIExtension<?, ?> createComponent(IUIComponent container)
				throws Throwable {
			final EnumConstructorType[] type = new EnumConstructorType[1];
			MethodHandle constructor =
					Try.call(() -> {
						type[0] = EnumConstructorType.EXTENDED_CLASS;
						return DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class, Class.class));
					}, LOGGER)
							.orElseGet(() -> Try.call(() -> {
								type[0] = EnumConstructorType.NO_ARGS;
								return DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class));
							}, LOGGER)
									.orElseThrow(ThrowableCatcher::rethrow));
			switch (type[0]) {
				case EXTENDED_CLASS:
					return (IUIExtension<?, ?>) constructor.invoke(container.getClass());
				case NO_ARGS:
					return (IUIExtension<?, ?>) constructor.invoke();
				default:
					throw new InternalError();
			}
		}

		protected enum EnumConstructorType {
			EXTENDED_CLASS,
			NO_ARGS,
		}
	}

	protected static class UIComponentPrototype
			extends ComponentBasedPrototype {
		protected final List<UIExtensionPrototype> extensions = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
		protected final List<UIComponentPrototype> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

		protected UIComponentPrototype(String className, Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping) { super(className, propertyMapping); }

		@SuppressWarnings("UnusedReturnValue")
		public boolean addChildren(Iterable<? extends UIComponentPrototype> children) { return Iterables.addAll(getChildren(), children); }

		@SuppressWarnings("UnusedReturnValue")
		public boolean addExtensions(Iterable<? extends UIExtensionPrototype> extensions) { return Iterables.addAll(getExtensions(), extensions); }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected List<UIExtensionPrototype> getExtensions() { return extensions; }

		@SuppressWarnings({"UnstableApiUsage"})
		protected IUIComponent createComponent(final List<Runnable> queue) throws Throwable {
			MethodHandle constructor = DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class, Map.class));
			IUIComponent ret = (IUIComponent) constructor.invoke(getPropertyMapping());
			queue.add(() -> {
				for (UIExtensionPrototype e : getExtensions())
					ret.addExtension(CastUtilities.castUnchecked(Try.call(() -> e.createComponent(ret), LOGGER)
							.orElseThrow(ThrowableCatcher::rethrow))); // COMMENT addExtension should check
			});
			if (!getChildren().isEmpty()) {
				if (!(ret instanceof IUIComponentContainer))
					throw BecauseOf.illegalArgument("UI component type is not a container",
							"ret.getClass()", ret.getClass(),
							"ret", ret);
				IUIComponentContainer container = (IUIComponentContainer) ret;
				container.addChildren(getChildren().stream().sequential()
						.map(c ->
								Try.call(() -> c.createComponent(queue), LOGGER)
										.orElseThrow(ThrowableCatcher::rethrow))
						.collect(ImmutableList.toImmutableList()));
			}
			return ret;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected List<UIComponentPrototype> getChildren() { return children; }
	}
}
