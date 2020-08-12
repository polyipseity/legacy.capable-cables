package $group__.client.ui.mvvm.core;

import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.viewmodels.IUIViewModel;
import $group__.client.ui.mvvm.core.views.IUIView;
import $group__.utilities.extensions.IExtensionContainer;
import net.minecraft.util.ResourceLocation;

public interface IUIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		extends IExtensionContainer<ResourceLocation, IUIExtension<? super ResourceLocation, ? extends IUIInfrastructure<?, ?, ?>>> {
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
