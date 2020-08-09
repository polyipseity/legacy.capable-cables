package $group__.client.ui.mvvm.views.events;

import $group__.client.ui.mvvm.core.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.views.core.events.*;
import $group__.client.ui.mvvm.views.core.nodes.IUINode;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public enum UIEventUtilities {
	;

	public static void dispatchEvent(IUIEvent event) {
		Reg.checkEvent(event);

		if (event instanceof IUINode) {
			ImmutableList<IUINode> path = computeNodePath((IUINode) event.getTarget());

			event.advancePhase();
			for (int i = 0, maxI = path.size() - 1; i < maxI; i++) {
				IUINode n = path.get(i);
				if (n instanceof IUIEventTarget)
					((IUIEventTarget) n).dispatchEvent(event);
			}

			event.advancePhase();
			event.getTarget().dispatchEvent(event);

			if (!event.isPropagationStopped() && event.canBubble()) {
				event.advancePhase();
				ImmutableList<IUINode> pathReversed = path.reverse();
				for (int i = 1, maxI = pathReversed.size(); i < maxI; i++) {
					IUINode n = pathReversed.get(i);
					if (n instanceof IUIEventTarget)
						((IUIEventTarget) n).dispatchEvent(event);
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

		event.reset();
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
			// COMMENT load
			// COMMENT unload
			// COMMENT abort
			// COMMENT error
			Reg.INSTANCE.register(IUIEvent.TYPE_SELECT, IUIEvent.class);
			Reg.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_OUT_POST, IUIEventFocus.class);
			Reg.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_IN_POST, IUIEventFocus.class);
			Reg.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_IN_PRE, IUIEventFocus.class);
			Reg.INSTANCE.register(IUIEventFocus.TYPE_FOCUS_OUT_PRE, IUIEventFocus.class);
			// COMMENT auxclick
			Reg.INSTANCE.register(IUIEventMouse.TYPE_CLICK, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_CLICK_DOUBLE, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_DOWN, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_ENTER, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_LEAVE, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_MOVE, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_LEAVE_SELF_OR_CHILD, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_ENTER_SELF_OR_CHILD, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouse.TYPE_MOUSE_UP, IUIEventMouse.class);
			Reg.INSTANCE.register(IUIEventMouseWheel.TYPE_WHEEL, IUIEventMouseWheel.class);
			// COMMENT beforeinput
			// COMMENT input
			Reg.INSTANCE.register(IUIEventKeyboard.TYPE_KEY_DOWN, IUIEventKeyboard.class);
			Reg.INSTANCE.register(IUIEventKeyboard.TYPE_KEY_UP, IUIEventKeyboard.class);
			// COMMENT compositionstart
			// COMMENT compositionupdate
			// COMMENT compositionend
		}

		public static IUIEvent createEventSelect(IUIEventTarget target) { return new UIEvent(IUIEvent.TYPE_SELECT, true, target); }

		public static IUIEventFocus createEventFocusOutPost(IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_OUT_POST, false, target, targetBeingFocused); }

		public static IUIEventFocus createEventFocusInPost(IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_IN_POST, false, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusInPre(IUIEventTarget target, @Nullable IUIEventTarget targetBeingUnfocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_IN_PRE, true, target, targetBeingUnfocused); }

		public static IUIEventFocus createEventFocusOutPre(IUIEventTarget target, @Nullable IUIEventTarget targetBeingFocused) { return new UIEventFocus(IUIEventFocus.TYPE_FOCUS_OUT_PRE, true, target, targetBeingFocused); }

		public static IUIEventMouse createEventClick(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_CLICK, true, target, data, null); }

		public static IUIEventMouse createEventClickDouble(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_CLICK_DOUBLE, true, target, data, null); }

		public static IUIEventMouse createEventMouseDown(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_DOWN, true, target, data, null); }

		public static IUIEventMouse createEventMouseEnter(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_ENTER, false, target, data, targetBeingLeft); }

		public static IUIEventMouse createEventMouseLeave(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_LEAVE, false, target, data, targetBeingEntered); }

		public static IUIEventMouse createEventMouseMove(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_MOVE, true, target, data, null); }

		public static IUIEventMouse createEventMouseLeaveSelfOrChild(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingEntered) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_LEAVE_SELF_OR_CHILD, true, target, data, targetBeingEntered); }

		public static IUIEventMouse createEventMouseEnterSelfOrChild(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingLeft) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_ENTER_SELF_OR_CHILD, true, target, data, targetBeingLeft); }

		public static IUIEventMouse createEventMouseUp(IUIEventTarget target, IUIDataMouseButtonClick data) { return new UIEventMouse(IUIEventMouse.TYPE_MOUSE_UP, true, target, data, null); }

		public static IUIEventMouseWheel createEventWheel(IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget targetBeingPointed, double delta) { return new UIEventMouseWheel(IUIEventMouseWheel.TYPE_WHEEL, true, target, data, targetBeingPointed, delta); }

		public static IUIEventKeyboard createEventKeyDown(IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(IUIEventKeyboard.TYPE_KEY_DOWN, true, target, data); }

		public static IUIEventKeyboard createEventKeyUp(IUIEventTarget target, IUIDataKeyboardKeyPress data) { return new UIEventKeyboard(IUIEventKeyboard.TYPE_KEY_UP, true, target, data); }
	}

	public static final class Reg extends Registry<ResourceLocation, Class<? extends IUIEvent>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final Reg INSTANCE = Singleton.getSingletonInstance(Reg.class, LOGGER);

		protected Reg() { super(false, LOGGER); }

		public static void checkEvent(IUIEvent event)
				throws IllegalArgumentException {
			if (!isEventValid(event))
				throw BecauseOf.illegalArgument("Invalid event",
						"event.getClass()", event.getClass().getName(),
						"event.getType()", event.getType(),
						"event", event);
		}

		public static boolean isEventValid(IUIEvent event) {
			return Optional.ofNullable(event.getType())
					.map(et -> INSTANCE.getDelegated().get(et))
					.map(RegistryObject::getValue)
					.map(ec -> ec.isInstance(event))
					.orElse(false);
		}
	}
}
