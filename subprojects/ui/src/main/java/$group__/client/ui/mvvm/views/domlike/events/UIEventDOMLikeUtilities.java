package $group__.client.ui.mvvm.views.domlike.events;

import $group__.client.ui.mvvm.views.domlike.nodes.IUIViewDOMLikeNode;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Optional;

public enum UIEventDOMLikeUtilities {
	;

	public static <T extends EventTarget & IUIViewDOMLikeNode> boolean dispatchEventToTarget(T target, UIEventDOMLike event) {
		Reg.checkEvent(event);
		event.setTarget(target);
		ImmutableList<IUIViewDOMLikeNode> path = computeNodePath(target);

		event.setEventPhase(Event.CAPTURING_PHASE);
		for (int i = 0, maxI = path.size() - 1; i < maxI; i++) {
			IUIViewDOMLikeNode n = path.get(i);
			if (n instanceof EventTarget)
				((EventTarget) n).dispatchEvent(event);
		}

		event.setEventPhase(Event.AT_TARGET);
		target.dispatchEvent(event);

		if (!event.isPropagationStopped() && event.getBubbles()) {
			event.setEventPhase(Event.BUBBLING_PHASE);
			ImmutableList<IUIViewDOMLikeNode> pathReversed = path.reverse();
			for (int i = 1, maxI = pathReversed.size(); i < maxI; i++) {
				IUIViewDOMLikeNode n = pathReversed.get(i);
				if (n instanceof EventTarget)
					((EventTarget) n).dispatchEvent(event);
			}
		}

		event.setEventPhase((short) 0);
		event.setCurrentTarget(null);
		event.setPropagationStopped(false);
		event.setTarget(null);

		return !event.isDefaultPrevented();
	}

