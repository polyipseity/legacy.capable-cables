package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.Optional;

@Immutable
public final class UIImmutableExtensionArguments
		implements IUIExtensionArguments {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final Class<?> containerClass;
	private final @Nullable String rendererName;

	private UIImmutableExtensionArguments(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings,
	                                      Class<?> containerClass,
	                                      @Nullable @NonNls CharSequence rendererName) {
		this.mappings = ImmutableMap.copyOf(mappings);
		this.containerClass = containerClass;
		this.rendererName = Optional.ofNullable(rendererName).map(CharSequence::toString).orElse(null);
	}

	public static UIImmutableExtensionArguments of(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings,
	                                               Class<?> containerClass,
	                                               @Nullable @NonNls CharSequence rendererName) {
		return new UIImmutableExtensionArguments(mappings, containerClass, rendererName);
	}

	@Override
	public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }

	@Override
	public Class<?> getContainerClass() { return containerClass; }

	@Override
	public Optional<? extends String> getRendererName() {
		return Optional.ofNullable(rendererName);
	}
}
