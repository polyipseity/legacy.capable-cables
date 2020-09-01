package $group__.ui.mvvm;

import $group__.ui.core.mvvm.IUIInfrastructure;
import $group__.ui.core.mvvm.binding.IBinder;
import $group__.ui.core.mvvm.binding.IBinderAction;
import $group__.ui.core.mvvm.viewmodels.IUIViewModel;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		implements IUIInfrastructure<V, VM, B> {
	protected final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final Set<Disposable> binderDisposables = new HashSet<>(2);
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
		IUIInfrastructure.checkBoundState(isBound(), false);
		getView().setInfrastructure(null);
		this.view = view;
		getView().setInfrastructure(this);
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

	@Override
	public boolean isBound() { return bound; }

	protected void setBound(boolean bound) { this.bound = bound; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Disposable> getBinderDisposables() { return binderDisposables; }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void bind() {
		IUIInfrastructure.checkBoundState(isBound(), false);

		// COMMENT must bind the bindings of view first
		getBinder().bindFields(Iterables.concat(getView().getBindingFields(), getViewModel().getBindingFields()));
		getBinder().bindMethods(Iterables.concat(getView().getBindingMethods(), getViewModel().getBindingMethods()));

		Supplier<? extends Observer<? super IBinderAction>> s = () -> {
			DisposableObserver<IBinderAction> d = IBinder.createBinderActionObserver(getBinder());
			getBinderDisposables().add(d);
			return d;
		};
		Streams.stream(getView().getBinderNotifiers()).unordered().distinct()
				.forEach(n ->
						n.subscribe(s.get()));
		Streams.stream(getViewModel().getBinderNotifiers()).unordered().distinct()
				.forEach(n ->
						n.subscribe(s.get()));

		setBound(true);
	}

	@Override
	public void unbind() {
		IUIInfrastructure.checkBoundState(isBound(), true);

		getBinderDisposables().stream().unordered()
				.forEach(Disposable::dispose);
		getBinderDisposables().clear();

		getBinder().unbindAll();

		setBound(false);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) { return IExtensionContainer.addExtensionImpl(this, getExtensions(), extension.getType().getKey(), extension); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }
}
