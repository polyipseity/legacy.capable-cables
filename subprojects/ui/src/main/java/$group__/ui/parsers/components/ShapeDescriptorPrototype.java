package $group__.ui.parsers.components;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.parsers.components.IUIDOMPrototypeParser;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilderFactory;
import $group__.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.ui.structures.shapes.interactions.ShapeConstraint;
import $group__.utilities.DOMUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.NumberUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.client.AffineTransformUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamedNodeMapMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class ShapeDescriptorPrototype
		extends ClassPrototype<Shape> {
	public static final String LOCAL_NAME = "shape";
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
	protected final Map<String, Number> attributes;
	protected final AffineTransform transform;
	protected final Iterable<IShapeConstraint> constraints;
	protected final Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping;

	protected ShapeDescriptorPrototype(String prototypeClassName,
	                                   Map<? extends String, ? extends Number> attributes,
	                                   AffineTransform transform,
	                                   Iterable<? extends IShapeConstraint> constraints,
	                                   Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping)
			throws ClassNotFoundException {
		super(prototypeClassName);
		this.attributes = ImmutableMap.copyOf(attributes);
		this.transform = (AffineTransform) transform.clone();
		this.constraints = Iterables.unmodifiableIterable(constraints);
		this.mapping = ImmutableMap.copyOf(mapping);
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static ShapeDescriptorPrototype create(Map<String, String> aliases, @Nullable String namespaceURI, Node node)
			throws ClassNotFoundException {
		// COMMENT attributes
		@Nullable NamedNodeMap nodeAs = node.getAttributes();
		assert nodeAs != null;
		Map<String, Number> attributes = ImmutableMap.copyOf(
				Maps.filterValues(
						Maps.transformValues(NamedNodeMapMap.wrap(nodeAs), a -> {
							assert a != null;
							@Nullable String av = a.getNodeValue();
							assert av != null;
							return NumberUtilities.tryParseDouble(av)
									.orElse(null);
						}),
						Objects::nonNull));

		// COMMENT transform
		AffineTransform transform = new AffineTransform();
		DOMUtilities.getChildByTagNameNS(node, namespaceURI, "transform")
				.ifPresent(nt -> {
					final double[] fm = AffineTransformUtilities.getFlatMatrixIdentityCopy();
					DOMUtilities.getChildByTagNameNS(nt, namespaceURI, "values")
							.map(Node::getAttributes)
							.ifPresent(vAs -> {
								NamedNodeMapMap.wrap(vAs)
										.forEach((s, a) -> Optional.ofNullable(TRANSFORM_INDEX_MAP.get(s))
												.ifPresent(i -> {
													@Nullable String av = a.getNodeValue();
													assert av != null;
													fm[i] = Double.parseDouble(av);
												}));
								transform.setTransform(new AffineTransform(fm));
							});
					DOMUtilities.getChildrenByTagNameNS(nt, namespaceURI, "operation")
							.forEach(op -> {
								@Nullable String opv = op.getTextContent();
								assert opv != null;
								Double[] args = Arrays.stream(
										opv.split(Pattern.quote(DOMUtilities.getAttributeValue(op, "delimiter")
												.orElseThrow(InternalError::new))))
										.map(Double::parseDouble)
										.toArray(Double[]::new);
								ThrowableUtilities.Try.run(() ->
										DynamicUtilities.IMPL_LOOKUP.findVirtual(
												AffineTransform.class,
												DOMUtilities.getAttributeValue(op, "method")
														.orElseThrow(InternalError::new),
												MethodType.methodType(void.class, Collections.nCopies(args.length, double.class)))
												.bindTo(transform)
												.invokeWithArguments((Object[]) args), LOGGER);
								ThrowableUtilities.ThrowableCatcher.rethrow(true);
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

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping = new HashMap<>(node.getChildNodes().getLength());
		IUIDOMPrototypeParser.initializeMapping(mapping, node, namespaceURI);

		return new ShapeDescriptorPrototype(
				IUIDOMPrototypeParser.getClassFromMaybeAlias(aliases, DOMUtilities.getAttributeValue(node, ClassPrototype.CLASS_ATTRIBUTE_NAME).orElseThrow(InternalError::new)),
				attributes, transform, constraints, mapping);
	}

	protected IShapeDescriptor<?> construct() {
		IShapeDescriptorBuilder<?> sdb = IShapeDescriptorBuilderFactory.getDefault().createBuilder(getPrototypeClass());

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
