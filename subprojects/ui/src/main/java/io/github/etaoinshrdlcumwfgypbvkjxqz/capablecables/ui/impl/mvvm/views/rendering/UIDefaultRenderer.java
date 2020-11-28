package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinderObserverSupplierHolder;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class UIDefaultRenderer<C>
		implements IUIRenderer<C> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<C> typeToken;
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final IBinderObserverSupplierHolder binderObserverSupplierHolder = new DefaultBinderObserverSupplierHolder();
	private OptionalWeakReference<C> container = OptionalWeakReference.of(null);

	@SuppressWarnings({"unchecked", "UnstableApiUsage"})
	@UIRendererConstructor
	public UIDefaultRenderer(IUIRendererArguments arguments) {
		this.typeToken = ((TypeToken<C>) TypeToken.of(arguments.getContainerClass()));

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);
	}

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	public void onRendererAdded(C container) {
		setContainer(container);
	}

	@Override
	public void onRendererRemoved() {
		setContainer(null);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<? extends C> getTypeToken() {
		return typeToken;
	}

	public Optional<? extends C> getContainer() { return container.getOptional(); }

	public void setContainer(@Nullable C container) { this.container = OptionalWeakReference.of(container); }

	@Override
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRenderer.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setValue(binderObserverSupplier);
	}

	@Override
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().setValue(null);
		IUIRenderer.super.cleanupBindings();
	}

	protected IBinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}
}
