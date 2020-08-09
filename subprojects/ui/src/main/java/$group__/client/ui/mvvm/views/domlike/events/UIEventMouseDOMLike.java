package $group__.client.ui.mvvm.views.domlike.events;

import $group__.utilities.NamespaceUtilities;
import org.lwjgl.glfw.GLFW;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Optional;

public class UIEventMouseDOMLike extends UIEventUIDOMLike implements MouseEvent {
	/* CODE
	readonly attribute long screenX;
	readonly attribute long screenY;
	readonly attribute long clientX;
	readonly attribute long clientY;

	readonly attribute boolean ctrlKey;
	readonly attribute boolean shiftKey;
	readonly attribute boolean altKey;
	readonly attribute boolean metaKey;
	 */

	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public static final String
			TYPE_CLICK_AUXILIARY = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "auxclick",
			TYPE_CLICK = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "click",
			TYPE_CLICK_DOUBLE = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "dblclick",
			TYPE_MOUSE_DOWN = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mousedown",
			TYPE_MOUSE_ENTER = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseenter",
			TYPE_MOUSE_LEAVE = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseleave",
			TYPE_MOUSE_MOVE = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mousemove",
			TYPE_MOUSE_LEAVE_SELF_OR_CHILD = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseout",
			TYPE_MOUSE_ENTER_SELF_OR_CHILD = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseover",
			TYPE_MOUSE_UP = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseup";

	@Nullable
	protected Point2D screen, client;
	protected int modifiers;
	protected short button;
	@Nullable
	protected EventTarget relatedTarget;
	protected int detail;

	@Override
	@Deprecated
	public int getScreenX() { return getScreen().map(Point2D::getX).map(Math::floor).map(Double::intValue).orElse(0); }

	@Override
	@Deprecated
	public int getScreenY() { return getScreen().map(Point2D::getY).map(Math::floor).map(Double::intValue).orElse(0); }

	@Override
	@Deprecated
	public int getClientX() { return getClient().map(Point2D::getX).map(Math::floor).map(Double::intValue).orElse(0); }

	@Override
	@Deprecated
	public int getClientY() { return getClient().map(Point2D::getY).map(Math::floor).map(Double::intValue).orElse(0); }

	@Override
	@Deprecated
	public boolean getCtrlKey() { return (getModifiers() & GLFW.GLFW_MOD_CONTROL) != 0; }

	@Override
	@Deprecated
	public boolean getShiftKey() { return (getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0; }

	@Override
	@Deprecated
	public boolean getAltKey() { return (getModifiers() & GLFW.GLFW_MOD_ALT) != 0; }

	@Override
	@Deprecated
	public boolean getMetaKey() {
		return (getModifiers() & GLFW.GLFW_MOD_SUPER) != 0; // COMMENT sort of a meta key but also not
	}

	@Override
	public short getButton() { return button; }

	@Override
	@Nullable
	public EventTarget getRelatedTarget() { return relatedTarget; }

	@Override
	@Deprecated
	public void initMouseEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, short buttonArg, EventTarget relatedTargetArg) {
		int modifiers = 0;
		if (ctrlKeyArg)
			modifiers |= GLFW.GLFW_MOD_CONTROL;
		if (altKeyArg)
			modifiers |= GLFW.GLFW_MOD_ALT;
		if (shiftKeyArg)
			modifiers |= GLFW.GLFW_MOD_SHIFT;
		if (metaKeyArg)
			modifiers |= GLFW.GLFW_MOD_SUPER;

		initMouseEvent(typeArg, canBubbleArg, cancelableArg, viewArg, detailArg, new Point2D.Double(screenXArg, screenYArg), new Point2D.Double(clientXArg, clientYArg), modifiers, buttonArg, relatedTargetArg);
	}

	public void initMouseEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, @Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg, @Nullable EventTarget relatedTargetArg) {
		setScreen((Point2D) screen.clone());
		setClient((Point2D) client.clone());
		setModifiers(modifiers);
		setButton(buttonArg);
		setRelatedTarget(relatedTargetArg);

		initUIEvent(typeArg, canBubbleArg, cancelableArg, viewArg, detailArg);
	}

	protected void setRelatedTarget(@Nullable EventTarget relatedTarget) { this.relatedTarget = relatedTarget; }

	protected void setButton(short button) { this.button = button; }

	public int getModifiers() { return modifiers; }

	protected void setModifiers(int modifiers) { this.modifiers = modifiers; }

	public Optional<Point2D> getClient() { return Optional.ofNullable(client); }

	protected void setClient(Point2D client) { this.client = client; }

	public Optional<Point2D> getScreen() { return Optional.ofNullable(screen); }

	protected void setScreen(Point2D screen) { this.screen = screen; }

	public static class Wheel extends UIEventMouseDOMLike {
		public static final String
				TYPE_WHEEL = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "wheel";

		/* SECTION DeltaModeCode */
		public static final int
				DOM_DELTA_PIXEL = 0x00,
				DOM_DELTA_LINE = 0X01,
				DOM_DELTA_PAGE = 0X02;

		protected double
				deltaX,
				deltaY,
				deltaZ;
		protected long deltaMode;

		public void initWheelEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, @Nullable AbstractView viewArg, int detailArg, Point2D screen, Point2D client, int modifiers, short buttonArg, @Nullable EventTarget relatedTargetArg, double deltaXArg, double deltaYArg, double deltaZArg, long deltaModeArg) {
			setDeltaX(deltaXArg);
			setDeltaY(deltaYArg);
			setDeltaZ(deltaZArg);
			setDeltaMode(deltaModeArg);

			initMouseEvent(typeArg, canBubbleArg, cancelableArg, viewArg, detailArg, screen, client, modifiers, buttonArg, relatedTargetArg);
		}

		public double getDeltaX() { return deltaX; }

		protected void setDeltaX(double deltaX) { this.deltaX = deltaX; }

		public double getDeltaY() { return deltaY; }

		protected void setDeltaY(double deltaY) { this.deltaY = deltaY; }

		public double getDeltaZ() { return deltaZ; }

		protected void setDeltaZ(double deltaZ) { this.deltaZ = deltaZ; }

		public long getDeltaMode() { return deltaMode; }

		protected void setDeltaMode(long deltaMode) { this.deltaMode = deltaMode; }
	}
}
