package $group__.ui.parsers.components;

import $group__.ui.ConfigurationUI;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.parsers.components.IGeneralPrototype;
import $group__.ui.core.parsers.components.IUIDOMPrototypeParser;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.TreeUtilities;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.dom.DOMUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class UIDOMPrototypeParser<T extends IUIComponentManager<?>>
		extends UIDOMParser<T>
		implements IUIDOMPrototypeParser<T> {
	@SuppressWarnings("HardcodedFileSeparator")
	public static final INamespacePrefixedString SCHEMA_LOCATION = new NamespacePrefixedString(ConfigurationUI.getModId(), "ui/schemas/components.xsd");
	public static final Schema SCHEMA;
	public static final String SCHEMA_NAMESPACE_URI = "https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/schemas/ui/components";
	private static final Logger LOGGER = LogManager.getLogger();

	static {
		SCHEMA = Try.call(() -> {
			try (InputStream res = ResourceUtilities.getResource(NamespaceUtilities.toResourceLocation(SCHEMA_LOCATION)).getInputStream()) {
				return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(res));
			}
		}, LOGGER)
				.orElseThrow(ThrowableCatcher::rethrow);
	}

	protected final ConcurrentMap<INamespacePrefixedString, BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype>> visitors =
			MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<String, String> aliases = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	@Nullable
	protected UIComponentPrototype prototype;

	public UIDOMPrototypeParser(Class<T> managerClass) {
		super(managerClass, SCHEMA_NAMESPACE_URI, SCHEMA);
	}

	public static <T extends IUIDOMPrototypeParser<?>> T makeParserStandard(T parser) {
		parser.putVisitor(ShapeAnchorPrototype.LOCAL_NAME_LOCATION, ShapeAnchorPrototype::create);
		parser.putVisitor(UIRendererPrototype.LOCAL_NAME_LOCATION, (p, n) ->
				Try.call(() -> UIRendererPrototype.create(p, n), LOGGER)
						.orElseThrow(ThrowableCatcher::rethrow));
		parser.putVisitor(UIExtensionPrototype.LOCAL_NAME_LOCATION, (p, n) ->
				Try.call(() -> UIExtensionPrototype.create(p, n), LOGGER)
						.orElseThrow(ThrowableCatcher::rethrow));
		return parser;
	}

	@Override
	public Map<INamespacePrefixedString, BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype>> getVisitorsView() { return ImmutableMap.copyOf(getVisitors()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype>> getVisitors() { return visitors; }

	@Override
	public Optional<? extends BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype>> putVisitor(INamespacePrefixedString localName,
	                                                                                                                              BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype> visitor) {
		return Optional.ofNullable(getVisitors().put(localName, visitor));
	}

	@Override
	public Map<String, String> getAliasesView() { return ImmutableMap.copyOf(getAliases()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, String> getAliases() { return aliases; }

	@Override
	public void parse(Document resource)
			throws Throwable {
		super.parse(resource);
		reset();
		Element root = resource.getDocumentElement();

		DOMUtilities.getChildrenByTagNameNS(root, getMainNamespaceURI(), "using").forEach(al ->
				getAliases().put(DOMUtilities.getAttributeValue(al, "alias").orElseThrow(InternalError::new),
						DOMUtilities.getAttributeValue(al, ClassPrototype.CLASS_ATTRIBUTE_NAME).orElseThrow(InternalError::new)));
		Node managerNode = DOMUtilities.getChildByTagNameNS(root, getMainNamespaceURI(), UIComponentPrototype.LOCAL_NAME).orElseThrow(() ->
				BecauseOf.illegalArgument("Component manager not found",
						"resource", resource));

		{
			String managerClassName = IUIDOMPrototypeParser.getClassFromMaybeAlias(getAliases(),
					DOMUtilities.getAttributeValue(managerNode, ClassPrototype.CLASS_ATTRIBUTE_NAME)
							.orElseThrow(InternalError::new));
			Class<?> managerClass = Class.forName(managerClassName);
			if (!getGenericClass().equals(managerClass))
				throw BecauseOf.illegalArgument("Component manager type does not match",
						"managerClass", managerClass,
						"getGenericClass()", getGenericClass(),
						"resource", resource);
		}

		setPrototype(TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, managerNode,
				n -> Try.call(() ->
						UIComponentPrototype.create(this, n), LOGGER)
						.orElseThrow(ThrowableCatcher::rethrow),
				n -> DOMUtilities.getChildrenByTagNameNS(n, getMainNamespaceURI(), UIComponentPrototype.LOCAL_NAME),
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

	@SuppressWarnings("unchecked")
	@Override
	public T construct() {
		List<Consumer<? super IUIComponentManager<?>>> queue = new LinkedList<>();
		return getPrototype()
				.map(cp ->
						(T) Try.call(() -> {
							IUIComponentManager<?> c = (IUIComponentManager<?>) cp.construct(queue); // COMMENT expect manager
							queue.forEach(q ->
									q.accept(c));
							return c;
						}, LOGGER).orElseThrow(ThrowableCatcher::rethrow))
				.orElseThrow(() -> new IllegalStateException("Prototype has not been created"));
	}

	protected Optional<? extends UIComponentPrototype> getPrototype() { return Optional.ofNullable(prototype); }

	protected void setPrototype(@Nullable UIComponentPrototype prototype) { this.prototype = prototype; }
}
