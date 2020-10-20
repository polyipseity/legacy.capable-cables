package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public abstract class UIAbstractSubInfrastructure<C extends IUISubInfrastructureContext>
		implements IUISubInfrastructure<C> {
	private final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private OptionalWeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = new OptionalWeakReference<>(null);
	@Nullable
	private C context;
	@Nullable
	private Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

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
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		return IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.removeExtensionImpl(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	protected Optional<? extends Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() {
		return Optional.ofNullable(binderObserverSupplier);
	}

	protected void setBinderObserverSupplier(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		this.binderObserverSupplier = binderObserverSupplier;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.findAndInitializeBindings(getExtensions().values(), binderObserverSupplier);
	}
}
