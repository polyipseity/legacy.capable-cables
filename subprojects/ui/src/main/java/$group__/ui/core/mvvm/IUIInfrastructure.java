package $group__.ui.core.mvvm;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.viewmodels.IUIViewModel;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.binding.core.IBinder;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.templates.CommonConfigurationTemplate;

import java.util.ResourceBundle;

public interface IUIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		extends IExtensionContainer<INamespacePrefixedString> {
	V getView();

	void setView(V view);

	VM getViewModel();

	void setViewModel(VM viewModel);

	B getBinder();

	void setBinder(B binder);

	void bind();

	void unbind();

	boolean isBound();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		@SuppressWarnings("UnusedReturnValue")
		public static boolean bindSafe(IUIInfrastructure<?, ?, ?> infrastructure) {
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
				throw ThrowableUtilities.logAndThrow(
						new IllegalStateException(
								new LogMessageBuilder()
										.addKeyValue("bound", bound).addKeyValue("expected", expected)
										.appendMessages(getResourceBundle().getString(bound ? "check.bound.true" : "check.bound.false"))
										.build()),
						UIConfiguration.getInstance().getLogger());
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
