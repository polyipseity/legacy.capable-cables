package $group__.ui.parsers.components;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.parsers.components.IGeneralPrototype;
import $group__.ui.core.parsers.components.IUIDOMPrototypeParser;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.ui.structures.EnumUISide;
import $group__.ui.structures.shapes.interactions.ShapeAnchor;
import $group__.utilities.DOMUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableSet;
import org.w3c.dom.Node;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ShapeAnchorPrototype
		implements IGeneralPrototype {
	public static final String LOCAL_NAME = "anchor";
	public static final INamespacePrefixedString LOCAL_NAME_LOCATION = new NamespacePrefixedString(UIDOMPrototypeParser.SCHEMA_NAMESPACE_URI, LOCAL_NAME);
	protected final String target;
	protected final Function<? super IUIComponent, ? extends IShapeAnchor> generator;

	protected ShapeAnchorPrototype(String target, Function<? super IUIComponent, ? extends IShapeAnchor> generator) {
		this.target = target;
		this.generator = generator;
	}

	protected static IGeneralPrototype create(@SuppressWarnings("unused") IUIDOMPrototypeParser<?> parser, Node node) {
		return new ShapeAnchorPrototype(
				DOMUtilities.getAttributeValue(node, "target")
						.orElseThrow(InternalError::new),
				t -> new ShapeAnchor(t,
						EnumUISide.valueOf(DOMUtilities.getAttributeValue(node, "originSide")
								.orElseThrow(InternalError::new)),
						EnumUISide.valueOf(DOMUtilities.getAttributeValue(node, "targetSide")
								.orElseThrow(InternalError::new)),
						Double.parseDouble(DOMUtilities.getAttributeValue(node, "borderThickness")
								.orElseThrow(InternalError::new))));
	}

	@Override
	public void construct(List<Consumer<? super IUIComponentManager<?>>> queue, IUIComponent container) {
		queue.add(m ->
				IUIComponentManager.getComponentByID(m, getTarget())
						.ifPresent(t ->
								m.getShapeAnchorController().addAnchors(container,
										ImmutableSet.of(getGenerator().apply(t)))));
	}

	protected String getTarget() { return target; }

	protected Function<? super IUIComponent, ? extends IShapeAnchor> getGenerator() { return generator; }
}
