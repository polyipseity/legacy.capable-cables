package $group__.client.ui.mvvm.views.domlike.events;

import $group__.utilities.NamespaceUtilities;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

import javax.annotation.Nullable;

public class UIEventUIDOMLike extends UIEventDOMLike implements UIEvent {
	public static final String TYPE_SELECT = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "select";

	@Nullable
	protected AbstractView view;
	protected int detail;

	@Override
	@Nullable
	public AbstractView getView() { return view; }

	@Override
	public int getDetail() { return detail; }

	@Override
	public void initUIEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, @Nullable AbstractView viewArg, int detailArg) {
		setView(viewArg);
		setDetail(detailArg);

		initEvent(typeArg, canBubbleArg, cancelableArg);
	}

	protected void setDetail(int detail) { this.detail = detail; }

	protected void setView(@Nullable AbstractView view) { this.view = view; }

	public static class Focus extends UIEventUIDOMLike {
		@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
		public static final String
				TYPE_FOCUS_OUT_POST = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "blur",
				TYPE_FOCUS_IN_POST = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "focus",
				TYPE_FOCUS_IN_PRE = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "focusin",
				TYPE_FOCUS_OUT_PRE = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "focusout";

		@Nullable
		protected EventTarget relatedTarget;

		public void initFocusEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, @Nullable AbstractView viewArg, int detailArg, @Nullable EventTarget relatedTargetArg) {
			setRelatedTarget(relatedTargetArg);

			initUIEvent(typeArg, canBubbleArg, cancelableArg, viewArg, detailArg);
		}

		@Nullable
		public EventTarget getRelatedTarget() { return relatedTarget; }

		protected void setRelatedTarget(@Nullable EventTarget relatedTarget) { this.relatedTarget = relatedTarget; }
	}

	public static class Keyboard extends UIEventUIDOMLike {
		/* CODE
		// KeyLocationCode
		const unsigned long DOM_KEY_LOCATION_STANDARD = 0x00;
		const unsigned long DOM_KEY_LOCATION_LEFT = 0x01;
		const unsigned long DOM_KEY_LOCATION_RIGHT = 0x02;
		const unsigned long DOM_KEY_LOCATION_NUMPAD = 0x03;



		readonly attribute unsigned long location;

		readonly attribute boolean ctrlKey;
		readonly attribute boolean shiftKey;
		readonly attribute boolean altKey;
		readonly attribute boolean metaKey;
		*/

		/* CODE
		boolean getModifierState(DOMString keyArg);
		 */

		@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
		public static final String
				TYPE_KEY_DOWN = "keydown",
				TYPE_KEY_UP = "keyup";

		protected boolean
				repeat,
				isComposing;
		protected int
				key,
				scanCode,
				modifiers;

		public boolean isRepeat() { return repeat; }

		protected void setRepeat(boolean repeat) { this.repeat = repeat; }

		public boolean isComposing() { return isComposing; }

		protected void setComposing(boolean composing) { isComposing = composing; }

		public int getKey() { return key; }

		protected void setKey(int key) { this.key = key; }

		public int getScanCode() { return scanCode; }

		protected void setScanCode(int scanCode) { this.scanCode = scanCode; }

		public int getModifiers() { return modifiers; }

		protected void setModifiers(int modifiers) { this.modifiers = modifiers; }

		public void initKeyboardEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, @Nullable AbstractView viewArg, int detailArg, boolean repeatArg, boolean isComposingArg, int key, int scanCode, int modifiers) {
			setRepeat(repeatArg);
			setComposing(isComposingArg);
			setKey(key);
			setScanCode(scanCode);
			setModifiers(modifiers);

			initUIEvent(typeArg, canBubbleArg, cancelableArg, viewArg, detailArg);
		}
	}
}
