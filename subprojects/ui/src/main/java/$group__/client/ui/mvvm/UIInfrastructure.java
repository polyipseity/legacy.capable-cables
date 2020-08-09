package $group__.client.ui.mvvm;

import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.viewmodels.IUIViewModel;
import $group__.client.ui.mvvm.core.views.IUIView;
import com.google.common.collect.Iterables;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.HashSet;
import java.util.Set;

public class UIInfrastructure<V extends IUIView, VM extends IUIViewModel<?>, B extends IBinder>
		implements IUIInfrastructure<V, VM, B> {
	protected final Set<Disposable> binderDisposables = new HashSet<>(2);
	protected V view;
	protected VM viewModel;
	protected B binder;
	protected boolean bound = false;

	public UIInfrastructure(V view, VM viewModel, B binder) {
		this.view = view;
		this.viewModel = viewModel;
		this.binder = binder;
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
		this.view = view;
	}

	@Override
	public void setViewModel(VM viewModel) {
		IUIInfrastructure.checkBoundState(isBound(), false);
		this.viewModel = viewModel;
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

	@Override
	public void bind() {
		IUIInfrastructure.checkBoundState(isBound(), false);

		getBinder().bindFields(Iterables.concat(getView().getBindingFields(), getViewModel().getBindingFields()));
		getBinder().bindMethods(Iterables.concat(getView().getBindingMethods(), getViewModel().getBindingMethods()));

		DisposableObserver<IBinderAction> d = IBinder.createBinderActionObserver(getBinder());
		getBinderDisposables().add(d);
		getView().subscribe(d);
		d = IBinder.createBinderActionObserver(getBinder());
		getBinderDisposables().add(d);
		getViewModel().subscribe(d);

		setBound(true);
	}

	@Override
	public void unbind() {
		IUIInfrastructure.checkBoundState(isBound(), true);

		getBinderDisposables().forEach(Disposable::dispose);
		getBinderDisposables().clear();

		getBinder().unbindAll();

		setBound(false);
	}
}
