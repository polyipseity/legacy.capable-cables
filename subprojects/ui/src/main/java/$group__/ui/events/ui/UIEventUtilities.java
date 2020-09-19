package $group__.ui.events.ui;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.views.events.*;
import $group__.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import $group__.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import $group__.ui.core.structures.IUIDataKeyboardKeyPress;
import $group__.ui.core.structures.IUIDataMouseButtonClick;
import $group__.ui.mvvm.views.events.ui.*;
import $group__.ui.utilities.UIDataMouseButtonClick;
import $group__.utilities.CastUtilities;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.structures.paths.INode;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.collect.ImmutableList;

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
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.SELECT.getEventType(), IUIEvent.class); // COMMENT select: NO default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(), IUIEventFocus.class); // COMMENT blur: NO default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), IUIEventFocus.class); // COMMENT focus: NO default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(), IUIEventFocus.class); // COMMENT focusin: NO default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(), IUIEventFocus.class); // COMMENT focusout: NO default
			// COMMENT auxclick
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.CLICK.getEventType(), IUIEventMouse.class); // COMMENT click: activate, and/or focus
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(), IUIEventMouse.class); // COMMENT dblclick: activate, focus, and/or select
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), IUIEventMouse.class); // COMMENT mousedown: drag, start select, scroll, and/or pan
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), IUIEventMouse.class); // COMMENT mousenter: NO default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), IUIEventMouse.class); // COMMENT mouseleave: NO default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.MOUSE_MOVE.getEventType(), IUIEventMouse.class); // COMMENT mousemove: may have default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), IUIEventMouse.class); // COMMENT mouseout: may have default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), IUIEventMouse.class); // COMMENT mouseover: may have default
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.MOUSE_UP.getEventType(), IUIEventMouse.class);// COMMENT mouseup: context menu
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.WHEEL.getEventType(), IUIEventMouseWheel.class); // COMMENT wheel: scroll or zoom, cancelability varies
			// COMMENT beforeinput
			// COMMENT input
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.KEY_DOWN.getEventType(), IUIEventKeyboard.class); // COMMENT keydown: focus, keypress, and/or activate
			UIEventRegistry.getINSTANCE().register(EnumUIEventDOMType.KEY_UP.getEventType(), IUIEventKeyboard.class); // COMMENT keyup: may have default
			// COMMENT compositionstart
			// COMMENT compositionupdate
			// COMMENT compositionend

			/* SECTION component */
			UIEventRegistry.getINSTANCE().register(EnumUIEventComponentType.CHAR_TYPED.getEventType(), IUIEventChar.class); // COMMENT char_typed: NO default
		}

		public static IUIEventKeyboard generateSyntheticEventKeyboardOpposite(IUIEventKeyboard event) {
			if (EnumUIEventDOMType.KEY_DOWN.getEventType().equals(event.getType()))
				return createEventKeyUp(event.getTarget(), event.getData().recreate());
			throw ThrowableUtilities.logAndThrow(
					new IllegalArgumentException(
							new LogMessageBuilder()
									.addKeyValue("event", event)
									.appendMessages(getResourceBundle().getString("synthetic.event.type.invalid"))
									.build()
					),
					UIConfiguration.getInstance().getLogger()
			);
		}

		public static IUIEvent createEventSelect(IUIEventTarget target) { return new UIEvent(EnumUIEventDOMType.SELECT.getEventType(), true, false, target); }

		public static IUIEventFocus createEventFocusOutPost(IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(), false, false, target, targetBeingFocused); }

		public static IUIEventFocus createEventFocusInPost(IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), false, false, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusInPre(IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(), true, false, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusOutPre(IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(), true, false, target, targetBeingFocused); }

		public static IUIEventMouse createEventClick(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.CLICK.getEventType(), true, true, target, data, null); }

		public static IUIEventMouse createEventClickDouble(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(), true, true, target, data, null); }

		public static IUIEventMouse createEventMouseDown(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), true, true, target, data, null); }

		public static IUIEventMouse createEventMouseEnter(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), false, false, target, data, targetBeingLeft); }

		public static IUIEventMouse createEventMouseMove(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_MOVE.getEventType(), true, true, target, data, null); }

		public static IUIEventMouse createEventMouseEnterSelf(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), true, true, target, data, targetBeingLeft); }

		public static IUIEventChar createEventChar(IUIEventTarget target, char codePoint, int modifiers) { return new UIEventChar(EnumUIEventComponentType.CHAR_TYPED.getEventType(), true, false, target, codePoint, modifiers); }

		public static IUIEventMouseWheel createEventWheel(boolean cancelable, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingPointed, double delta) { return new UIEventMouseWheel(EnumUIEventDOMType.WHEEL.getEventType(), true, cancelable, target, data, targetBeingPointed, delta); }

		public static IUIEventKeyboard createEventKeyDown(IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(EnumUIEventDOMType.KEY_DOWN.getEventType(), true, true, target, data); }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static IUIEventKeyboard createEventKeyUp(IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(EnumUIEventDOMType.KEY_UP.getEventType(), true, true, target, data); }

		public static IUIEventMouse generateSyntheticEventMouseOpposite(IUIEventMouse event, Point2D cursorPosition) {
			UIDataMouseButtonClick data = new UIDataMouseButtonClick(cursorPosition, event.getData().getButton());
			if (EnumUIEventDOMType.MOUSE_DOWN.getEventType().equals(event.getType()))
				return createEventMouseUp(event.getTarget(), data);
			else if (EnumUIEventDOMType.MOUSE_ENTER.getEventType().equals(event.getType()))
				return createEventMouseLeave(event.getTarget(), data, event.getRelatedTarget().orElse(null));
			else if (EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType().equals(event.getType()))
				return createEventMouseLeaveSelf(event.getTarget(), data, event.getRelatedTarget().orElse(null));
			throw ThrowableUtilities.logAndThrow(
					new IllegalArgumentException(
							new LogMessageBuilder()
									.addKeyValue("event", event)
									.appendMessages(getResourceBundle().getString("synthetic.event.type.invalid"))
									.build()
					),
					UIConfiguration.getInstance().getLogger()
			);
		}

		public static IUIEventMouse createEventMouseUp(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_UP.getEventType(), true, true, target, data, null); }

		public static IUIEventMouse createEventMouseLeave(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), false, false, target, data, targetBeingEntered); }

		public static IUIEventMouse createEventMouseLeaveSelf(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), true, true, target, data, targetBeingEntered); }
	}
}
