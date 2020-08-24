package $group__.ui.core.mvvm;

import $group__.ui.core.mvvm.binding.IBinder;
import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.viewmodels.IUIViewModel;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

public interface IUIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		extends IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIInfrastructure<?, ?, ?>>> {
	static void checkBoundState(boolean bound, boolean expected) {
		if (bound != expected)
			throw new IllegalStateException(bound ? "View and view-model is bound" : "View and view-model is not bound");
	}

	V getView();

	void setView(V view);

	VM getViewModel();

	void setViewModel(VM viewModel);

	B getBinder();

	void setBinder(B binder);

	void bind();

	void unbind();

	boolean isBound();

	@SuppressWarnings("UnusedReturnValue")
	static boolean bindSafe(IUIInfrastructure<?, ?, ?> infrastructure) {
		if (!infrastructure.isBound()) {
			infrastructure.bind();
			return true;
		}
		return false;
	}

	@SuppressWarnings("UnusedReturnValue")
	static boolean unbindSafe(IUIInfrastructure<?, ?, ?> infrastructure) {
		if (infrastructure.isBound()) {
			infrastructure.unbind();
			return true;
		}
		return false;
	}
}
