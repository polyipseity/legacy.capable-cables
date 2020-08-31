package $group__.ui.core.mvvm.views.events;

import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

public interface IUIEvent {
	String
			TYPE_SELECT_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "select";
	INamespacePrefixedString
			TYPE_SELECT = new NamespacePrefixedString(TYPE_SELECT_STRING);

	INamespacePrefixedString getType();

	IUIEventTarget getTarget();

	EnumPhase getPhase();

	void advancePhase()
			throws IllegalStateException;

	void reset();

	boolean canBubble();

	boolean isPropagationStopped();

	void stopPropagation();

	boolean isCancelable();

	boolean isDefaultPrevented();

	@SuppressWarnings("UnusedReturnValue")
	boolean preventDefault();

	enum EnumPhase {
		NONE {
			@Override
			public EnumPhase getNextPhase() { return CAPTURING_PHASE; }
		},
		CAPTURING_PHASE {
			@Override
			public EnumPhase getNextPhase() { return AT_TARGET; }
		},
		AT_TARGET {
			@Override
			public EnumPhase getNextPhase() { return BUBBLING_PHASE; }
		},
		BUBBLING_PHASE {
			@Override
			public EnumPhase getNextPhase()
					throws IllegalStateException { throw new IllegalStateException(); }
		},
		;

		public abstract EnumPhase getNextPhase()
				throws IllegalStateException;
	}
}
