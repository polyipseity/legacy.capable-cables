package $group__.client.ui.utilities;

import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;

public enum InputUtilities {
	;

	public static final long DOUBLE_CLICK_INTERVAL_MAX = 250L;

	public static boolean isDoubleClick(IUIDataMouseButtonClick previous, IUIDataMouseButtonClick current) {
		// TODO add accessibility setting to set double click interval
		return current.getCursorPositionView().equals(previous.getCursorPositionView())
				&& current.getTimestampMills() - previous.getTimestampMills() <= DOUBLE_CLICK_INTERVAL_MAX;
	}
}
