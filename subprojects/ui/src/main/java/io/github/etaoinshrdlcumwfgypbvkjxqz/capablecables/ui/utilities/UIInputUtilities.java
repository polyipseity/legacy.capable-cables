package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataMouseButtonClick;

public enum UIInputUtilities {
	;

	public static final long DOUBLE_CLICK_INTERVAL_MAX = 250L;

	public static boolean isDoubleClick(IUIDataMouseButtonClick previous, IUIDataMouseButtonClick current) {
		// TODO add accessibility setting to set double click interval
		return current.getButton() == previous.getButton()
				&& current.getCursorPositionView().equals(previous.getCursorPositionView())
				&& current.getTimestampMills() - previous.getTimestampMills() <= getDoubleClickIntervalMax();
	}

	public static long getDoubleClickIntervalMax() {
		return DOUBLE_CLICK_INTERVAL_MAX;
	}
}
