package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIComponentHierarchyChangedBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UIComponentContainer
		extends UIComponent
		implements IUIComponentContainer {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	protected final List<IUIComponent> children = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

	@UIComponentConstructor
	public UIComponentContainer(UIComponentConstructor.IArguments arguments) { super(arguments); }

	@Override
	public void transformChildren(AffineTransform transform) {
		Rectangle2D bounds = getShapeDescriptor().getShapeOutput().getBounds2D();
		transform.translate(bounds.getX(), bounds.getY());
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean addChildren(Iterable<? extends IUIComponent> components) {
		return Streams.stream(components).sequential()
				.map(component -> !getChildren().contains(component) && addChildAt(getChildren().size(), component))
				.reduce(false, Boolean::logicalOr);
	}

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
					component.onParentChange(null, this);
				},
				new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.PRE, component, null, this),
				new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.POST, component, null, this));
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return true;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public boolean removeChildren(Iterable<? extends IUIComponent> components) {
		@SuppressWarnings("UnstableApiUsage") boolean ret = Streams.stream(components)
				.map(component -> {
					int index = getChildren().indexOf(component);
					if (index != -1) {
						EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
									getChildren().remove(component);
									component.onParentChange(this, null);
								},
								new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.PRE, component, this, null),
								new UIComponentHierarchyChangedBusEvent.Parent(EnumHookStage.POST, component, this, null));
						return true;
					}
					return false;
				})
				.reduce(false, Boolean::logicalOr);
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return ret;
	}

	@Override
	public boolean moveChildTo(int index, IUIComponent component) {
		int previous = getChildren().indexOf(component);
		if (previous == index || previous == -1)
			return false;
		getChildren().remove(previous);
		getChildren().add(index, component);
		return true;
	}

	@Override
	public boolean moveChildToTop(IUIComponent component) { return moveChildTo(getChildren().size() - 1, component); }

	@Override
	public List<IUIComponent> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIComponent> getChildren() { return children; }
}
