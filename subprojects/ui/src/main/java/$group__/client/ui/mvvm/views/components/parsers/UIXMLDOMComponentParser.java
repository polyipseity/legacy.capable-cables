package $group__.client.ui.mvvm.views.components.parsers;

import $group__.client.ui.ConfigurationUI;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.parsers.IUIResourceParser;
import $group__.client.ui.mvvm.structures.UIPropertyMappingValue;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.DOMUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.specific.ThrowableUtilities.Try;
import $group__.utilities.structures.NodeListList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class UIXMLDOMComponentParser<T extends IUIComponentManager>
		extends IHasGenericClass.Impl<T>
		implements IUIResourceParser<T, Document> {
	@SuppressWarnings("HardcodedFileSeparator")
	public static final ResourceLocation SCHEMA_LOCATION = new ResourceLocation(ConfigurationUI.getModId(), "ui/schemas/components.xsd");
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Schema SCHEMA = Try.call(() ->
			SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
					.newSchema(new StreamSource(ResourceUtilities.getResource(SCHEMA_LOCATION).getInputStream())), LOGGER).orElseThrow(ThrowableCatcher::rethrow);

	protected ConcurrentMap<String, String> aliases = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	@Nullable
	protected UIComponentPrototype prototype;

	public UIXMLDOMComponentParser(Class<T> genericClass) { super(genericClass); }

	@Override
	public void parseResource(Document resource) throws IOException, SAXException, ClassNotFoundException {
		SCHEMA.newValidator().validate(new DOMSource(resource));
		reset();
		Element root = resource.getDocumentElement();
		@Nullable String namespaceURI = root.getNamespaceURI();

		DOMUtilities.getChildrenByTagNameNS(root, namespaceURI, "alias").forEach(a ->
				getAliases().put(a.getAttributes().getNamedItem("name").getNodeValue(), a.getAttributes().getNamedItem("class").getNodeValue()));
		Node managerNode = DOMUtilities.getChildrenByTagNameNS(root, namespaceURI, "component").stream().unordered().findAny().orElseThrow(() ->
				BecauseOf.illegalArgument("Component manager not found",
						"resource", resource));

		{
			String managerClassName = getClassFromMaybeAlias(getAliases(), managerNode.getAttributes().getNamedItem("class").getNodeValue());
			Class<?> managerClass = Class.forName(managerClassName);
			if (!getGenericClass().equals(managerClass))
				throw BecauseOf.illegalArgument("Component manager type does not match",
						"managerClass", managerClass,
						"getGenericClass()", getGenericClass(),
						"resource", resource);
		}

		setPrototype(TreeUtilities.visitNodesDepthFirst(managerNode,
				n -> {
					NodeListList i = NodeListList.wrap(n.getChildNodes());
					@SuppressWarnings("MismatchedQueryAndUpdateOfCollection") Map<String, IUIPropertyMappingValue> propertyMapping = new HashMap<>(i.size() - 1);
					DOMUtilities.getChildrenByTagNameNS(n, namespaceURI, "property").forEach(p ->
							propertyMapping.put(p.getAttributes().getNamedItem("key").getNodeValue(),
									new UIPropertyMappingValue(Optional.ofNullable(p.getAttributes().getNamedItem("value"))
											.orElseThrow(InternalError::new).getNodeValue(), null)));
					DOMUtilities.getChildrenByTagNameNS(n, namespaceURI, "binding").forEach(p ->
							propertyMapping.put(p.getAttributes().getNamedItem("key").getNodeValue(),
									new UIPropertyMappingValue(Optional.ofNullable(p.getAttributes().getNamedItem("value"))
											.map(Node::getNodeValue).orElse(null),
											Optional.ofNullable(p.getAttributes().getNamedItem("binding"))
													.orElseThrow(InternalError::new).getNodeValue())));
					return new UIComponentPrototype(
							getClassFromMaybeAlias(getAliases(), n.getAttributes().getNamedItem("class").getNodeValue()),
							propertyMapping);
				},
				n -> NodeListList.wrap(
						DOMUtilities.getChildrenByTagNameNS(n, namespaceURI, "componentContainer").stream().unordered()
								.findAny()
								.orElseThrow(() ->
										BecauseOf.illegalArgument("Component container not found",
												"n", n,
												"resource", resource)).getChildNodes())
				,
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
		return getPrototype()
				.map(c -> Try.call(c::createComponent, LOGGER).orElseThrow(ThrowableCatcher::rethrow))
				.map(c -> CastUtilities.castChecked(getGenericClass(), c, LOGGER).orElseThrow(ThrowableCatcher::rethrow))
				.orElseThrow(() -> new IllegalStateException("Prototype has not been created"));
	}

	protected Optional<UIComponentPrototype> getPrototype() { return Optional.ofNullable(prototype); }

	protected void setPrototype(@Nullable UIComponentPrototype prototype) { this.prototype = prototype; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, String> getAliases() { return aliases; }

	protected static String getClassFromMaybeAlias(ConcurrentMap<String, String> aliases, String maybeAlias) { return aliases.getOrDefault(maybeAlias, maybeAlias); }

	protected static class UIComponentPrototype {
		private static final Logger LOGGER = LogManager.getLogger();
		protected final String className;
		protected final Map<String, IUIPropertyMappingValue> propertyMapping;
		protected final List<UIComponentPrototype> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

		public UIComponentPrototype(String className, Map<String, IUIPropertyMappingValue> propertyMapping) {
			this.className = className;
			this.propertyMapping = ImmutableMap.copyOf(propertyMapping);
		}

		public String getClassName() { return className; }

		public boolean addChildren(Iterable<? extends UIComponentPrototype> children) { return Iterables.addAll(getChildren(), children); }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected List<UIComponentPrototype> getChildren() { return children; }

		protected Map<String, IUIPropertyMappingValue> getPropertyMapping() { return propertyMapping; }

		public IUIComponent createComponent() throws Throwable {
			MethodHandle constructor = DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class, Map.class));
			IUIComponent ret = (IUIComponent) constructor.invoke(getPropertyMapping());
			if (!getChildren().isEmpty()) {
				if (!(ret instanceof IUIComponentContainer))
					throw BecauseOf.illegalArgument("Component type is not a container",
							"ret.getClass()", ret.getClass(),
							"ret", ret);
				IUIComponentContainer container = (IUIComponentContainer) ret;
				getChildren().stream().sequential()
						.map(c -> Try.call(c::createComponent, LOGGER).orElseThrow(ThrowableCatcher::rethrow))
						.forEachOrdered(c -> container.addChildren(ImmutableSet.of(c)));
			}
			return ret;
		}
	}
}
