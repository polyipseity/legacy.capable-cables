package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class UIDefaultInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		implements IUIInfrastructure<V, VM, B> {
	private final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final CompositeDisposable binderDisposables = new CompositeDisposable();
	private V view;
	private VM viewModel;
	private B binder;
	private boolean bound = false;

	private final Supplier<@Nonnull Optional<DisposableObserver<IBinderAction>>> binderObserverSupplier;

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		return IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.removeExtensionImpl(getExtensions(), key); }

	@Override
	public V getView() { return view; }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@Override
	public VM getViewModel() { return viewModel; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	@Override
	public B getBinder() { return binder; }

	@SuppressWarnings({"ThisEscapedInObjectConstruction", "RedundantTypeArguments"})
	public UIDefaultInfrastructure(V view, VM viewModel, B binder) {
		this.view = view;
		this.viewModel = viewModel;
		this.binder = binder;

		view.setInfrastructure(this);
		viewModel.setInfrastructure(this);

		OptionalWeakReference<UIDefaultInfrastructure<V, VM, B>> thisWeakReference = new OptionalWeakReference<>(this);
		this.binderObserverSupplier = () -> thisWeakReference.getOptional().map(UIDefaultInfrastructure<V, VM, B>::createBinderActionObserver);
	}

	protected DisposableObserver<IBinderAction> createBinderActionObserver() {
		return new BinderActionDelegatingDisposableObserver(createBinderActionObserver(getBinder()), getBinderDisposables());
	}

	@Override
	public void bind(IUIContextContainer contextContainer) {
		IUIInfrastructure.checkBoundState(isBound(), false);

		getView().setContext(contextContainer.getViewContext());
		getViewModel().setContext(contextContainer.getViewModelContext());

		// COMMENT must bind the bindings of view first to ensure that the default values are from the view
		getView().initializeBindings(getBinderObserverSupplier());
		getViewModel().initializeBindings(getBinderObserverSupplier());

		setBound(true);
	}

	protected Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> getBinderObserverSupplier() {
		return binderObserverSupplier;
	}

	@Override
	public void setView(V view) {
		IUIInfrastructure.checkBoundState(isBound(), false);
		getView().setInfrastructure(null);
		this.view = view;
		getView().setInfrastructure(this);
	}

	@Override
	public boolean isBound() { return bound; }

	protected void setBound(boolean bound) { this.bound = bound; }

	public static DisposableObserver<IBinderAction> createBinderActionObserver(IBinder binder) {
		return new LoggingDisposableObserver<>(new DefaultDisposableObserver<IBinderAction>() {
			@Override
			public void onNext(@Nonnull IBinderAction o) {
				switch (o.getActionType()) {
					case BIND:
						try {
							binder.bind(o.getBindings());
						} catch (NoSuchBindingTransformerException e) {
							onError(e);
						}
						break;
					case UNBIND:
						binder.unbind(o.getBindings());
						break;
					default:
						onError(new AssertionError());
						break;
				}
			}
		}, UIConfiguration.getInstance().getLogger());
	}

	@Override
	public void unbind() {
		IUIInfrastructure.checkBoundState(isBound(), true);

		getViewModel().cleanupBindings(getBinderObserverSupplier());
		getView().cleanupBindings(getBinderObserverSupplier());

		getViewModel().setContext(null);
		getView().setContext(null);

		getBinderDisposables().clear();
		getBinder().unbindAll();

		setBound(false);
	}

	@Override
	public void setViewModel(VM viewModel) {
		IUIInfrastructure.checkBoundState(isBound(), false);
		getViewModel().setInfrastructure(null);
		this.viewModel = viewModel;
		getViewModel().setInfrastructure(this);
	}

	@Override
	public void setBinder(B binder) {
		IUIInfrastructure.checkBoundState(isBound(), false);
		this.binder = binder;
	}

	protected CompositeDisposable getBinderDisposables() { return binderDisposables; }

	protected static final class BinderActionDelegatingDisposableObserver
			extends DelegatingDisposableObserver<IBinderAction> {
		private final CompositeDisposable binderDisposables;

		public BinderActionDelegatingDisposableObserver(DisposableObserver<? super IBinderAction> delegate, CompositeDisposable binderDisposables) {
			super(delegate);
			this.binderDisposables = binderDisposables;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		protected void onStart() {
			super.onStart();
			getBinderDisposables().add(this);
		}

		protected CompositeDisposable getBinderDisposables() {
			return binderDisposables;
		}
	}

	@Override
	public void initialize() {
		getViewModel().initialize();
		getView().initialize();
	}

	@Override
	public void removed() {
		getViewModel().removed();
		getView().removed();
	}
}
