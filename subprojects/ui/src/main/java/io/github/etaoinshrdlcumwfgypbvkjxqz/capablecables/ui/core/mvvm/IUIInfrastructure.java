package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import java.util.ResourceBundle;

public interface IUIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		extends IExtensionContainer<INamespacePrefixedString> {
	V getView();

	void setView(V view);

	VM getViewModel();

	void setViewModel(VM viewModel);

	B getBinder();

	void setBinder(B binder);

	void bind()
			throws NoSuchBindingTransformerException;

	void unbind();

	boolean isBound();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		@SuppressWarnings("UnusedReturnValue")
		public static boolean bindSafe(IUIInfrastructure<?, ?, ?> infrastructure)
				throws NoSuchBindingTransformerException {
			if (!infrastructure.isBound()) {
				infrastructure.bind();
				return true;
			}
			return false;
		}

		@SuppressWarnings("UnusedReturnValue")
		public static boolean unbindSafe(IUIInfrastructure<?, ?, ?> infrastructure) {
			if (infrastructure.isBound()) {
				infrastructure.unbind();
				return true;
			}
			return false;
		}

		public static void checkBoundState(boolean bound, boolean expected)
				throws IllegalStateException {
			if (bound != expected)
				throw new IllegalStateException(
						new LogMessageBuilder()
								.addMarkers(UIMarkers.getInstance()::getMarkerUIInfrastructure)
								.addKeyValue("bound", bound).addKeyValue("expected", expected)
								.addMessages(() -> getResourceBundle().getString(bound ? "check.bound.true" : "check.bound.false"))
								.build());
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
