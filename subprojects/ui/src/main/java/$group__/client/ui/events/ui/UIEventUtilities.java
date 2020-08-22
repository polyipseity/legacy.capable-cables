package $group__.client.ui.events.ui;

import $group__.client.ui.core.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.core.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.core.views.events.*;
import $group__.client.ui.mvvm.core.views.paths.IUINode;
import $group__.client.ui.mvvm.views.events.ui.*;
import $group__.client.ui.utilities.UIDataMouseButtonClick;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Optional;

public enum UIEventUtilities {
	;

	public static boolean dispatchEvent(IUIEvent event) {
		RegUIEvent.checkEvent(event);
		event.reset();

		if (event.getTarget() instanceof IUINode) {
			ImmutableList<IUINode> path = computeNodePath((IUINode) event.getTarget());

			event.advancePhase();
			for (int i = 0, maxI = path.size() - 1; i < maxI; i++) {
				@Nullable IUINode n = path.get(i);
				if (n instanceof IUIEventTarget) {
					IUIEventTarget nc = (IUIEventTarget) n;
					if (nc.isActive())
						nc.dispatchEvent(event);
				}
			}

			event.advancePhase();
			if (event.getTarget().isActive())
				event.getTarget().dispatchEvent(event);

			if (!event.isPropagationStopped() && event.canBubble()) {
				event.advancePhase();
				ImmutableList<IUINode> pathReversed = path.reverse();
				for (int i = 1, maxI = pathReversed.size(); i < maxI; i++) {
					@Nullable IUINode n = pathReversed.get(i);
					if (n instanceof IUIEventTarget) {
						IUIEventTarget nc = (IUIEventTarget) n;
						if (nc.isActive())
							nc.dispatchEvent(event);
					}
				}
			}
		} else {
			event.advancePhase();
			// COMMENT NOOP
			event.advancePhase();
			event.getTarget().dispatchEvent(event);
			if (event.canBubble())
				event.advancePhase();
			// COMMENT NOOP
		}

		return !event.isDefaultPrevented();
	}

	public static ImmutableList<IUINode> computeNodePath(IUINode node) {
		ImmutableList.Builder<IUINode> builder = ImmutableList.builder();
		builder.add(node);
		Optional<IUINode> parent = node.getParentNode();
		while (parent.isPresent()) {
			builder.add(parent.get());
			parent = parent.get().getParentNode();
		}
		return builder.build().reverse();
	}

	@SuppressWarnings("SpellCheckingInspection")
	public enum Factory {
		;

