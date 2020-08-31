package $group__.ui.parsers.components;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.parsers.components.IGeneralPrototype;
import $group__.ui.core.parsers.components.IUIDOMPrototypeParser;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.mvvm.structures.UIPropertyMappingValue;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.DOMUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamedNodeMapMap;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.*;
import java.util.function.Consumer;

public class UIComponentPrototype
		extends ClassConstructorPrototype<IUIComponent, UIComponentConstructor.ConstructorType> {
	public static final String LOCAL_NAME = "component";
	private static final Logger LOGGER = LogManager.getLogger();
	protected final ShapeDescriptorPrototype shapeDescriptorPrototype;
	protected final Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping;
	protected final List<IGeneralPrototype> prototypes;
	protected final List<UIComponentPrototype> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

	protected UIComponentPrototype(String prototypeClassName,
	                               ShapeDescriptorPrototype shapeDescriptorPrototype,
	                               Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping,
	                               List<IGeneralPrototype> prototypes) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
		super(prototypeClassName,
				c -> Arrays.stream(c.getDeclaredConstructors()).unordered()
						.map(cc -> cc.getAnnotation(UIComponentConstructor.class))
						.filter(Objects::nonNull)
						.findAny()
						.orElseThrow(() ->
								ThrowableUtilities.BecauseOf.illegalArgument("Cannot find any constructor annotated with 'UIComponentConstructor'",
										"c.getDeclaredConstructors()", c.getDeclaredConstructors(),
										"c", c)).type());
		this.shapeDescriptorPrototype = shapeDescriptorPrototype;
		this.mapping = ImmutableMap.copyOf(mapping);
		this.prototypes = ImmutableList.copyOf(prototypes);
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static UIComponentPrototype create(IUIDOMPrototypeParser<?> parser, Node node)
			throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
		Map<String, String> aliases = parser.getAliasesView();

		NamedNodeMap as = AssertionUtilities.assertNonnull(node.getAttributes());
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping = new HashMap<>(as.getLength() + node.getChildNodes().getLength());
		IUIDOMPrototypeParser.initializeMapping(mapping, node, parser.getMainNamespaceURI());

		// COMMENT attributes
		// COMMENT automatically adds the namespace 'minecraft'
		NamedNodeMapMap.wrap(as).forEach((k, v) ->
				Optional.ofNullable(v.getNodeValue())
						.ifPresent(vs -> {
							IUIPropertyMappingValue mv =
									vs.startsWith("@") ?
											new UIPropertyMappingValue(null, new NamespacePrefixedString(vs))
											: new UIPropertyMappingValue(v, null);
							mapping.put(new NamespacePrefixedString(k), mv);
						}));

		// COMMENT prototypes
		List<IGeneralPrototype> prototypes = new LinkedList<>();
		parser.getVisitorsView().forEach((k, v) ->
				prototypes.addAll(
						DOMUtilities.getChildrenByTagNameNS(node, k.getNamespace(), k.getPath()).stream().sequential()
								.map(n -> ThrowableUtilities.Try.call(() ->
										v.apply(parser, n), LOGGER)
										.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow))
								.collect(ImmutableList.toImmutableList())));

		return new UIComponentPrototype(
				IUIDOMPrototypeParser.getClassFromMaybeAlias(aliases, DOMUtilities.getAttributeValue(node, CLASS_ATTRIBUTE_NAME).orElseThrow(InternalError::new)),
				ShapeDescriptorPrototype.create(aliases, parser.getMainNamespaceURI(),
						DOMUtilities.getChildByTagNameNS(node, parser.getMainNamespaceURI(), ShapeDescriptorPrototype.LOCAL_NAME)
								.orElseThrow(InternalError::new)),
				mapping, prototypes);
	}

	@SuppressWarnings("UnusedReturnValue")
	public boolean addChildren(Iterable<? extends UIComponentPrototype> children) { return Iterables.addAll(getChildren(), children); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<UIComponentPrototype> getChildren() { return children; }

	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMapping() { return mapping; }

	protected ShapeDescriptorPrototype getShapeDescriptorPrototype() { return shapeDescriptorPrototype; }

	protected List<IGeneralPrototype> getPrototypes() { return prototypes; }

	@SuppressWarnings({"SwitchStatementWithTooFewBranches", "UnstableApiUsage"})
	public IUIComponent construct(List<Consumer<? super IUIComponentManager<?>>> queue)
			throws Throwable {
		IUIComponent ret;
		switch (getConstructorType()) {
			case MAPPING__SHAPE_DESCRIPTOR:
				ret = (IUIComponent) getConstructor().invoke(getMapping(), getShapeDescriptorPrototype().construct());
				break;
			default:
				throw new InternalError();
		}
		if (!getChildren().isEmpty()) {
			if (!(ret instanceof IUIComponentContainer))
				throw ThrowableUtilities.BecauseOf.illegalArgument("UI component type is not a container",
						"ret.getClass()", ret.getClass(),
						"ret", ret);
			IUIComponentContainer container = (IUIComponentContainer) ret;
			container.addChildren(getChildren().stream().sequential()
					.map(c ->
							ThrowableUtilities.Try.call(() -> c.construct(queue), LOGGER)
									.orElseThrow(ThrowableUtilities.ThrowableCatcher::rethrow))
					.collect(ImmutableList.toImmutableList()));
		}
		for (IGeneralPrototype p : getPrototypes())
			p.construct(queue, ret);
		return ret;
	}
}
