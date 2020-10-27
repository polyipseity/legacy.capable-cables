package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.util.ResourceBundle;

public interface IUIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		extends IExtensionContainer<INamespacePrefixedString> {
	@SuppressWarnings("UnusedReturnValue")
	static boolean bindSafe(IUIInfrastructure<?, ?, ?> infrastructure, IUIContextContainer contextContainer) {
		if (!infrastructure.isBound()) {
			infrastructure.bind(contextContainer);
			return true;
		}
		return false;
	}

	boolean isBound();

	void bind(IUIContextContainer contextContainer);

	@SuppressWarnings("UnusedReturnValue")
	static boolean unbindSafe(IUIInfrastructure<?, ?, ?> infrastructure) {
		if (infrastructure.isBound()) {
			infrastructure.unbind();
			return true;
		}
		return false;
	}

	void unbind();

	static void checkBoundState(boolean bound, boolean expected)
			throws IllegalStateException {
		if (bound != expected)
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIInfrastructure)
							.addKeyValue("bound", bound).addKeyValue("expected", expected)
							.addMessages(bound
									? () -> StaticHolder.getResourceBundle().getString("check.bound.true")
									: () -> StaticHolder.getResourceBundle().getString("check.bound.false"))
							.build());
	}

	void initialize();

	void removed();

	V getView();

	void setView(V view);

	VM getViewModel();

	void setViewModel(VM viewModel);

	B getBinder();

	void setBinder(B binder);

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
