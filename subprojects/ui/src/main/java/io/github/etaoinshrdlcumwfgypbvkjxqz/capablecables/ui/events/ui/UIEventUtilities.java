package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataKeyboardKeyPress;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataMouseButtonClick;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.UIDataMouseButtonClick;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.INode;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.ResourceBundle;

public enum UIEventUtilities {
	;

	@SuppressWarnings("ObjectAllocationInLoop")
	public static boolean dispatchEvent(IUIEvent event) {
		UIEventRegistry.checkEvent(event);
		event.reset();

		IUIEventTarget et = event.getTarget();
		if (et instanceof INode) {
			ImmutableList<INode> path = computeNodePath((INode) et);

			event.advancePhase();
			for (int i = 0, maxI = path.size() - 1; i < maxI; i++) {
				@Nullable INode n = path.get(i);
				CastUtilities.castChecked(IUIEventTarget.class, n)
						.filter(IUIEventTarget::isActive)
						.ifPresent(nc -> nc.dispatchEvent(event));
			}

			event.advancePhase();
			if (et.isActive())
				et.dispatchEvent(event);

			if (!event.isPropagationStopped() && event.canBubble()) {
				event.advancePhase();
				ImmutableList<INode> pathReversed = path.reverse();
				for (int i = 1, maxI = pathReversed.size(); i < maxI; i++) {
					@Nullable INode n = pathReversed.get(i);
					CastUtilities.castChecked(IUIEventTarget.class, n)
							.filter(IUIEventTarget::isActive)
							.ifPresent(nc -> nc.dispatchEvent(event));
				}
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
			// COMMENT inputs
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
				return createEventKeyUp(event.getViewContextView(), event.getTarget(), event.getData().recreate());
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIEvent)
							.addKeyValue("event", event)
							.addMessages(() -> getResourceBundle().getString("synthetic.event.type.invalid"))
							.build()
			);
		}

		public static IUIEventKeyboard createEventKeyUp(IUIViewContext viewContext, IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(EnumUIEventDOMType.KEY_UP.getEventType(), true, true, viewContext, target, data); }

		public static IUIEvent createEventSelect(IUIViewContext viewContext, IUIEventTarget target) { return new UIEvent(EnumUIEventDOMType.SELECT.getEventType(), true, false, viewContext, target); }

		public static IUIEventFocus createEventFocusOutPost(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(), false, false, viewContext, target, targetBeingFocused); }

		public static IUIEventFocus createEventFocusInPost(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), false, false, viewContext, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusInPre(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(), true, false, viewContext, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusOutPre(IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(), true, false, viewContext, target, targetBeingFocused); }

		public static IUIEventMouse createEventClick(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.CLICK.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventClickDouble(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseDown(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseEnter(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), false, false, viewContext, target, data, targetBeingLeft); }

		public static IUIEventMouse createEventMouseMove(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_MOVE.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseEnterSelf(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), true, true, viewContext, target, data, targetBeingLeft); }

		public static IUIEventChar createEventChar(IUIViewContext viewContext, IUIEventTarget target, char codePoint, int modifiers) { return new UIEventChar(EnumUIEventComponentType.CHAR_TYPED.getEventType(), true, false, viewContext, target, codePoint, modifiers); }

		public static IUIEventMouseWheel createEventWheel(boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingPointed, double delta) { return new UIEventMouseWheel(EnumUIEventDOMType.WHEEL.getEventType(), true, cancelable, viewContext, target, data, targetBeingPointed, delta); }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static IUIEventKeyboard createEventKeyDown(IUIViewContext viewContext, IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(EnumUIEventDOMType.KEY_DOWN.getEventType(), true, true, viewContext, target, data); }

		public static IUIEventMouse generateSyntheticEventMouseOpposite(IUIEventMouse event, Point2D cursorPosition) {
			UIDataMouseButtonClick data = new UIDataMouseButtonClick(cursorPosition, event.getData().getButton());
			if (EnumUIEventDOMType.MOUSE_DOWN.getEventType().equals(event.getType()))
				return createEventMouseUp(event.getViewContextView(), event.getTarget(), data);
			else if (EnumUIEventDOMType.MOUSE_ENTER.getEventType().equals(event.getType()))
				return createEventMouseLeave(event.getViewContextView(), event.getTarget(), data, event.getRelatedTarget().orElse(null));
			else if (EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType().equals(event.getType()))
				return createEventMouseLeaveSelf(event.getViewContextView(), event.getTarget(), data, event.getRelatedTarget().orElse(null));
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIEvent)
							.addKeyValue("event", event)
							.addMessages(() -> getResourceBundle().getString("synthetic.event.type.invalid"))
							.build()
			);
		}

		public static IUIEventMouse createEventMouseUp(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_UP.getEventType(), true, true, viewContext, target, data, null); }

		public static IUIEventMouse createEventMouseLeave(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), false, false, viewContext, target, data, targetBeingEntered); }

		public static IUIEventMouse createEventMouseLeaveSelf(IUIViewContext viewContext, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), true, true, viewContext, target, data, targetBeingEntered); }
	}
}
