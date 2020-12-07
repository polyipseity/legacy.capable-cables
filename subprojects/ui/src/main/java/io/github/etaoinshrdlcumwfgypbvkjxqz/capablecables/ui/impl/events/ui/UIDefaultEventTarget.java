package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.jetbrains.annotations.NonNls;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

public class UIDefaultEventTarget
		implements IUIEventTarget {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	@SuppressWarnings("UnstableApiUsage")
	private final SetMultimap<IIdentifier, UIEventListenerWithParameters> eventTargetListeners = MultimapBuilder
			.hashKeys(CapacityUtilities.getInitialCapacitySmall())
			.linkedHashSetValues(CapacityUtilities.getInitialCapacitySmall())
			.build();

	@Override
	public boolean addEventListener(IIdentifier type, IUIEventListener<?> listener, boolean useCapture) { return getEventTargetListeners().put(type, new UIEventListenerWithParameters(listener, useCapture)); }

	@Override
	public boolean removeEventListener(IIdentifier type, IUIEventListener<?> listener, boolean useCapture) {
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

		Predicate<@Nonnull UIEventListenerWithParameters> shouldHandle;
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

		return stripBool(
				listeners.stream()
						.filter(shouldHandle)
						.map(UIEventListenerWithParameters::getListener)
						.mapToInt(listener -> {
							listener.accept(CastUtilities.castUnchecked(event));
							return tBool();
						})
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public boolean isActive() { return true; }

	@Override
	public boolean isFocusable() { return false; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected SetMultimap<IIdentifier, UIEventListenerWithParameters> getEventTargetListeners() { return eventTargetListeners; }

	public static class UIEventListenerWithParameters {
		@SuppressWarnings("AutoBoxing")
		@NonNls
		private static final ImmutableMap<String, Function<@Nonnull UIEventListenerWithParameters, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull UIEventListenerWithParameters, @Nullable ?>>builder()
						.put("listeners", UIEventListenerWithParameters::getListener)
						.put("useCapture", UIEventListenerWithParameters::isUseCapture)
						.build();
		private final IUIEventListener<?> listener;
		private final boolean useCapture;

		public UIEventListenerWithParameters(IUIEventListener<?> listener, boolean useCapture) {
			this.listener = listener;
			this.useCapture = useCapture;
		}

		@Override
		public int hashCode() { return ObjectUtilities.hashCodeImpl(this, getObjectVariableMap().values()); }

		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		@Override
		public boolean equals(Object obj) { return ObjectUtilities.equalsImpl(this, obj, UIEventListenerWithParameters.class, false, getObjectVariableMap().values()); }

		@Override
		public String toString() { return ObjectUtilities.toStringImpl(this, getObjectVariableMap()); }

		public static ImmutableMap<String, Function<@Nonnull UIEventListenerWithParameters, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }

		public IUIEventListener<?> getListener() { return listener; }

		public boolean isUseCapture() { return useCapture; }
	}
}
