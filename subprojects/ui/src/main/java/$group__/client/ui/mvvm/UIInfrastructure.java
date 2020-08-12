package $group__.client.ui.mvvm;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.viewmodels.IUIViewModel;
import $group__.client.ui.mvvm.core.views.IUIView;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		implements IUIInfrastructure<V, VM, B> {
	protected final ConcurrentMap<ResourceLocation, IUIExtension<? extends IUIInfrastructure<?, ?, ?>>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final Set<Disposable> binderDisposables = new HashSet<>(2);
	protected V view;
	protected VM viewModel;
	protected B binder;
	protected boolean bound = false;

	public UIInfrastructure(V view, VM viewModel, B binder) {
		this.view = view;
		this.viewModel = viewModel;
		this.binder = binder;

		getView().setInfrastructure(this);
		getViewModel().setInfrastructure(this);
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

	@Override
	public void bind() {
		IUIInfrastructure.checkBoundState(isBound(), false);

		getBinder().bindFields(Iterables.concat(getView().getBindingFields(), getViewModel().getBindingFields()));
		getBinder().bindMethods(Iterables.concat(getView().getBindingMethods(), getViewModel().getBindingMethods()));

		DisposableObserver<IBinderAction> d = IBinder.createBinderActionObserver(getBinder());
		getBinderDisposables().add(d);
		getView().getBinderNotifier().subscribe(d);
		d = IBinder.createBinderActionObserver(getBinder());
		getBinderDisposables().add(d);
		getViewModel().getBinderNotifier().subscribe(d);

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

	@Override
	public Optional<IUIExtension<? extends IUIInfrastructure<?, ?, ?>>> getExtension(ResourceLocation key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Optional<IUIExtension<? extends IUIInfrastructure<?, ?, ?>>> addExtension(IUIExtension<? extends IUIInfrastructure<?, ?, ?>> extension) {
		IExtension.RegExtension.checkExtensionRegistered(extension);
		return IExtensionContainer.addExtension(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<IUIExtension<? extends IUIInfrastructure<?, ?, ?>>> removeExtension(ResourceLocation key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Map<ResourceLocation, IUIExtension<? extends IUIInfrastructure<?, ?, ?>>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, IUIExtension<? extends IUIInfrastructure<?, ?, ?>>> getExtensions() { return extensions; }
}