	public static ImmutableList<IUIViewDOMLikeNode> computeNodePath(IUIViewDOMLikeNode node) {
		ImmutableList.Builder<IUIViewDOMLikeNode> builder = ImmutableList.builder();
		builder.add(node);
		Optional<IUIViewDOMLikeNode> parent = node.getParentNode();
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
			Reg.INSTANCE.register(new ResourceLocation(UIEventUIDOMLike.TYPE_SELECT), UIEventUIDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventUIDOMLike.Focus.TYPE_FOCUS_OUT_POST), UIEventUIDOMLike.Focus.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventUIDOMLike.Focus.TYPE_FOCUS_IN_POST), UIEventUIDOMLike.Focus.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventUIDOMLike.Focus.TYPE_FOCUS_IN_PRE), UIEventUIDOMLike.Focus.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventUIDOMLike.Focus.TYPE_FOCUS_OUT_PRE), UIEventUIDOMLike.Focus.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_CLICK_AUXILIARY), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_CLICK), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_CLICK_DOUBLE), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_MOUSE_DOWN), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_MOUSE_ENTER), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_MOUSE_LEAVE), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_MOUSE_MOVE), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_MOUSE_LEAVE_SELF_OR_CHILD), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_MOUSE_ENTER_SELF_OR_CHILD), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.TYPE_MOUSE_UP), UIEventMouseDOMLike.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventMouseDOMLike.Wheel.TYPE_WHEEL), UIEventMouseDOMLike.Wheel.class);
			// COMMENT beforeinput
			// COMMENT input
			Reg.INSTANCE.register(new ResourceLocation(UIEventUIDOMLike.Keyboard.TYPE_KEY_DOWN), UIEventUIDOMLike.Keyboard.class);
			Reg.INSTANCE.register(new ResourceLocation(UIEventUIDOMLike.Keyboard.TYPE_KEY_UP), UIEventUIDOMLike.Keyboard.class);
			// COMMENT compositionstart
			// COMMENT compositionupdate
			// COMMENT compositionend
		}

		public static UIEventUIDOMLike createEventSelect(@Nullable AbstractView viewArg, int detailArg) {
			UIEventUIDOMLike ret = new UIEventUIDOMLike();
			ret.initUIEvent(UIEventUIDOMLike.TYPE_SELECT, true, false, viewArg, detailArg);
			return ret;
		}

		public static UIEventUIDOMLike.Focus createEventFocusOutPost(@Nullable AbstractView viewArg, int detailArg, EventTarget targetBeingFocused) {
			UIEventUIDOMLike.Focus ret = new UIEventUIDOMLike.Focus();
			ret.initFocusEvent(UIEventUIDOMLike.Focus.TYPE_FOCUS_OUT_POST, false, false, viewArg, detailArg, targetBeingFocused);
			return ret;
		}

		public static UIEventUIDOMLike.Focus createEventFocusInPost(@Nullable AbstractView viewArg, int detailArg, @Nullable EventTarget targetBeingUnfocused) {
			UIEventUIDOMLike.Focus ret = new UIEventUIDOMLike.Focus();
			ret.initFocusEvent(UIEventUIDOMLike.Focus.TYPE_FOCUS_IN_POST, false, false, viewArg, detailArg, targetBeingUnfocused);
			return ret;
		}

		public static UIEventUIDOMLike.Focus createEventFocusInPre(@Nullable AbstractView viewArg, int detailArg, @Nullable EventTarget targetBeingUnfocused) {
			UIEventUIDOMLike.Focus ret = new UIEventUIDOMLike.Focus();
			ret.initFocusEvent(UIEventUIDOMLike.Focus.TYPE_FOCUS_IN_PRE, true, false, viewArg, detailArg, targetBeingUnfocused);
			return ret;
		}

		public static UIEventUIDOMLike.Focus createEventFocusOutPre(@Nullable AbstractView viewArg, int detailArg, EventTarget targetBeingFocused) {
			UIEventUIDOMLike.Focus ret = new UIEventUIDOMLike.Focus();
			ret.initFocusEvent(UIEventUIDOMLike.Focus.TYPE_FOCUS_OUT_PRE, true, false, viewArg, detailArg, targetBeingFocused);
			return ret;
		}

		public static UIEventMouseDOMLike createEventClickAuxiliary(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_CLICK_AUXILIARY, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, null);
			return ret;
		}

		public static UIEventMouseDOMLike createEventClick(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_CLICK, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, null);
			return ret;
		}

		public static UIEventMouseDOMLike createEventClickDouble(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_CLICK_DOUBLE, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, null);
			return ret;
		}

		public static UIEventMouseDOMLike createEventMouseDown(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_MOUSE_DOWN, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, null);
			return ret;
		}

		public static UIEventMouseDOMLike createEventMouseEnter(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg, @Nullable EventTarget targetBeingLeft) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_MOUSE_ENTER, false, false, viewArg, detailArg, screen, client, modifiers, buttonArg, targetBeingLeft);
			return ret;
		}

		public static UIEventMouseDOMLike createEventMouseLeave(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg, @Nullable EventTarget targetBeingEntered) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_MOUSE_LEAVE, false, false, viewArg, detailArg, screen, client, modifiers, buttonArg, targetBeingEntered);
			return ret;
		}

		public static UIEventMouseDOMLike createEventMouseMove(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_MOUSE_MOVE, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, null);
			return ret;
		}

		public static UIEventMouseDOMLike createEventMouseLeaveSelfOrChild(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg, @Nullable EventTarget targetBeingEntered) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_MOUSE_LEAVE_SELF_OR_CHILD, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, targetBeingEntered);
			return ret;
		}

		public static UIEventMouseDOMLike createEventMouseEnterSelfOrChild(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg, @Nullable EventTarget targetBeingLeft) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_MOUSE_ENTER_SELF_OR_CHILD, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, targetBeingLeft);
			return ret;
		}

		public static UIEventMouseDOMLike createEventMouseUp(@Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg) {
			UIEventMouseDOMLike ret = new UIEventMouseDOMLike();
			ret.initMouseEvent(UIEventMouseDOMLike.TYPE_MOUSE_UP, true, true, viewArg, detailArg, screen, client, modifiers, buttonArg, null);
			return ret;
		}

		public static UIEventMouseDOMLike.Wheel createEventWheel(boolean cancelableArg, @Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg, @Nullable EventTarget targetBeingPointed, double deltaXArg, double deltaYArg, double deltaZArg, long deltaModeArg) {
			UIEventMouseDOMLike.Wheel ret = new UIEventMouseDOMLike.Wheel();
			ret.initWheelEvent(UIEventMouseDOMLike.Wheel.TYPE_WHEEL, true, cancelableArg, viewArg, detailArg, screen, client, modifiers, buttonArg, targetBeingPointed, deltaXArg, deltaYArg, deltaZArg, deltaModeArg);
			return ret;
		}

		public static UIEventUIDOMLike.Keyboard createEventKeyDown(@Nullable AbstractView viewArg, int detailArg, boolean repeatArg, boolean isComposingArg, int key, int scanCode, int modifiers) {
			UIEventUIDOMLike.Keyboard ret = new UIEventUIDOMLike.Keyboard();
			ret.initKeyboardEvent(UIEventUIDOMLike.Keyboard.TYPE_KEY_DOWN, true, true, viewArg, detailArg, repeatArg, isComposingArg, key, scanCode, modifiers);
			return ret;
		}

		public static UIEventUIDOMLike.Keyboard createEventKeyUp(@Nullable AbstractView viewArg, int detailArg, boolean repeatArg, boolean isComposingArg, int key, int scanCode, int modifiers) {
			UIEventUIDOMLike.Keyboard ret = new UIEventUIDOMLike.Keyboard();
			ret.initKeyboardEvent(UIEventUIDOMLike.Keyboard.TYPE_KEY_UP, true, true, viewArg, detailArg, repeatArg, isComposingArg, key, scanCode, modifiers);
			return ret;
		}
	}

	public static final class Reg extends Registry<ResourceLocation, Class<? extends Event>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final Reg INSTANCE = Singleton.getSingletonInstance(Reg.class, LOGGER);

		protected Reg() { super(false, LOGGER); }

		public static void checkEvent(Event event)
				throws IllegalArgumentException {
			if (!isEventValid(event))
				throw BecauseOf.illegalArgument("Invalid event",
						"event.getClass()", event.getClass().getName(),
						"event.getType()", event.getType(),
						"event", event);
		}

		public static boolean isEventValid(Event event) {
			return Optional.ofNullable(event.getType())
					.map(et -> INSTANCE.getDelegated().get(new ResourceLocation(et)))
					.map(RegistryObject::getValue)
					.map(ec -> ec.equals(event.getClass()))
					.orElse(false);
		}
	}
}
