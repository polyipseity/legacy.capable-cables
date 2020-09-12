package $group__.ui.mvvm.views.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventBusUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UIComponentContainer
		extends UIComponent
		implements IUIComponentContainer {
	protected final List<IUIComponent> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentContainer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<?> shapeDescriptor) { super(mappings, id, shapeDescriptor); }

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

	@Override
	public boolean addChildAt(int index, IUIComponent component) {
		if (equals(component))
			throw ThrowableUtilities.BecauseOf.illegalArgument("Adding self as child", "component", component);
		if (getChildren().contains(component))
			return moveChildTo(index, component);
		boolean ret = false;
		if (getParent().map(p -> p.removeChildren(ImmutableList.of(component))).orElse(true)) {
			ret = EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
						getChildren().add(index, component);
						List<IUIComponent> childrenMoved = getChildren().subList(index + 1, getChildren().size());
						for (int i = 0; i < childrenMoved.size(); i++)
							AssertionUtilities.assertNonnull(childrenMoved.get(i)).onIndexMove(index + i, index + i + 1);
						component.onParentChange(null, this);
						return true;
					},
					new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.PRE, component, null, this),
					new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.POST, component, null, this));
		}
		if (ret)
			IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return ret;
	}

	@SuppressWarnings({"ObjectAllocationInLoop"})
	@Override
	public boolean removeChildren(Iterable<? extends IUIComponent> components) {
		boolean ret = false;
		for (IUIComponent component : components) {
			int index = getChildren().indexOf(component);
			if (index != -1) {
				ret |= EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
							getChildren().remove(component);
							List<IUIComponent> childrenMoved = getChildren().subList(index, getChildren().size());
							for (int i = 0; i < childrenMoved.size(); i++)
								AssertionUtilities.assertNonnull(childrenMoved.get(i)).onIndexMove(index + i + 1, index + i);
							component.onParentChange(this, null);
							return true;
						},
						new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.PRE, component, this, null),
						new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.POST, component, this, null));
			}
		}
		if (ret)
			IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return ret;
	}

	@Override
	public boolean moveChildTo(int index, IUIComponent component) {
		int previous = getChildren().indexOf(component);
		if (previous == index)
			return false;
		return getChildren().contains(component) && EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
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
					return true;
				},
				new EventUIComponentHierarchyChanged.Index(EnumEventHookStage.PRE, this, previous, index),
				new EventUIComponentHierarchyChanged.Index(EnumEventHookStage.POST, this, previous, index));
	}

	@Override
	public boolean moveChildToTop(IUIComponent component) { return moveChildTo(getChildren().size() - 1, component); }

	@Override
	public List<IUIComponent> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIComponent> getChildren() { return children; }
}
