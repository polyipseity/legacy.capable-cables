package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.INode;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IKeyboardKeyPressData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl.ImmutableMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.ResourceBundle;

public enum UIEventUtilities {
	;

	public static boolean dispatchEvent(IUIEvent event) {
		UIEventRegistry.checkEvent(event);
		event.reset();

		IUIEventTarget et = event.getTarget();
		if (et instanceof INode) {
			ImmutableList<INode> path = computeNodePath((INode) et);

			event.advancePhase();
			path.subList(0, path.size() - 1).stream().sequential()
					.filter(IUIEventTarget.class::isInstance)
					.map(IUIEventTarget.class::cast)
					.filter(IUIEventTarget::isActive)
					.forEachOrdered(eventTarget -> eventTarget.dispatchEvent(event));

			event.advancePhase();
			if (et.isActive())
				et.dispatchEvent(event);

			if (!event.isPropagationStopped() && event.canBubble()) {
				event.advancePhase();
				path.subList(0, path.size() - 1).reverse().stream().sequential()
						.filter(IUIEventTarget.class::isInstance)
						.map(IUIEventTarget.class::cast)
						.filter(IUIEventTarget::isActive)
						.forEachOrdered(eventTarget -> eventTarget.dispatchEvent(event));
			}
		} else {
			event.advancePhase();
			// COMMENT NOOP
			event.advancePhase();
			et.dispatchEvent(event);
			if (event.canBubble())
				event.advancePhase();
			// COMMENT NOOP
		}

		return !event.isDefaultPrevented();
	}

	public static ImmutableList<INode> computeNodePath(INode node) {
		ImmutableList.Builder<INode> builder = ImmutableList.builder();
		builder.add(node);
		Optional<? extends INode> parent = node.getParentNode();
		while (parent.isPresent()) {
			builder.add(parent.get());
			parent = parent.get().getParentNode();
		}
		return builder.build().reverse();
	}

