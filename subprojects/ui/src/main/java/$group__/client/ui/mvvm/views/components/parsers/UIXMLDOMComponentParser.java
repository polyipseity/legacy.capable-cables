package $group__.client.ui.mvvm.views.components.parsers;

import $group__.client.ui.ConfigurationUI;
import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilderFactory;
import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.client.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.parsers.IUIResourceParser;
import $group__.client.ui.mvvm.structures.UIPropertyMappingValue;
import $group__.client.ui.structures.EnumUISide;
import $group__.client.ui.structures.shapes.interactions.ShapeConstraint;
import $group__.client.ui.structures.shapes.interactions.UIAnchor;
import $group__.utilities.*;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.client.AffineTransformUtilities;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.NamedNodeMapMap;
import com.google.common.collect.*;
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
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

// TODO could be better, not very modular
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

	@Override
	public void parseResource(Document resource) throws IOException, SAXException, ClassNotFoundException {
		SCHEMA.newValidator().validate(new DOMSource(resource));
		reset();
		Element root = resource.getDocumentElement();
		@Nullable String namespaceURI = root.getNamespaceURI();

		DOMUtilities.getChildrenByTagNameNS(root, namespaceURI, "alias").forEach(al ->
				getAliases().put(DOMUtilities.getAttributeValue(al, "name").orElseThrow(InternalError::new),
						DOMUtilities.getAttributeValue(al, "class").orElseThrow(InternalError::new)));
		Node managerNode = DOMUtilities.getChildByTagNameNS(root, namespaceURI, "component").orElseThrow(() ->
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
				n -> UIComponentPrototype.createPrototype(getAliases(), namespaceURI, n),
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
		List<Consumer<? super IUIComponentManager<?>>> queue = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		return getPrototype()
				.map(cp -> Try.call(() -> {
					IUIComponentManager<?> c = (IUIComponentManager<?>) cp.createComponent(queue); // COMMENT expect manager
					queue.forEach(q -> q.accept(c));
					return c;
				}, LOGGER)
						.orElseThrow(ThrowableCatcher::rethrow))
				.map(CastUtilities::<T>castUnchecked)
				.orElseThrow(() -> new IllegalStateException("Prototype has not been created"));
	}

	protected Optional<UIComponentPrototype> getPrototype() { return Optional.ofNullable(prototype); }

	protected void setPrototype(@Nullable UIComponentPrototype prototype) { this.prototype = prototype; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, String> getAliases() { return aliases; }

	protected static String getClassFromMaybeAlias(Map<String, String> aliases, String maybeAlias) { return aliases.getOrDefault(maybeAlias, maybeAlias); }

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

		protected static UIExtensionPrototype createPrototype(Map<String, String> aliases, @Nullable String namespaceURI, Node node) {
			Map<ResourceLocation, IUIPropertyMappingValue> m = new HashMap<>(node.getChildNodes().getLength());
			ComponentBasedPrototype.constructMapping(m, node, namespaceURI);
			return new UIExtensionPrototype(
					getClassFromMaybeAlias(aliases, DOMUtilities.getAttributeValue(node, "class").orElseThrow(InternalError::new)),
					m);
		}

		protected IUIExtension<?, ?> createComponent(IUIComponent container)
				throws Throwable {
			final EnumConstructorType[] type = new EnumConstructorType[1];
			MethodHandle constructor = // COMMENT it's almost like if-else if-else ;/
					Try.call(() -> {
						type[0] = EnumConstructorType.MAP__EXTENDED_CLASS;
						return DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class, Map.class, Class.class));
					}, LOGGER).orElseGet(() -> Try.call(() -> {
						type[0] = EnumConstructorType.MAP;
						return DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class, Map.class));
					}, LOGGER).orElseGet(() -> Try.call(() -> {
						type[0] = EnumConstructorType.EXTENDED_CLASS;
						return DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class, Class.class));
					}, LOGGER).orElseGet(() -> Try.call(() -> {
						type[0] = EnumConstructorType.NO_ARGS;
						return DynamicUtilities.IMPL_LOOKUP.findConstructor(Class.forName(getClassName()), MethodType.methodType(void.class));
					}, LOGGER).orElseThrow(ThrowableCatcher::rethrow))));
			switch (type[0]) {
				case MAP__EXTENDED_CLASS:
					return (IUIExtension<?, ?>) constructor.invoke(getPropertyMapping(), container.getClass());
				case MAP:
					return (IUIExtension<?, ?>) constructor.invoke(getPropertyMapping());
				case EXTENDED_CLASS:
					return (IUIExtension<?, ?>) constructor.invoke(container.getClass());
				case NO_ARGS:
					return (IUIExtension<?, ?>) constructor.invoke();
				default:
					throw new InternalError();
			}
		}

		protected enum EnumConstructorType {
			MAP__EXTENDED_CLASS,
			MAP,
			EXTENDED_CLASS,
			NO_ARGS,
		}
	}

	protected static class UIComponentPrototype
			extends ComponentBasedPrototype {
		private static final Logger LOGGER = LogManager.getLogger();
		protected final UIShapeDescriptorPrototype shapeDescriptorPrototype;
		protected final Iterable<Function<? super IUIComponentManager<?>, ? extends Optional<? extends IShapeAnchor>>> anchors;
		protected final List<UIExtensionPrototype> extensions = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
		protected final List<UIComponentPrototype> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

		protected UIComponentPrototype(String className,
		                               UIShapeDescriptorPrototype shapeDescriptorPrototype,
		                               Iterable<? extends Function<? super IUIComponentManager<?>, ? extends Optional<? extends IShapeAnchor>>> anchors,
		                               Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping) {
			super(className, propertyMapping);
			this.anchors = Iterables.unmodifiableIterable(anchors);
			this.shapeDescriptorPrototype = shapeDescriptorPrototype;
		}

		@SuppressWarnings("UnstableApiUsage")
		protected static UIComponentPrototype createPrototype(Map<String, String> aliases, @Nullable String namespaceURI, Node node) {
			@Nullable NamedNodeMap as = node.getAttributes();
			assert as != null;
			Map<ResourceLocation, IUIPropertyMappingValue> mapping = new HashMap<>(as.getLength() + node.getChildNodes().getLength());
			ComponentBasedPrototype.constructMapping(mapping, node, namespaceURI);

			// COMMENT adds the attributes, automatically adds the namespace 'minecraft'
			NamedNodeMapMap.wrap(as).forEach((k, v) ->
					Optional.ofNullable(v.getNodeValue())
							.ifPresent(vs -> {
								IUIPropertyMappingValue mv =
										vs.startsWith("@") ?
												new UIPropertyMappingValue(null, new ResourceLocation(vs))
												: new UIPropertyMappingValue(v, null);
								mapping.put(new ResourceLocation(k), mv);
							}));

			// COMMENT anchors
			Iterable<Function<IUIComponentManager<?>, Optional<IShapeAnchor>>> anchors =
					DOMUtilities.getChildrenByTagNameNS(node, namespaceURI, "anchor").stream().sequential()
							.map(na ->
									(Function<IUIComponentManager<?>, Optional<IShapeAnchor>>) mag ->
											IUIComponentManager.getComponentByID(mag,
													DOMUtilities.getAttributeValue(na, "target")
															.orElseThrow(InternalError::new))
													.map(ct ->
															new UIAnchor(
																	ct,
																	EnumUISide.valueOf(DOMUtilities.getAttributeValue(na, "originSide")
																			.orElseThrow(InternalError::new)),
																	EnumUISide.valueOf(DOMUtilities.getAttributeValue(na, "targetSide")
																			.orElseThrow(InternalError::new)),
																	Double.parseDouble(DOMUtilities.getAttributeValue(na, "borderThickness")
																			.orElseThrow(InternalError::new)))))
							.collect(ImmutableList.toImmutableList());

			UIComponentPrototype ret = new UIComponentPrototype(
					getClassFromMaybeAlias(aliases, DOMUtilities.getAttributeValue(node, "class").orElseThrow(InternalError::new)),
					UIShapeDescriptorPrototype.createPrototype(aliases, namespaceURI,
							DOMUtilities.getChildByTagNameNS(node, namespaceURI, "shape")
									.orElseThrow(InternalError::new)),
					anchors, mapping);
			ret.addExtensions(
					DOMUtilities.getChildrenByTagNameNS(node, namespaceURI, "extension").stream().sequential()
							.map(e -> UIExtensionPrototype.createPrototype(aliases, namespaceURI, e))
							.collect(ImmutableList.toImmutableList()));
			return ret;
		}

		@SuppressWarnings("UnusedReturnValue")
		public boolean addChildren(Iterable<? extends UIComponentPrototype> children) { return Iterables.addAll(getChildren(), children); }

		@SuppressWarnings("UnusedReturnValue")
		public boolean addExtensions(Iterable<? extends UIExtensionPrototype> extensions) { return Iterables.addAll(getExtensions(), extensions); }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected List<UIExtensionPrototype> getExtensions() { return extensions; }

		@SuppressWarnings({"UnstableApiUsage"})
		protected IUIComponent createComponent(final List<Consumer<? super IUIComponentManager<?>>> queue)
				throws Throwable {
			IUIComponent ret = (IUIComponent) DynamicUtilities.IMPL_LOOKUP
					.findConstructor(Class.forName(getClassName()),
							MethodType.methodType(void.class, IShapeDescriptor.class, Map.class))
					.invoke(getShapeDescriptorPrototype().createComponent(), getPropertyMapping());
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
			queue.add(m ->
					getExtensions().forEach(e ->
							ret.addExtension(CastUtilities.castUnchecked(Try.call(() -> e.createComponent(ret), LOGGER) // COMMENT addExtension should check
									.orElseThrow(ThrowableCatcher::rethrow)))));
			queue.add(m ->
					m.getShapeAnchorController().addAnchors(ret,
							Streams.stream(getAnchors()).sequential()
									.map(a -> a.apply(m))
									.filter(Optional::isPresent)
									.map(Optional::get)
									.collect(ImmutableList.toImmutableList())));
			return ret;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected List<UIComponentPrototype> getChildren() { return children; }

		protected UIShapeDescriptorPrototype getShapeDescriptorPrototype() { return shapeDescriptorPrototype; }

		protected Iterable<Function<? super IUIComponentManager<?>, ? extends Optional<? extends IShapeAnchor>>> getAnchors() { return anchors; }
	}

	protected static class UIShapeDescriptorPrototype
			extends ComponentBasedPrototype {
		protected static final ImmutableMap<String, BiConsumer<? super IShapeDescriptorBuilder<?>, ? super Number>> SHAPE_DESCRIPTOR_ATTRIBUTE_MAP = ImmutableMap.<String, BiConsumer<? super IShapeDescriptorBuilder<?>, ? super Number>>builder()
				.put("x", (sdb, n) -> sdb.setX(n.doubleValue()))
				.put("y", (sdb, n) -> sdb.setY(n.doubleValue()))
				.put("width", (sdb, n) -> sdb.setWidth(n.doubleValue()))
				.put("height", (sdb, n) -> sdb.setHeight(n.doubleValue()))
				.build();
		protected static final ImmutableMap<String, Integer> TRANSFORM_INDEX_MAP = ImmutableMap.<String, Integer>builder()
				.put("translateX", AffineTransformUtilities.TRANSLATE_X_INDEX)
				.put("translateY", AffineTransformUtilities.TRANSLATE_Y_INDEX)
				.put("scaleX", AffineTransformUtilities.SCALE_X_INDEX)
				.put("scaleY", AffineTransformUtilities.SCALE_Y_INDEX)
				.put("shearX", AffineTransformUtilities.SHEAR_X_INDEX)
				.put("shearY", AffineTransformUtilities.SHEAR_Y_INDEX)
				.build();
		private static final Logger LOGGER = LogManager.getLogger();
		protected final AffineTransform transform;
		protected final Map<String, Number> attributes;
		protected final Iterable<IShapeConstraint> constraints;

		protected UIShapeDescriptorPrototype(String className, Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping, AffineTransform transform, Map<? extends String, ? extends Number> attributes, Iterable<? extends IShapeConstraint> constraints) {
			super(className, propertyMapping);
			this.attributes = ImmutableMap.copyOf(attributes);
			this.transform = (AffineTransform) transform.clone();
			this.constraints = Iterables.unmodifiableIterable(constraints);
		}

		@SuppressWarnings("UnstableApiUsage")
		protected static UIShapeDescriptorPrototype createPrototype(Map<String, String> aliases, @Nullable String namespaceURI, Node node) {
			Map<ResourceLocation, IUIPropertyMappingValue> m = new HashMap<>(node.getChildNodes().getLength());
			ComponentBasedPrototype.constructMapping(m, node, namespaceURI);

			// COMMENT attributes
			@Nullable NamedNodeMap nodeAs = node.getAttributes();
			assert nodeAs != null;
			Map<String, Number> attributes = ImmutableMap.copyOf(
					Maps.filterValues(
							Maps.transformValues(NamedNodeMapMap.wrap(nodeAs), a -> {
								assert a != null;
								@Nullable String av = a.getNodeValue();
								assert av != null;
								@Nullable Number ret = null;
								try {
									ret = Double.parseDouble(av);
								} catch (NumberFormatException ex) {
									ThrowableCatcher.log(ex, LOGGER);
								}
								return ret;
							}),
							Objects::nonNull));

			// COMMENT transform
			AffineTransform transform = new AffineTransform();
			DOMUtilities.getChildByTagNameNS(node, namespaceURI, "transform")
					.ifPresent(nt -> {
						final double[] fm = AffineTransformUtilities.getMatrix3x2IdentityView();
						@Nullable NamedNodeMap vAs = DOMUtilities.getChildByTagNameNS(nt, namespaceURI, "values")
								.orElseThrow(InternalError::new).getAttributes();
						assert vAs != null;
						NamedNodeMapMap.wrap(vAs)
								.forEach((s, a) -> Optional.ofNullable(TRANSFORM_INDEX_MAP.get(s))
										.ifPresent(i -> {
											@Nullable String av = a.getNodeValue();
											assert av != null;
											fm[i] = Double.parseDouble(av);
										}));
						transform.setTransform(new AffineTransform(fm));
						DOMUtilities.getChildrenByTagNameNS(nt, namespaceURI, "operation")
								.forEach(op -> {
									@Nullable String opv = op.getTextContent();
									assert opv != null;
									Double[] args = Arrays.stream(
											opv.split(Pattern.quote(DOMUtilities.getAttributeValue(op, "delimiter")
													.orElseThrow(InternalError::new))))
											.map(Double::parseDouble)
											.toArray(Double[]::new);
									Try.run(() ->
											DynamicUtilities.IMPL_LOOKUP.findVirtual(
													AffineTransform.class,
													DOMUtilities.getAttributeValue(op, "method")
															.orElseThrow(InternalError::new),
													MethodType.methodType(void.class, Collections.nCopies(args.length, double.class)))
													.bindTo(transform)
													.invokeWithArguments((Object[]) args), LOGGER);
									ThrowableCatcher.rethrow(true);
								});
					});

			// COMMENT constraints
			Iterable<IShapeConstraint> constraints = DOMUtilities.getChildrenByTagNameNS(node, namespaceURI, "constraint").stream().sequential()
					.map(nc ->
							new ShapeConstraint(
									DOMUtilities.getAttributeValue(nc, "minX")
											.map(Double::parseDouble)
											.orElse(null),
									DOMUtilities.getAttributeValue(nc, "minY")
											.map(Double::parseDouble)
											.orElse(null),
									DOMUtilities.getAttributeValue(nc, "maxX")
											.map(Double::parseDouble)
											.orElse(null),
									DOMUtilities.getAttributeValue(nc, "maxY")
											.map(Double::parseDouble)
											.orElse(null),
									DOMUtilities.getAttributeValue(nc, "minWidth")
											.map(Double::parseDouble)
											.orElse(null),
									DOMUtilities.getAttributeValue(nc, "minHeight")
											.map(Double::parseDouble)
											.orElse(null),
									DOMUtilities.getAttributeValue(nc, "maxWidth")
											.map(Double::parseDouble)
											.orElse(null),
									DOMUtilities.getAttributeValue(nc, "maxHeight")
											.map(Double::parseDouble)
											.orElse(null))
					)
					.collect(ImmutableList.toImmutableList());

			return new UIShapeDescriptorPrototype(
					getClassFromMaybeAlias(aliases, DOMUtilities.getAttributeValue(node, "class").orElseThrow(InternalError::new)),
					m,
					transform, attributes, constraints);
		}

		protected IShapeDescriptor<?> createComponent()
				throws Throwable {
			@SuppressWarnings("unchecked") IShapeDescriptorBuilder<?> sdb = IShapeDescriptorBuilderFactory.getDefault()
					.createBuilder((Class<? extends Shape>) Class.forName(getClassName()));

			getAttributes().forEach((s, n) ->
					SHAPE_DESCRIPTOR_ATTRIBUTE_MAP.getOrDefault(s, (sdbD, nD) ->
							sdbD.setProperty(s, nD))
							.accept(sdb, n));

			return sdb.transformConcatenate(getTransform())
					.constrain(getConstraints())
					.build();
		}

		protected Map<String, Number> getAttributes() { return attributes; }

		protected AffineTransform getTransform() { return transform; }

		protected Iterable<IShapeConstraint> getConstraints() { return constraints; }
	}
}
