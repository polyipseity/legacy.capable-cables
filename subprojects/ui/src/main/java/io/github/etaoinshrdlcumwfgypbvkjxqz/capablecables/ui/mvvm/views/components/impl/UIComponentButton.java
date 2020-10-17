package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.FunctionalUIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEventRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEventUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.methods.BindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class UIComponentButton
		extends UIComponentContainer
		implements IUIComponentCursorHandleProviderModifier {
	@NonNls
	public static final String METHOD_ON_ACTIVATE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.methods.activate";
	@NonNls
	public static final String METHOD_ON_ACTIVATED = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.methods.activated";

	public static final INamespacePrefixedString METHOD_ON_ACTIVATE_LOCATION = ImmutableNamespacePrefixedString.of(METHOD_ON_ACTIVATE);
	public static final INamespacePrefixedString METHOD_ON_ACTIVATED_LOCATION = ImmutableNamespacePrefixedString.of(METHOD_ON_ACTIVATED);

	@UIMethod(METHOD_ON_ACTIVATE)
	private final IBindingMethodSource<IUIEventActivate> methodOnActivate;
	@UIMethod(METHOD_ON_ACTIVATED)
	private final IBindingMethodSource<IUIEvent> methodOnActivated;

	private final Set<IButtonState> buttonStates = EnumSet.noneOf(IButtonState.class);

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIComponentConstructor
	public UIComponentButton(UIComponentConstructor.IArguments arguments) {
		super(arguments);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.methodOnActivate = new BindingMethodSource<>(IUIEventActivate.class,
				Optional.ofNullable(mappings.get(METHOD_ON_ACTIVATE_LOCATION)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
		this.methodOnActivated = new BindingMethodSource<>(IUIEvent.class,
				Optional.ofNullable(mappings.get(METHOD_ON_ACTIVATED_LOCATION)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));

		addEventListener(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), new FunctionalUIEventListener<IUIEventMouse>(e -> {
			if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
				getButtonStates().add(IButtonState.HOVERING);
		}), false);
		addEventListener(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), new FunctionalUIEventListener<IUIEventMouse>(e -> {
			if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
				getButtonStates().remove(IButtonState.HOVERING);
		}), false);

		addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new FunctionalUIEventListener<IUIEventMouse>(e -> {
			if (IUIEventActivate.shouldActivate(this, e)) {
				getButtonStates().add(IButtonState.PRESSING);
				e.stopPropagation();
			}
		}), false);
		addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new FunctionalUIEventListener<IUIEventMouse>(e -> {
			if (getButtonStates().remove(IButtonState.PRESSING) && getButtonStates().contains(IButtonState.HOVERING)) {
				getMethodOnActivated().invoke(e);
				e.stopPropagation();
			}
		}), false);

		addEventListener(EnumUIEventDOMType.KEY_DOWN.getEventType(), new FunctionalUIEventListener<IUIEventKeyboard>(e -> {
			if (IUIEventActivate.shouldActivate(this, e))
				getMethodOnActivated().invoke(e);
		}), false);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IButtonState> getButtonStates() { return buttonStates; }

	protected IBindingMethodSource<IUIEvent> getMethodOnActivated() {
		return methodOnActivated;
	}

	protected IBindingMethodSource<IUIEventActivate> getMethodOnActivate() { return methodOnActivate; }

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

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(getMethodOnActivate(), getMethodOnActivated()));
	}

	public interface IUIEventActivate extends IUIEvent {
		@NonNls String TYPE_STRING = IUIEventType.StaticHolder.getDefaultPrefix() + "component.button.activated";
		INamespacePrefixedString TYPE = ImmutableNamespacePrefixedString.of(TYPE_STRING);

		static boolean shouldActivate(UIComponentButton self, IUIEvent event) {
			return UIEventUtilities.dispatchEvent(new IUIEventActivate.Impl((IUIEventTarget.Functional) e -> {
				self.getMethodOnActivate().invoke((IUIEventActivate) e);
				return !event.isDefaultPrevented();
			}, event));
		}

		IUIEvent getCause();

		class Impl
				extends UIEvent
				implements IUIEventActivate {
			static {
				UIEventRegistry.getInstance().register(TYPE, IUIEventActivate.class); // COMMENT custom: button will be activated
			}

			protected final IUIEvent cause;

			public Impl(IUIEventTarget target, IUIEvent cause) {
				super(TYPE, false, true, cause.getViewContext(), target);
				this.cause = cause;
			}

			@Override
			public IUIEvent getCause() { return cause; }
		}
	}
}
