package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

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

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void bind()
			throws NoSuchBindingTransformerException {
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

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.StaticHolder.removeExtensionImpl(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.StaticHolder.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		return IExtensionContainer.StaticHolder.addExtensionImpl(this, getExtensions(), extension.getType().getKey(), extension);
	}

	protected CompositeDisposable getBinderDisposables() { return binderDisposables; }
}
