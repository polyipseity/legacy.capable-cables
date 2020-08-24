package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.client.ui.events.bus.UIEventBusEntryPoint;
import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.core.Observer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIComponentContainer
		extends UIComponent
		implements IUIComponentContainer {
	protected final List<IUIComponent> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

	public UIComponentContainer(IShapeDescriptor<?> shapeDescriptor, Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping) { super(shapeDescriptor, propertyMapping); }

	@Override
	public void transformChildren(IAffineTransformStack stack) {
		Rectangle2D bounds = getShapeDescriptor().getShapeOutput().getBounds2D();
		stack.getDelegated().peek().translate(bounds.getX(), bounds.getY());
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
			ret = EventUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
						getChildren().add(index, component);
						List<IUIComponent> childrenMoved = getChildren().subList(index + 1, getChildren().size());
						for (int i = 0; i < childrenMoved.size(); i++) {
							@Nullable IUIComponent cm = childrenMoved.get(i);
							assert cm != null;
							cm.onIndexMove(index + i, index + i + 1);
						}
						component.onParentChange(null, this);
						return true;
					},
					new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.PRE, this, null, this),
					new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.POST, this, null, this));
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
				ret |= EventUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
							getChildren().remove(component);
							List<IUIComponent> childrenMoved = getChildren().subList(index, getChildren().size());
							for (int i = 0; i < childrenMoved.size(); i++) {
								@Nullable IUIComponent cm = childrenMoved.get(i);
								assert cm != null;
								cm.onIndexMove(index + i + 1, index + i);
							}
							component.onParentChange(this, null);
							return true;
						},
						new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.PRE, this, this, null),
						new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.POST, this, this, null));
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
		return getChildren().contains(component) && EventUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
					getChildren().remove(previous);
					getChildren().add(index, component); // COMMENT cast is always safe
					List<IUIComponent> childrenMoved;
					if (index > previous) {
						childrenMoved = getChildren().subList(previous, index);
						for (int i = 0; i < childrenMoved.size(); i++) {
							@Nullable IUIComponent cm = childrenMoved.get(i);
							assert cm != null;
							cm.onIndexMove(previous + i + 1, previous + i);
						}
					} else {
						childrenMoved = getChildren().subList(index + 1, previous + 1);
						for (int i = 0; i < childrenMoved.size(); i++) {
							@Nullable IUIComponent cm = childrenMoved.get(i);
							assert cm != null;
							cm.onIndexMove(index + i, index + i + 1);
						}
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

	@Override
	public Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() {
		return s -> {
			super.getBinderSubscriber().accept(s);
			getChildren().forEach(c ->
					c.getBinderSubscriber().accept(s));
		};
	}
}