	@SuppressWarnings("SpellCheckingInspection")
	public enum Factory {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		static {
			/* SECTION DOM */
			// COMMENT load
			// COMMENT unload
			// COMMENT abort
			// COMMENT error
			// TODO implement select
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.SELECT.getEventType(), IUIEvent.class); // COMMENT select: NO default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(), IUIEventFocus.class); // COMMENT blur: NO default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), IUIEventFocus.class); // COMMENT focus: NO default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(), IUIEventFocus.class); // COMMENT focusin: NO default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(), IUIEventFocus.class); // COMMENT focusout: NO default
			// COMMENT auxclick
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.CLICK.getEventType(), IUIEventMouse.class); // COMMENT click: activate, and/or focus
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(), IUIEventMouse.class); // COMMENT dblclick: activate, focus, and/or select
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), IUIEventMouse.class); // COMMENT mousedown: drag, start select, scroll, and/or pan
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), IUIEventMouse.class); // COMMENT mousenter: NO default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), IUIEventMouse.class); // COMMENT mouseleave: NO default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.MOUSE_MOVE.getEventType(), IUIEventMouse.class); // COMMENT mousemove: may have default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), IUIEventMouse.class); // COMMENT mouseout: may have default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), IUIEventMouse.class); // COMMENT mouseover: may have default
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.MOUSE_UP.getEventType(), IUIEventMouse.class);// COMMENT mouseup: context menu
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.WHEEL.getEventType(), IUIEventMouseWheel.class); // COMMENT wheel: scroll or zoom, cancelability varies
			// COMMENT beforeinput
			// COMMENT input
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.KEY_DOWN.getEventType(), IUIEventKeyboard.class); // COMMENT keydown: focus, keypress, and/or activate
			UIEventRegistry.getInstance().register(EnumUIEventDOMType.KEY_UP.getEventType(), IUIEventKeyboard.class); // COMMENT keyup: may have default
			// COMMENT compositionstart
			// COMMENT compositionupdate
			// COMMENT compositionend

			/* SECTION component */
			UIEventRegistry.getInstance().register(EnumUIEventComponentType.CHAR_TYPED.getEventType(), IUIEventChar.class); // COMMENT char_typed: NO default
		}

		public static IUIEventKeyboard generateSyntheticEventKeyboardOpposite(IUIEventKeyboard event) {
			if (EnumUIEventDOMType.KEY_DOWN.getEventType().equals(event.getType()))
				return createEventKeyUp(event.getViewContext(), event.getTarget(), event.getData().recreate());
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIEvent)
							.addKeyValue("event", event)
							.addMessages(() -> getResourceBundle().getString("synthetic.event.type.invalid"))
							.build()
			);
		}

		public static IUIEventKeyboard createEventKeyUp(IUIViewContext viewContext, IUIEventTarget target, IKeyboardKeyPressData data) { return new UIDefaultEventKeyboard(EnumUIEventDOMType.KEY_UP.getEventType(), true, true, viewContext, target, data); }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static IUIEvent createEventSelect(IUIViewContext viewContext, IUIEventTarget target) { return new UIDefaultEvent(EnumUIEventDOMType.SELECT.getEventType(), true, false, viewContext, target); }

		public static IUIEventFocus createEventFocusOutPost(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIDefaultEventFocus(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(), false, false, viewContext, target, targetBeingFocused); }

		public static IUIEventFocus createEventFocusInPost(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIDefaultEventFocus(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), false, false, viewContext, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusInPre(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIDefaultEventFocus(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(), true, false, viewContext, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusOutPre(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIDefaultEventFocus(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(), true, false, viewContext, target, targetBeingFocused); }

		public static IUIEventMouse createEventClick(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data) { return new UIDefaultEventMouse(EnumUIEventDOMType.CLICK.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventClickDouble(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data) { return new UIDefaultEventMouse(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseDown(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data) { return new UIDefaultEventMouse(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseEnter(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data, @Nullable IUIEventTarget targetBeingLeft) { return new UIDefaultEventMouse(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), false, false, viewContext, target, data, targetBeingLeft); }

		public static IUIEventMouse createEventMouseMove(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data) { return new UIDefaultEventMouse(EnumUIEventDOMType.MOUSE_MOVE.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseEnterSelf(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data, @Nullable IUIEventTarget targetBeingLeft) { return new UIDefaultEventMouse(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), true, true, viewContext, target, data, targetBeingLeft); }

		public static IUIEventChar createEventChar(IUIViewContext viewContext, IUIEventTarget target, char codePoint, int modifiers) { return new UIDefaultEventChar(EnumUIEventComponentType.CHAR_TYPED.getEventType(), true, false, viewContext, target, codePoint, modifiers); }

		public static IUIEventMouseWheel createEventWheel(boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data, @Nullable IUIEventTarget targetBeingPointed, double delta) { return new UIDefaultEventMouseWheel(EnumUIEventDOMType.WHEEL.getEventType(), true, cancelable, viewContext, target, data, targetBeingPointed, delta); }

		public static IUIEventKeyboard createEventKeyDown(IUIViewContext viewContext, IUIEventTarget target, IKeyboardKeyPressData data) { return new UIDefaultEventKeyboard(EnumUIEventDOMType.KEY_DOWN.getEventType(), true, true, viewContext, target, data); }

		public static IUIEventMouse generateSyntheticEventMouseOpposite(IUIEventMouse event, Point2D cursorPosition) {
			ImmutableMouseButtonClickData data = new ImmutableMouseButtonClickData(cursorPosition, event.getData().getButton());
			if (EnumUIEventDOMType.MOUSE_DOWN.getEventType().equals(event.getType()))
				return createEventMouseUp(event.getViewContext(), event.getTarget(), data);
			else if (EnumUIEventDOMType.MOUSE_ENTER.getEventType().equals(event.getType()))
				return createEventMouseLeave(event.getViewContext(), event.getTarget(), data, event.getRelatedTarget().orElse(null));
			else if (EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType().equals(event.getType()))
				return createEventMouseLeaveSelf(event.getViewContext(), event.getTarget(), data, event.getRelatedTarget().orElse(null));
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIEvent)
							.addKeyValue("event", event)
							.addMessages(() -> getResourceBundle().getString("synthetic.event.type.invalid"))
							.build()
			);
		}

		public static IUIEventMouse createEventMouseUp(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data) { return new UIDefaultEventMouse(EnumUIEventDOMType.MOUSE_UP.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseLeave(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data, @Nullable IUIEventTarget targetBeingEntered) { return new UIDefaultEventMouse(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), false, false, viewContext, target, data, targetBeingEntered); }

		public static IUIEventMouse createEventMouseLeaveSelf(IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data, @Nullable IUIEventTarget targetBeingEntered) { return new UIDefaultEventMouse(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), true, true, viewContext, target, data, targetBeingEntered); }
	}
}
