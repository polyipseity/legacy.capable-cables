package $group__.ui.events.ui;

import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.ui.core.mvvm.views.events.IUIEventListener;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.functions.FunctionUtilities;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import org.jetbrains.annotations.NonNls;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class UIEventTarget
		implements IUIEventTarget {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	@SuppressWarnings("UnstableApiUsage")
	private final SetMultimap<INamespacePrefixedString, IUIEventListenerWithParameters> eventTargetListeners = MultimapBuilder
			.hashKeys(CapacityUtilities.INITIAL_CAPACITY_SMALL)
			.linkedHashSetValues(CapacityUtilities.INITIAL_CAPACITY_SMALL)
			.build();

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		if (event.isPropagationStopped())
			return false;
		ImmutableList<IUIEventListenerWithParameters> ls = ImmutableList.copyOf(getEventTargetListeners().get(event.getType()));

		Predicate<IUIEventListenerWithParameters> shouldHandle;
		switch (event.getPhase()) {
			case CAPTURING_PHASE:
				shouldHandle = IUIEventListenerWithParameters::isUseCapture;
				break;
			case AT_TARGET:
				shouldHandle = FunctionUtilities.alwaysTruePredicate();
				break;
			case BUBBLING_PHASE:
				shouldHandle = l -> !l.isUseCapture();
				break;
			default:
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(UIMarkers.getInstance()::getMarkerUIEvent)
								.addKeyValue("event", event)
								.addMessages(() -> getResourceBundle().getString("dispatch.phase.invalid"))
								.build()
				);
		}

		for (IUIEventListenerWithParameters l : ls) {
			if (shouldHandle.test(l))
				l.getListener().accept(CastUtilities.castUnchecked(event));
		}

		return !ls.isEmpty();
	}

	@Override
	public boolean addEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture) { return getEventTargetListeners().put(type, new IUIEventListenerWithParameters(listener, useCapture)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected SetMultimap<INamespacePrefixedString, IUIEventListenerWithParameters> getEventTargetListeners() { return eventTargetListeners; }

	@Override
	public boolean removeEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture) {
		if (getEventTargetListeners().remove(type, new IUIEventListenerWithParameters(listener, useCapture))) {
			listener.markAsRemoved();
			return true;
		} else
			return false;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public boolean isFocusable() { return false; }

	@Override
	public boolean isActive() { return true; }

	public static class IUIEventListenerWithParameters {
		private static final ImmutableList<Function<? super IUIEventListenerWithParameters, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IUIEventListenerWithParameters::getListener, IUIEventListenerWithParameters::isUseCapture);
		@NonNls
		private static final ImmutableMap<String, Function<? super IUIEventListenerWithParameters, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(getObjectVariables().size(),
				ImmutableList.of("listener", "useCapture"),
				getObjectVariables()));
		private final IUIEventListener<?> listener;
		private final boolean useCapture;

		public IUIEventListenerWithParameters(IUIEventListener<?> listener, boolean useCapture) {
			this.listener = listener;
			this.useCapture = useCapture;
		}

		@Override
		public int hashCode() { return ObjectUtilities.hashCode(this, null, getObjectVariables()); }

		public static ImmutableList<Function<? super IUIEventListenerWithParameters, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public IUIEventListener<?> getListener() { return listener; }

		public boolean isUseCapture() { return useCapture; }

		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		@Override
		public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, getObjectVariables()); }

		@Override
		public String toString() { return ObjectUtilities.toString(this, super::toString, getObjectVariablesMap()); }

		public static ImmutableMap<String, Function<? super IUIEventListenerWithParameters, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
