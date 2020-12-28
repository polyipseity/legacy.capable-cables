package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.IUIActiveLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.IUIStructureLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;

import java.util.function.Supplier;

public interface IUIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		extends
		IUIStructureLifecycle<IUIContextContainer, @AlwaysNull @Nullable Void>, IUIActiveLifecycle<@AlwaysNull @Nullable Void, @AlwaysNull @Nullable Void>,
		IExtensionContainer<IIdentifier> {
	static <T extends IUIInfrastructure<V, VM, B>,
			V extends IUIView<?>,
			VM extends IUIViewModel<?>,
			B extends IBinder> T create(Supplier<@Nonnull ? extends T> constructor,
	                                    V view,
	                                    VM viewModel,
	                                    B binder) {
		T result = constructor.get();
		result.setView(view);
		result.setViewModel(viewModel);
		result.setBinder(binder);
		return result;
	}

	V getView();

	void setView(V view);

	VM getViewModel();

	void setViewModel(VM viewModel);

	B getBinder();

	void setBinder(B binder);
}
