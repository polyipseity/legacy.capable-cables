package $group__.client.ui.mvvm.core;

import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.core.viewmodels.IUIViewModel;
import $group__.client.ui.mvvm.core.views.IUIView;

public interface IUIInfrastructure<V extends IUIView, VM extends IUIViewModel<?>, B extends IBinder> {
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
}
