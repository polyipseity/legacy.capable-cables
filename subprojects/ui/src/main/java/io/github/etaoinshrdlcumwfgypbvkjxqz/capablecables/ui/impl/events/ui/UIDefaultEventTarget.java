package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.jetbrains.annotations.NonNls;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class UIDefaultEventTarget
		implements IUIEventTarget {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	@SuppressWarnings("UnstableApiUsage")
	private final SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters> eventTargetListeners = MultimapBuilder
			.hashKeys(CapacityUtilities.getInitialCapacitySmall())
			.linkedHashSetValues(CapacityUtilities.getInitialCapacitySmall())
			.build();

	@Override
	public boolean addEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture) { return getEventTargetListeners().put(type, new UIEventListenerWithParameters(listener, useCapture)); }

	@Override
	public boolean removeEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture) {
		if (getEventTargetListeners().remove(type, new UIEventListenerWithParameters(listener, useCapture))) {
			listener.markAsRemoved();
			return true;
		} else
			return false;
	}

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		if (event.isPropagationStopped())
			return false;
		ImmutableList<UIEventListenerWithParameters> listeners = ImmutableList.copyOf(getEventTargetListeners().get(event.getType()));

		Predicate<UIEventListenerWithParameters> shouldHandle;
		switch (event.getPhase()) {
			case CAPTURING_PHASE:
				shouldHandle = UIEventListenerWithParameters::isUseCapture;
				break;
			case AT_TARGET:
				shouldHandle = FunctionUtilities.getAlwaysTruePredicate();
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

		return listeners.stream()
				.filter(shouldHandle)
				.map(UIEventListenerWithParameters::getListener)
				.map(listener -> {
					listener.accept(CastUtilities.castUnchecked(event));
					return true;
				})
				.reduce(false, Boolean::logicalOr);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public boolean isActive() { return true; }

	@Override
	public boolean isFocusable() { return false; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters> getEventTargetListeners() { return eventTargetListeners; }

	public static class UIEventListenerWithParameters {
		private static final ImmutableList<Function<? super UIEventListenerWithParameters, ?>> OBJECT_VARIABLES = ImmutableList.of(
				UIEventListenerWithParameters::getListener, UIEventListenerWithParameters::isUseCapture);
		@NonNls
		private static final ImmutableMap<String, Function<? super UIEventListenerWithParameters, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("listener", "useCapture"),
				getObjectVariables()));
		private final IUIEventListener<?> listener;
		private final boolean useCapture;

		public UIEventListenerWithParameters(IUIEventListener<?> listener, boolean useCapture) {
			this.listener = listener;
			this.useCapture = useCapture;
		}

		@Override
		public int hashCode() { return ObjectUtilities.hashCode(this, null, getObjectVariables()); }

		public static ImmutableList<Function<? super UIEventListenerWithParameters, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		@Override
		public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, getObjectVariables()); }

		@Override
		public String toString() { return ObjectUtilities.toString(this, super::toString, getObjectVariablesMap()); }

		public static ImmutableMap<String, Function<? super UIEventListenerWithParameters, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }

		public IUIEventListener<?> getListener() { return listener; }

		public boolean isUseCapture() { return useCapture; }
	}
}
