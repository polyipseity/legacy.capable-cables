package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.SetMultimap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIEventRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIEventUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.DelayedFieldInitializer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodSource;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class UIButtonComponent
		extends UIDefaultComponent
		implements IUIComponentCursorHandleProviderModifier {
	@NonNls
	public static final String METHOD_ON_ACTIVATE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "method.button.activate";
	@NonNls
	public static final String METHOD_ON_ACTIVATED = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "method.button.activated";

	private static final INamespacePrefixedString METHOD_ON_ACTIVATE_LOCATION = ImmutableNamespacePrefixedString.of(getMethodOnActivate());
	private static final INamespacePrefixedString METHOD_ON_ACTIVATED_LOCATION = ImmutableNamespacePrefixedString.of(getMethodOnActivated());

	@UIMethod(METHOD_ON_ACTIVATE)
	private final IBindingMethodSource<IUIEventActivate> onActivate;
	@UIMethod(METHOD_ON_ACTIVATED)
	private final IBindingMethodSource<IUIEvent> onActivated;

	private final Set<IButtonState> buttonStates = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(IButtonState.values().length).makeMap()
	);

	private final DelayedFieldInitializer<SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters>> eventTargetListenersInitializer;

	@UIComponentConstructor
	public UIButtonComponent(IUIComponentArguments arguments) {
		super(arguments);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.onActivate = new ImmutableBindingMethodSource<>(IUIEventActivate.class,
				Optional.ofNullable(mappings.get(getMethodOnActivateLocation())).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
		this.onActivated = new ImmutableBindingMethodSource<>(IUIEvent.class,
				Optional.ofNullable(mappings.get(getMethodOnActivatedLocation())).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));

		this.eventTargetListenersInitializer = new DelayedFieldInitializer<>(field -> {
			addEventListener(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
					getButtonStates().add(IButtonState.HOVERING);
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
					getButtonStates().remove(IButtonState.HOVERING);
			}), false);

			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (IUIEventActivate.shouldActivate(this, e)) {
					getButtonStates().add(IButtonState.PRESSING);
					e.stopPropagation();
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (getButtonStates().remove(IButtonState.PRESSING) && getButtonStates().contains(IButtonState.HOVERING)) {
					getOnActivated().invoke(e);
					e.stopPropagation();
				}
			}), false);

			addEventListener(EnumUIEventDOMType.KEY_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventKeyboard>(e -> {
				if (IUIEventActivate.shouldActivate(this, e))
					getOnActivated().invoke(e);
			}), false);
		});
	}

	@Override
	protected SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters> getEventTargetListeners() {
		return this.eventTargetListenersInitializer.apply(super.getEventTargetListeners());
	}

	public static INamespacePrefixedString getMethodOnActivateLocation() {
		return METHOD_ON_ACTIVATE_LOCATION;
	}

	public static INamespacePrefixedString getMethodOnActivatedLocation() {
		return METHOD_ON_ACTIVATED_LOCATION;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IButtonState> getButtonStates() { return buttonStates; }

	protected IBindingMethodSource<IUIEvent> getOnActivated() {
		return onActivated;
	}

	public static String getMethodOnActivate() {
		return METHOD_ON_ACTIVATE;
	}

	public static String getMethodOnActivated() {
		return METHOD_ON_ACTIVATED;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(getOnActivate(), getOnActivated()));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.cleanupBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.unbind(getOnActivate(), getOnActivated()));
	}

	protected IBindingMethodSource<IUIEventActivate> getOnActivate() { return onActivate; }

	@Override
	public boolean isFocusable() { return true; }

	@Override
	public Optional<Long> getCursorHandle(IUIComponentContext context) {
		return getButtonStates().contains(IButtonState.HOVERING)
				? Optional.of(EnumGLFWCursor.STANDARD_HAND_CURSOR.getHandle())
				: Optional.empty();
	}

	public enum IButtonState {
		HOVERING,
		PRESSING,
	}

	public interface IUIEventActivate extends IUIEvent {
		static boolean shouldActivate(UIButtonComponent self, IUIEvent event) {
			return UIEventUtilities.dispatchEvent(new IUIEventActivate.Impl((IUIEventTarget.Functional) e -> {
				self.getOnActivate().invoke((IUIEventActivate) e);
				return !event.isDefaultPrevented();
			}, event));
		}

		IUIEvent getCause();

		enum StaticHolder {
			;

			public static final @NonNls String TYPE_STRING = IUIEventType.StaticHolder.DEFAULT_PREFIX + "component.button.activated";
			private static final INamespacePrefixedString TYPE = ImmutableNamespacePrefixedString.of(getTypeString());

			public static String getTypeString() {
				return TYPE_STRING;
			}

			public static INamespacePrefixedString getType() {
				return TYPE;
			}
		}

		class Impl
				extends UIDefaultEvent
				implements IUIEventActivate {
			static {
				UIEventRegistry.getInstance().register(StaticHolder.getType(), IUIEventActivate.class); // COMMENT custom: button will be activated
			}

			private final IUIEvent cause;

			public Impl(IUIEventTarget target, IUIEvent cause) {
				super(StaticHolder.getType(), false, true, cause.getViewContext(), target);
				this.cause = cause;
			}

			@Override
			public IUIEvent getCause() { return cause; }
		}
	}
}
