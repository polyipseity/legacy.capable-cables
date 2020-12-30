package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUILifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UIDefaultLifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UILifecycleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class UIAbstractSubInfrastructure<C extends IUISubInfrastructureContext>
		implements IUISubInfrastructure<C> {
	private final ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final IUILifecycleStateTracker lifecycleStateTracker = new UIDefaultLifecycleStateTracker();
	private final IBindingActionConsumerSupplierHolder bindingActionConsumerSupplierHolder = new DefaultBindingActionConsumerSupplierHolder();
	private OptionalWeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = OptionalWeakReference.of(null);
	@Nullable
	private C context;

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends IIdentifier, ?>> addExtension(IExtension<? extends IIdentifier, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		Optional<? extends IExtension<? extends IIdentifier, ?>> result = IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.findAndInitializeBindings(bindingActionConsumer, Iterators.singletonIterator(extension)));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends IIdentifier, ?>> removeExtension(IIdentifier key) {
		Optional<IExtension<? extends IIdentifier, ?>> result = IExtensionContainer.removeExtensionImpl(getExtensions(), key);
		BindingUtilities.findAndCleanupBindings(CollectionUtilities.iterate(result));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends IIdentifier, ?>> getExtension(IIdentifier key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensions() { return extensions; }

	protected IBindingActionConsumerSupplierHolder getBindingActionConsumerSupplierHolder() {
		return bindingActionConsumerSupplierHolder;
	}

	@Override
	public Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure() {
		return infrastructure.getOptional();
	}

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) {
		this.infrastructure = OptionalWeakReference.of(infrastructure);
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
		initializeBindings(context.getBindingActionConsumerSupplier());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		IUISubInfrastructure.super.initializeBindings(bindingActionConsumerSupplier);
		getBindingActionConsumerSupplierHolder().setValue(bindingActionConsumerSupplier);
		BindingUtilities.findAndInitializeBindings(bindingActionConsumerSupplier, getExtensions().values().iterator());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		BindingUtilities.findAndCleanupBindings(getExtensions().values().iterator());
		getBindingActionConsumerSupplierHolder().setValue(null);
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
