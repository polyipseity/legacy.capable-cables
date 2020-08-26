package $group__.ui.parsers.components;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.parsers.components.IGeneralPrototype;
import $group__.ui.core.parsers.components.IUIDOMPrototypeParser;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.utilities.CastUtilities;
import $group__.utilities.DOMUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import org.w3c.dom.Node;

import java.util.*;
import java.util.function.Consumer;

public class UIExtensionPrototype
		extends ClassConstructorPrototype<IUIExtension<?, ?>, UIExtensionConstructor.ConstructorType>
		implements IGeneralPrototype {
	public static final String LOCAL_NAME = "extension";
	public static final INamespacePrefixedString LOCAL_NAME_LOCATION = new NamespacePrefixedString(UIDOMPrototypeParser.SCHEMA_NAMESPACE_URI, LOCAL_NAME);
	protected final Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping;

	protected UIExtensionPrototype(String prototypeClassName, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
		super(prototypeClassName,
				c -> Arrays.stream(c.getDeclaredConstructors()).unordered()
						.map(cc -> cc.getAnnotation(UIExtensionConstructor.class))
						.filter(Objects::nonNull)
						.findAny()
						.orElseThrow(() ->
								ThrowableUtilities.BecauseOf.illegalArgument("Cannot find any constructor annotated with 'UIExtensionConstructor'",
										"c.getDeclaredConstructors()", c.getDeclaredConstructors(),
										"c", c)).type());

		this.mapping = ImmutableMap.copyOf(mapping);
	}

	protected static IGeneralPrototype create(IUIDOMPrototypeParser<?> parser, Node node)
			throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping = new HashMap<>(node.getChildNodes().getLength());
		IUIDOMPrototypeParser.initializeMapping(mapping, node, parser.getMainNamespaceURI());
		return new UIExtensionPrototype(
				IUIDOMPrototypeParser.getClassFromMaybeAlias(parser.getAliasesView(), DOMUtilities.getAttributeValue(node, CLASS_ATTRIBUTE_NAME).orElseThrow(InternalError::new)),
				mapping);
	}

	@Override
	public void construct(List<Consumer<? super IUIComponentManager<?>>> queue, IUIComponent container)
			throws Throwable {
		IUIExtension<?, ?> ret;
		switch (getConstructorType()) {
			case MAPPING__CONTAINER_CLASS:
				ret = (IUIExtension<?, ?>) getConstructor().invoke(getMapping(), container.getClass());
				break;
			case MAPPING:
				ret = (IUIExtension<?, ?>) getConstructor().invoke(getMapping());
				break;
			case CONTAINER_CLASS:
				ret = (IUIExtension<?, ?>) getConstructor().invoke(container.getClass());
				break;
			case NO_ARGS:
				ret = (IUIExtension<?, ?>) getConstructor().invoke();
				break;
			default:
				throw new InternalError();
		}
		queue.add(m ->
				container.addExtension(CastUtilities.castUnchecked(ret))); // COMMENT addExtension should check
	}

	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMapping() { return mapping; }
}
