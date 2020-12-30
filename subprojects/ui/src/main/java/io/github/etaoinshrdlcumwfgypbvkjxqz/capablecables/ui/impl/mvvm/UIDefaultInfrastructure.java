package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUIActiveLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUILifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUIStructureLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UIDefaultLifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UILifecycleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIDefaultInfrastructure<V extends IUIView<?>, VM extends IUIViewModel<?>, B extends IBinder>
		implements IUIInfrastructure<V, VM, B> {
	private final ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final CompositeDisposable binderDisposables = new CompositeDisposable();
	private final IUILifecycleStateTracker lifecycleStateTracker = new UIDefaultLifecycleStateTracker();
	private final Supplier<@Nonnull Optional<Consumer<? super IBindingAction>>> bindingActionConsumerSupplier;
	private @Nullable V internalView;
	private @Nullable VM internalViewModel;
	private @Nullable B internalBinder;

	public static <V extends IUIView<?>,
			VM extends IUIViewModel<?>,
			B extends IBinder> UIDefaultInfrastructure<V, VM, B> of(V view, VM viewModel, B binder) {
		return IUIInfrastructure.create(UIDefaultInfrastructure::new, view, viewModel, binder);
	}

	@SuppressWarnings("RedundantTypeArguments")
	protected UIDefaultInfrastructure() {
		OptionalWeakReference<UIDefaultInfrastructure<V, VM, B>> thisWeakReference = OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));
		this.bindingActionConsumerSupplier = () -> thisWeakReference.getOptional().map(UIDefaultInfrastructure<V, VM, B>::createBindingActionConsumer);
	}

	protected Consumer<? super IBindingAction> createBindingActionConsumer() {
		return BinderActionConsumer.of(getBinder());
	}

	protected CompositeDisposable getBinderDisposables() { return binderDisposables; }

	protected Optional<? extends B> getInternalBinder() {
		return Optional.ofNullable(internalBinder);
	}

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends IIdentifier, ?>> addExtension(IExtension<? extends IIdentifier, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		return IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends IIdentifier, ?>> removeExtension(IIdentifier key) { return IExtensionContainer.removeExtensionImpl(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends IIdentifier, ?>> getExtension(IIdentifier key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensions() { return extensions; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public final void bind(IUIContextContainer context) {
		UILifecycleUtilities.addStateIdempotent(this, EnumUILifecycleState.BOUND, context, true, context1 -> {
			IUIInfrastructure.super.bind(context1);
			bind0(context1);
		});
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	@OverridingMethodsMustInvokeSuper
	public final void unbind(@AlwaysNull @Nullable Void context) {
		UILifecycleUtilities.removeStateIdempotent(this, EnumUILifecycleState.BOUND, context, context1 -> {
			unbind0(context1);
			IUIInfrastructure.super.unbind(context1);
		});
	}

	@OverridingMethodsMustInvokeSuper
	protected void unbind0(@SuppressWarnings("unused") @AlwaysNull @Nullable Void context) {
		IUIStructureLifecycle.unbindV(getViewModel());
		IUIStructureLifecycle.unbindV(getView());

		getViewModel().setContext(null);
		getView().setContext(null);

		getBinderDisposables().clear();
		getBinder().unbindAll();
	}

	@OverridingMethodsMustInvokeSuper
	protected void bind0(IUIContextContainer contextContainer) {
		getView().setContext(contextContainer.getViewContext());
		getViewModel().setContext(contextContainer.getViewModelContext());

		// COMMENT must bind the bindings of view first to ensure that the default values are from the view
		IUIStructureLifecycleContext structureLifecycleContext =
				UIImmutableStructureLifecycleContext.of(getBindingActionConsumerSupplier());
		getView().bind(structureLifecycleContext);
		getViewModel().bind(structureLifecycleContext);
	}

	@Override
	public V getView() {
		return getInternalView().orElseThrow(IllegalStateException::new);
	}

	protected Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> getBindingActionConsumerSupplier() {
		return bindingActionConsumerSupplier;
	}

	protected Optional<? extends V> getInternalView() {
		return Optional.ofNullable(internalView);
	}

	protected Optional<? extends VM> getInternalViewModel() {
		return Optional.ofNullable(internalViewModel);
	}

	@Override
	public void setView(V view) {
		IUIStructureLifecycle.checkBoundState(getLifecycleStateTracker().containsState(EnumUILifecycleState.BOUND), false);
		getInternalView().ifPresent(internalView -> internalView.setInfrastructure(null));
		this.internalView = view;
		getView().setInfrastructure(this);
	}

	@Override
	public VM getViewModel() {
		return getInternalViewModel().orElseThrow(IllegalStateException::new);
	}

	@Override
	public void setViewModel(VM viewModel) {
		IUIStructureLifecycle.checkBoundState(getLifecycleStateTracker().containsState(EnumUILifecycleState.BOUND), false);
		getInternalViewModel().ifPresent(internalViewModel -> internalViewModel.setInfrastructure(null));
		this.internalViewModel = viewModel;
		getViewModel().setInfrastructure(this);
	}

	@Override
	public B getBinder() {
		return getInternalBinder().orElseThrow(IllegalStateException::new);
	}

	@Override
	public void setBinder(B binder) {
		IUIStructureLifecycle.checkBoundState(getLifecycleStateTracker().containsState(EnumUILifecycleState.BOUND), false);
		this.internalBinder = binder;
	}

	@Override
	public IUILifecycleStateTracker getLifecycleStateTracker() {
		return lifecycleStateTracker;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	@OverridingMethodsMustInvokeSuper
	public final void initialize(@AlwaysNull @Nullable Void context) {
		UILifecycleUtilities.addStateIdempotent(this, EnumUILifecycleState.INITIALIZED, context, false, context1 -> {
			IUIInfrastructure.super.initialize(context1);
			initialize0(context1);
		});
	}

	@OverridingMethodsMustInvokeSuper
	protected void initialize0(@SuppressWarnings("unused") @AlwaysNull @Nullable Void context) {
		IUIActiveLifecycle.initializeV(getView());
		IUIActiveLifecycle.initializeV(getViewModel());
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	@OverridingMethodsMustInvokeSuper
	public final void cleanup(@AlwaysNull @Nullable Void context) {
		UILifecycleUtilities.removeStateIdempotent(this, EnumUILifecycleState.INITIALIZED, context, context1 -> {
			cleanup0(context1);
			IUIInfrastructure.super.cleanup(context1);
		});
	}

	@OverridingMethodsMustInvokeSuper
	protected void cleanup0(@SuppressWarnings("unused") @AlwaysNull @Nullable Void context) {
		IUIActiveLifecycle.cleanupV(getViewModel());
		IUIActiveLifecycle.cleanupV(getView());
	}

	public static class BinderActionConsumer
			implements Consumer<IBindingAction> {
		private final OptionalWeakReference<IBinder> binder;

		public BinderActionConsumer(IBinder binder) {
			this.binder = OptionalWeakReference.of(binder);
		}

		public static BinderActionConsumer of(IBinder binder) {
			return new BinderActionConsumer(binder);
		}

		@Override
		public void accept(IBindingAction action) {
			getBinder().ifPresent(action);
		}

		protected Optional<? extends IBinder> getBinder() {
			return binder.getOptional();
		}
	}
}
