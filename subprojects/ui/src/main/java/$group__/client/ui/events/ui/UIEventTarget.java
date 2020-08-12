package $group__.client.ui.events.ui;

import $group__.client.ui.mvvm.core.views.events.IUIEvent;
import $group__.client.ui.mvvm.core.views.events.IUIEventListener;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;
import java.util.function.Predicate;

public class UIEventTarget implements IUIEventTarget {
	@SuppressWarnings("UnstableApiUsage")
	protected final SetMultimap<ResourceLocation, IUIEventListenerWithParameters> eventTargetListeners = MultimapBuilder
			.hashKeys(CapacityUtilities.INITIAL_CAPACITY_SMALL)
			.linkedHashSetValues(CapacityUtilities.INITIAL_CAPACITY_SMALL)
			.build();

	@Override
	public boolean addEventListener(ResourceLocation type, IUIEventListener<?> listener, boolean useCapture) { return getEventTargetListeners().put(type, new IUIEventListenerWithParameters(listener, useCapture)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected SetMultimap<ResourceLocation, IUIEventListenerWithParameters> getEventTargetListeners() { return eventTargetListeners; }

	@Override
	public boolean removeEventListener(ResourceLocation type, IUIEventListener<?> listener, boolean useCapture) {
		if (getEventTargetListeners().remove(type, new IUIEventListenerWithParameters(listener, useCapture))) {
			listener.markAsRemoved();
			return true;
		} else
			return false;
	}

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
				shouldHandle = l -> true;
				break;
			case BUBBLING_PHASE:
				shouldHandle = l -> !l.isUseCapture();
				break;
			default:
				throw BecauseOf.illegalArgument("Invalid event phase", event,
						"event.getPhase()", event.getPhase(),
						"event", event);
		}

		for (IUIEventListenerWithParameters l : ls) {
			if (shouldHandle.test(l))
				l.getListener().accept(CastUtilities.castUnchecked(event));
		}

		return !ls.isEmpty();
	}

	@Override
	public boolean isActive() { return true; }

	public static class IUIEventListenerWithParameters {
		public static final ImmutableList<Function<IUIEventListenerWithParameters, Object>> OBJECT_VARIABLES = ImmutableList.of(
				IUIEventListenerWithParameters::getListener, IUIEventListenerWithParameters::isUseCapture);
		public static final ImmutableMap<String, Function<IUIEventListenerWithParameters, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
				ImmutableList.of("listener", "useCapture"),
				OBJECT_VARIABLES));
		protected final IUIEventListener<?> listener;
		protected final boolean useCapture;

		public IUIEventListenerWithParameters(IUIEventListener<?> listener, boolean useCapture) {
			this.listener = listener;
			this.useCapture = useCapture;
		}

		public IUIEventListener<?> getListener() { return listener; }

		public boolean isUseCapture() { return useCapture; }

		@Override
		public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		@Override
		public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

		@Override
		public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
	}
}
