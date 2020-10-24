package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class UIDefaultRenderer<C>
		implements IUIRenderer<C> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<C> typeToken;
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private OptionalWeakReference<C> container = new OptionalWeakReference<>(null);

	@SuppressWarnings({"unchecked", "UnstableApiUsage"})
	@UIRendererConstructor
	public UIDefaultRenderer(UIRendererConstructor.IArguments arguments) {
		this.typeToken = ((TypeToken<C>) TypeToken.of(arguments.getContainerClass()));

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<? extends C> getTypeToken() {
		return typeToken;
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

	public Optional<? extends C> getContainer() { return container.getOptional(); }

	public void setContainer(@Nullable C container) { this.container = new OptionalWeakReference<>(container); }
}
