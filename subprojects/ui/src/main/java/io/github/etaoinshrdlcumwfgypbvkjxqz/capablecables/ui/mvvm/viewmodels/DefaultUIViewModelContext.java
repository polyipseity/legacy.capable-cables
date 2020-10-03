package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.viewmodels;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModelContext;

public class DefaultUIViewModelContext
		implements IUIViewModelContext {
	@Override
	public DefaultUIViewModelContext copy() {
		return new DefaultUIViewModelContext();
	}
}