		static {
			/* SECTION DOM */
			// COMMENT load
			// COMMENT unload
			// COMMENT abort
			// COMMENT error
			// TODO implement select
			RegUIEvent.INSTANCE.register(IUIEvent.TYPE_SELECT, IUIEvent.class); // COMMENT select: NO default
			RegUIEvent.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_OUT_POST, IUIEventFocus.class); // COMMENT blur: NO default
			RegUIEvent.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_IN_POST, IUIEventFocus.class); // COMMENT focus: NO default
			RegUIEvent.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_IN_PRE, IUIEventFocus.class); // COMMENT focusin: NO default
			RegUIEvent.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_OUT_PRE, IUIEventFocus.class); // COMMENT focusout: NO default
			// COMMENT auxclick
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_CLICK, IUIEventMouse.class); // COMMENT click: activate, and/or focus
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_CLICK_DOUBLE, IUIEventMouse.class); // COMMENT dblclick: activate, focus, and/or select
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_DOWN, IUIEventMouse.class); // COMMENT mousedown: drag, start select, scroll, and/or pan
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_ENTER, IUIEventMouse.class); // COMMENT mousenter: NO default
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_LEAVE, IUIEventMouse.class); // COMMENT mouseleave: NO default
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_MOVE, IUIEventMouse.class); // COMMENT mousemove: may have default
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_LEAVE_SELF, IUIEventMouse.class); // COMMENT mouseout: may have default
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_ENTER_SELF, IUIEventMouse.class); // COMMENT mouseover: may have default
			RegUIEvent.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_UP, IUIEventMouse.class);// COMMENT mouseup: context menu
			RegUIEvent.INSTANCE.register(IUIEventMouseWheel.TYPE_WHEEL, IUIEventMouseWheel.class); // COMMENT wheel: scroll or zoom, cancelability varies
			// COMMENT beforeinput
			// COMMENT input
			RegUIEvent.INSTANCE.register(IUIEventKeyboard.TYPE_KEY_DOWN, IUIEventKeyboard.class); // COMMENT keydown: focus, keypress, and/or activate
			RegUIEvent.INSTANCE.register(IUIEventKeyboard.TYPE_KEY_UP, IUIEventKeyboard.class); // COMMENT keyup: may have default
			// COMMENT compositionstart
			// COMMENT compositionupdate
			// COMMENT compositionend

			/* SECTION Standard */
			RegUIEvent.INSTANCE.register(IUIEventChar.TYPE_CHAR_TYPED, IUIEventChar.class); // COMMENT char_typed: NO default
		}

		public static IUIEvent createEventSelect(IUIEventTarget target) { return new UIEvent(IUIEvent.TYPE_SELECT, true, false, target); }

		public static IUIEventFocus createEventFocusOutPost(IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_OUT_POST, false, false, target, targetBeingFocused); }

		public static IUIEventFocus createEventFocusInPost(IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_IN_POST, false, false, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusInPre(IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_IN_PRE, true, false, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusOutPre(IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_OUT_PRE, true, false, target, targetBeingFocused); }

		public static IUIEventMouse createEventClick(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_CLICK, true, true, target, data, null); }

		public static IUIEventMouse createEventClickDouble(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_CLICK_DOUBLE, true, true, target, data, null); }

		public static IUIEventMouse createEventMouseDown(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_DOWN, true, true, target, data, null); }

		public static IUIEventMouse createEventMouseEnter(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_ENTER, false, false, target, data, targetBeingLeft); }

		public static IUIEventMouse createEventMouseLeave(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_LEAVE, false, false, target, data, targetBeingEntered); }

		public static IUIEventMouse createEventMouseMove(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_MOVE, true, true, target, data, null); }

		public static IUIEventMouse createEventMouseEnterSelf(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_ENTER_SELF, true, true, target, data, targetBeingLeft); }

		public static IUIEventMouse createEventMouseLeaveSelf(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_LEAVE_SELF, true, true, target, data, targetBeingEntered); }

		public static IUIEventChar createEventChar(IUIEventTarget target, char codePoint, int modifiers) { return new UIEventChar(IUIEventChar.TYPE_CHAR_TYPED, true, false, target, codePoint, modifiers); }

		public static IUIEventMouse createEventMouseUp(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_UP, true, true, target, data, null); }

		public static IUIEventMouseWheel createEventWheel(boolean cancelable, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingPointed, double delta) { return new UIEventMouseWheel(IUIEventMouseWheel.TYPE_WHEEL, true, cancelable, target, data, targetBeingPointed, delta); }

		public static IUIEventKeyboard createEventKeyDown(IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(IUIEventKeyboard.TYPE_KEY_DOWN, true, true, target, data); }

		public static IUIEventKeyboard createEventKeyUp(IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(IUIEventKeyboard.TYPE_KEY_UP, true, true, target, data); }

		public static IUIEventKeyboard generateSyntheticEventKeyboardOpposite(IUIEventKeyboard event) {
			if (IUIEventKeyboard.TYPE_KEY_DOWN.equals(event.getType()))
				return createEventKeyUp(event.getTarget(), event.getData().recreate());
			throw BecauseOf.illegalArgument("Illgeal event type",
					"event.getType()", event.getType(),
					"event", event);
		}

		public static IUIEventMouse generateSyntheticEventMouseOpposite(IUIEventMouse event, Point2D cursorPosition) {
			UIDataMouseButtonClick data = new UIDataMouseButtonClick(cursorPosition, event.getData().getButton());
			if (IUIEventMouse.TYPE_MOUSE_DOWN.equals(event.getType()))
				return createEventMouseUp(event.getTarget(), data);
			else if (IUIEventMouse.TYPE_MOUSE_ENTER.equals(event.getType()))
				return createEventMouseLeave(event.getTarget(), data, event.getRelatedTarget().orElse(null));
			else if (IUIEventMouse.TYPE_MOUSE_ENTER_SELF.equals(event.getType()))
				return createEventMouseLeaveSelf(event.getTarget(), data, event.getRelatedTarget().orElse(null));
			throw BecauseOf.illegalArgument("Illgeal event type",
					"event.getType()", event.getType(),
					"event", event);
		}
	}

	public static final class RegUIEvent extends Registry<ResourceLocation, Class<? extends IUIEvent>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final RegUIEvent INSTANCE = Singleton.getSingletonInstance(RegUIEvent.class, LOGGER);

		protected RegUIEvent() { super(false, LOGGER); }

		public static void checkEvent(IUIEvent event)
				throws IllegalArgumentException {
			if (!isEventValid(event))
				throw BecauseOf.illegalArgument("Invalid event",
						"event.getClass()", event.getClass(),
						"event.getType()", event.getType(),
						"event", event);
		}

		public static boolean isEventValid(IUIEvent event) {
			return Optional.ofNullable(INSTANCE.getDelegated().get(event.getType()))
					.map(RegistryObject::getValue)
					.map(ec -> ec.isInstance(event))
					.orElse(false);
		}
	}
}
