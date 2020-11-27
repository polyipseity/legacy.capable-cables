package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.IUILifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UIDefaultLifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UILifecycleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public abstract class UIAbstractSubInfrastructure<C extends IUISubInfrastructureContext>
		implements IUISubInfrastructure<C> {
	private final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final IUILifecycleStateTracker lifecycleStateTracker = new UIDefaultLifecycleStateTracker();
	private final IBinderObserverSupplierHolder binderObserverSupplierHolder = new DefaultBinderObserverSupplierHolder();
	private OptionalWeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = new OptionalWeakReference<>(null);
	@Nullable
	private C context;

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> result = IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndInitializeBindings(binderObserverSupplier, ImmutableList.of(extension)));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) {
		Optional<IExtension<? extends INamespacePrefixedString, ?>> result = IExtensionContainer.removeExtensionImpl(getExtensions(), key);
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndCleanupBindings(result.map(ImmutableList::of).orElseGet(ImmutableList::of)));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	protected IBinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	@Override
	public Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure() {
		return infrastructure.getOptional();
	}

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) {
		this.infrastructure = new OptionalWeakReference<>(infrastructure);
	}

	@Override
	public Optional<? extends C> getContext() {
		return Optional.ofNullable(context);
	}

	@Override
	public void setContext(@Nullable C context) {
		this.context = context;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public final void bind(IUIStructureLifecycleContext context) {
		UILifecycleUtilities.addStateIdempotent(this, EnumUILifecycleState.BOUND, context, true, context1 -> {
			IUISubInfrastructure.super.bind(context1);
			bind0(context1);
		});
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	@OverridingMethodsMustInvokeSuper
	public final void unbind(@AlwaysNull @Nullable Void context) {
		UILifecycleUtilities.removeStateIdempotent(this, EnumUILifecycleState.BOUND, context, context1 -> {
			unbind0(context1);
			IUISubInfrastructure.super.unbind(context1);
		});
	}

	@OverridingMethodsMustInvokeSuper
	protected void unbind0(@AlwaysNull @Nullable Void context) {
		cleanupBindings();
	}

	@OverridingMethodsMustInvokeSuper
	protected void bind0(IUIStructureLifecycleContext context) {
		initializeBindings(context.getBinderObserverSupplier());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUISubInfrastructure.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setValue(binderObserverSupplier);
		BindingUtilities.findAndInitializeBindings(binderObserverSupplier, getExtensions().values());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndCleanupBindings(getExtensions().values()));
		getBinderObserverSupplierHolder().setValue(null);
		IUISubInfrastructure.super.cleanupBindings();
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	@OverridingMethodsMustInvokeSuper
	public final void initialize(@AlwaysNull @Nullable Void context) {
		UILifecycleUtilities.addStateIdempotent(this, EnumUILifecycleState.INITIALIZED, context, false, context1 -> {
			IUISubInfrastructure.super.initialize(context1);
			initialize0(context1);
		});
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	@OverridingMethodsMustInvokeSuper
	public final void cleanup(@AlwaysNull @Nullable Void context) {
		UILifecycleUtilities.removeStateIdempotent(this, EnumUILifecycleState.INITIALIZED, context, context1 -> {
			cleanup0(context1);
			IUISubInfrastructure.super.cleanup(context1);
		});
	}

	@OverridingMethodsMustInvokeSuper
	protected void cleanup0(@SuppressWarnings("unused") @AlwaysNull @Nullable Void context) {}

	@OverridingMethodsMustInvokeSuper
	protected void initialize0(@SuppressWarnings("unused") @AlwaysNull @Nullable Void context) {}

	@Override
	public IUILifecycleStateTracker getLifecycleStateTracker() {
		return lifecycleStateTracker;
	}
}
