package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIComponentHierarchyChangedBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class UIComponentContainer
		extends UIComponent
		implements IUIComponentContainer {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	@Override
	public boolean addChildAt(int index, IUIComponent component) {
		if (equals(component))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIComponent)
							.addKeyValue("index", index).addKeyValue("component", component)
							.addMessages(() -> getResourceBundle().getString("children.add.self"))
							.build()
			);
		if (getChildren().contains(component))
			return moveChildTo(index, component);
		component.getParent()
				.ifPresent(p -> p.removeChildren(ImmutableList.of(component)));
		EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
					getChildren().add(index, component);
					List<IUIComponent> childrenMoved = getChildren().subList(index + 1, getChildren().size());
					for (int i = 0; i < childrenMoved.size(); i++)
						AssertionUtilities.assertNonnull(childrenMoved.get(i)).onIndexMove(index + i, index + i + 1);
					component.onParentChange(null, this);
				},
				new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.PRE, component, null, this),
				new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.POST, component, null, this));
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return true;
	}

	protected final List<IUIComponent> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__NAME__SHAPE_DESCRIPTOR)
	public UIComponentContainer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String name, IShapeDescriptor<?> shapeDescriptor) { super(mappings, name, shapeDescriptor); }

	@Override
	public void transformChildren(IAffineTransformStack stack) {
		Rectangle2D bounds = getShapeDescriptor().getShapeOutput().getBounds2D();
		stack.element().translate(bounds.getX(), bounds.getY());
	}

	@Override
	public boolean addChildren(Iterable<? extends IUIComponent> components) {
		boolean ret = false;
		for (IUIComponent component : components)
			ret |= !getChildren().contains(component) && addChildAt(getChildren().size(), component);
		return ret;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@SuppressWarnings({"ObjectAllocationInLoop"})
	@Override
	public boolean removeChildren(Iterable<? extends IUIComponent> components) {
		boolean ret = false;
		for (IUIComponent component : components) {
			int index = getChildren().indexOf(component);
			if (index != -1) {
				EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
							getChildren().remove(component);
							List<IUIComponent> childrenMoved = getChildren().subList(index, getChildren().size());
							for (int i = 0; i < childrenMoved.size(); i++)
								AssertionUtilities.assertNonnull(childrenMoved.get(i)).onIndexMove(index + i + 1, index + i);
							component.onParentChange(this, null);
						},
						new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.PRE, component, this, null),
						new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.POST, component, this, null));
				ret = true;
			}
		}
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return ret;
	}

	@Override
	public boolean moveChildTo(int index, IUIComponent component) {
		int previous = getChildren().indexOf(component);
		if (previous == index || previous == -1)
			return false;
		EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
					getChildren().remove(previous);
					getChildren().add(index, component); // COMMENT cast is always safe
					List<IUIComponent> childrenMoved;
					if (index > previous) {
						childrenMoved = getChildren().subList(previous, index);
						for (int i = 0; i < childrenMoved.size(); i++)
							AssertionUtilities.assertNonnull(childrenMoved.get(i)).onIndexMove(previous + i + 1, previous + i);
					} else {
						childrenMoved = getChildren().subList(index + 1, previous + 1);
						for (int i = 0; i < childrenMoved.size(); i++)
							AssertionUtilities.assertNonnull(childrenMoved.get(i)).onIndexMove(index + i, index + i + 1);
					}
					component.onIndexMove(previous, index);
				},
				new UIComponentHierarchyChangedBusEvent.Index(EnumHookStage.PRE, this, previous, index),
				new UIComponentHierarchyChangedBusEvent.Index(EnumHookStage.POST, this, previous, index));
		return true;
	}

	@Override
	public boolean moveChildToTop(IUIComponent component) { return moveChildTo(getChildren().size() - 1, component); }

	@Override
	public List<IUIComponent> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIComponent> getChildren() { return children; }
}
