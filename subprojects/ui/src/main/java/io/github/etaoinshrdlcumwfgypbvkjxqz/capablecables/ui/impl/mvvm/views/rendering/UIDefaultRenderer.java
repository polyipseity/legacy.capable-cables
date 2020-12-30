package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBindingActionConsumerSupplierHolder;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIDefaultRenderer<C>
		implements IUIRenderer<C> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<C> typeToken;
	private final Map<IIdentifier, IUIPropertyMappingValue> mappings;
	private final IBindingActionConsumerSupplierHolder bindingActionConsumerSupplierHolder = new DefaultBindingActionConsumerSupplierHolder();
	private OptionalWeakReference<C> container = OptionalWeakReference.of(null);

	@SuppressWarnings({"unchecked", "UnstableApiUsage"})
	@UIRendererConstructor
	public UIDefaultRenderer(IUIRendererArguments arguments) {
		this.typeToken = ((TypeToken<C>) TypeToken.of(arguments.getContainerClass()));

		Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);
	}

	@Override
	public Map<IIdentifier, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<IIdentifier, IUIPropertyMappingValue> getMappings() { return mappings; }

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
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		IUIRenderer.super.initializeBindings(bindingActionConsumerSupplier);
		getBindingActionConsumerSupplierHolder().setValue(bindingActionConsumerSupplier);
	}

	@Override
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().setValue(null);
		IUIRenderer.super.cleanupBindings();
	}

	protected IBindingActionConsumerSupplierHolder getBindingActionConsumerSupplierHolder() {
		return bindingActionConsumerSupplierHolder;
	}
}
