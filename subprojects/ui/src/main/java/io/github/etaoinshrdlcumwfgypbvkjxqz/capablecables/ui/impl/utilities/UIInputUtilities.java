package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.def.IMouseButtonClickData;

public enum UIInputUtilities {
	;

	public static final long DOUBLE_CLICK_INTERVAL_MAX = 250L;

	public static boolean isDoubleClick(IMouseButtonClickData previous, IMouseButtonClickData current) {
		// TODO add accessibility setting to set double click interval
		return current.getButton() == previous.getButton()
				&& current.getCursorPositionView().equals(previous.getCursorPositionView())
				&& current.getTimestamp() - previous.getTimestamp() <= getDoubleClickIntervalMax();
	}

	public static long getDoubleClickIntervalMax() {
		return DOUBLE_CLICK_INTERVAL_MAX;
	}
}
