package $group__.ui.mvvm;

import $group__.ui.core.mvvm.IUIInfrastructure;
import $group__.ui.core.mvvm.viewmodels.IUIViewModel;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.utilities.binding.core.BindingTransformerNotFoundException;
import $group__.utilities.binding.core.IBinder;
import $group__.utilities.binding.core.IBinderAction;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		implements IUIInfrastructure<V, VM, B> {
	protected final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final CompositeDisposable binderDisposables = new CompositeDisposable();
	protected V view;
	protected VM viewModel;
	protected B binder;
	protected boolean bound = false;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public UIInfrastructure(V view, VM viewModel, B binder) {
		this.view = view;
		this.viewModel = viewModel;
		this.binder = binder;

		view.setInfrastructure(this);
		viewModel.setInfrastructure(this);
	}

	@Override
	public V getView() { return view; }

	@Override
	public VM getViewModel() { return viewModel; }

	@Override
	public B getBinder() { return binder; }

	@Override
	public void setView(V view) {
		StaticHolder.checkBoundState(isBound(), false);
		getView().setInfrastructure(null);
		this.view = view;
		getView().setInfrastructure(this);
	}

	@Override
	public void setViewModel(VM viewModel) {
		StaticHolder.checkBoundState(isBound(), false);
		getViewModel().setInfrastructure(null);
		this.viewModel = viewModel;
		getViewModel().setInfrastructure(this);
	}

	@Override
	public void setBinder(B binder) {
		StaticHolder.checkBoundState(isBound(), false);
		this.binder = binder;
	}

	@Override
	public boolean isBound() { return bound; }

	protected void setBound(boolean bound) { this.bound = bound; }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void bind() {
		StaticHolder.checkBoundState(isBound(), false);

		// COMMENT must bind the bindings of view first
		getBinder().bind(Iterables.concat(
				// COMMENT fields
				getView().getBindingFields(), getView().getBindingMethods(),
				// COMMENT methods
				getViewModel().getBindingFields(), getViewModel().getBindingMethods()));

		Streams.stream(
				Iterables.concat(getView().getBinderNotifiers(), getViewModel().getBinderNotifiers())).unordered().distinct()
				.forEach(n ->
						n.subscribe(createBinderActionObserver()));

		setBound(true);
	}

	protected DisposableObserver<IBinderAction> createBinderActionObserver() {
		DisposableObserver<IBinderAction> d = createBinderActionObserver(getBinder());
		getBinderDisposables().add(d);
		return d;
	}

	@Override
	public void unbind() {
		StaticHolder.checkBoundState(isBound(), true);

		getBinderDisposables().clear();
		getBinder().unbindAll();

		setBound(false);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) { return IExtensionContainer.StaticHolder.addExtensionImpl(this, getExtensions(), extension.getType().getKey(), extension); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.StaticHolder.removeExtensionImpl(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.StaticHolder.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	public static DisposableObserver<IBinderAction> createBinderActionObserver(IBinder binder) {
		return new DisposableObserverAuto<IBinderAction>() {
			@Override
			public void onNext(@Nonnull IBinderAction o) {
				try {
					switch (o.getActionType()) {
						case BIND:
							binder.bind(o.getBindings());
							break;
						case UNBIND:
							binder.unbind(o.getBindings());
							break;
						default:
							onError(new InternalError());
							break;
					}
				} catch (BindingTransformerNotFoundException ex) {
					onError(ex);
				}
			}
		};
	}

	protected CompositeDisposable getBinderDisposables() { return binderDisposables; }
}
