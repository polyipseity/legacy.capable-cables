package $group__.ui.parsers.components;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRenderer;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.components.IGeneralPrototype;
import $group__.ui.core.parsers.components.IUIDOMPrototypeParser;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.utilities.CastUtilities;
import $group__.utilities.DOMUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import org.w3c.dom.Node;

import java.util.*;
import java.util.function.Consumer;

public class UIRendererPrototype
		extends ClassConstructorPrototype<IUIComponentRenderer<?>, UIRendererConstructor.ConstructorType>
		implements IGeneralPrototype {
	public static final String LOCAL_NAME = "renderer";
	public static final INamespacePrefixedString LOCAL_NAME_LOCATION = new NamespacePrefixedString(UIDOMPrototypeParser.SCHEMA_NAMESPACE_URI, LOCAL_NAME);
	protected final Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping;

	protected UIRendererPrototype(String prototypeClassName, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
		super(prototypeClassName,
				c -> Arrays.stream(c.getDeclaredConstructors()).unordered()
						.map(cc -> cc.getAnnotation(UIRendererConstructor.class))
						.filter(Objects::nonNull)
						.findAny()
						.orElseThrow(() ->
								ThrowableUtilities.BecauseOf.illegalArgument("Cannot find any constructor annotated with 'UIRendererConstructor'",
										"c.getDeclaredConstructors()", c.getDeclaredConstructors(),
										"c", c)).type());

		this.mapping = ImmutableMap.copyOf(mapping);
	}

	protected static IGeneralPrototype create(IUIDOMPrototypeParser<?> parser, Node node)
			throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping = new HashMap<>(node.getChildNodes().getLength());
		IUIDOMPrototypeParser.initializeMapping(mapping, node, parser.getMainNamespaceURI());
		return new UIRendererPrototype(
				IUIDOMPrototypeParser.getClassFromMaybeAlias(parser.getAliasesView(), DOMUtilities.getAttributeValue(node, CLASS_ATTRIBUTE_NAME).orElseThrow(InternalError::new)),
				mapping);
	}

	@Override
	public void construct(List<Consumer<? super IUIComponentManager<?>>> queue, IUIComponent container)
			throws Throwable {
		IUIComponentRenderer<?> ret;
		switch (getConstructorType()) {
			case MAPPING__CONTAINER_CLASS:
				ret = (IUIComponentRenderer<?>) getConstructor().invoke(getMapping(), container.getClass());
				break;
			case MAPPING:
				ret = (IUIComponentRenderer<?>) getConstructor().invoke(getMapping());
				break;
			case CONTAINER_CLASS:
				ret = (IUIComponentRenderer<?>) getConstructor().invoke(container.getClass());
				break;
			case NO_ARGS:
				ret = (IUIComponentRenderer<?>) getConstructor().invoke();
				break;
			default:
				throw new InternalError();
		}
		if (container instanceof IUIComponentRendererContainer)
			((IUIComponentRendererContainer<?>) container).setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
		else
			throw new IllegalStateException("Cannot set renderer as container class '" + container.getClass() + "' is not an instance of 'IUIComponentRendererContainer'");
	}

	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMapping() { return mapping; }
}
