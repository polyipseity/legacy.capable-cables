package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.Optional;

public class UIImmutableComponentEmbedPrototypeArguments
		implements IUIComponentArguments.IEmbedPrototype {
	private final Map<IIdentifier, IUIPropertyMappingValue> mappings;
	private final @Nullable String rendererName;
	private final Map<String, IUIComponentArguments.IEmbedPrototype> embedArguments;

	private UIImmutableComponentEmbedPrototypeArguments(Map<? extends IIdentifier, ? extends IUIPropertyMappingValue> mappings,
	                                                    @Nullable @NonNls CharSequence rendererName,
	                                                    Map<? extends String, ? extends IUIComponentArguments.IEmbedPrototype> embedArguments) {
		this.mappings = ImmutableMap.copyOf(mappings);
		this.rendererName = Optional.ofNullable(rendererName).map(CharSequence::toString).orElse(null);
		this.embedArguments = ImmutableMap.copyOf(embedArguments);
	}

	public static UIImmutableComponentEmbedPrototypeArguments of(Map<? extends IIdentifier, ? extends IUIPropertyMappingValue> mappings,
	                                                             @Nullable @NonNls CharSequence rendererName,
	                                                             Map<? extends String, ? extends IUIComponentArguments.IEmbedPrototype> embedArguments) {
		return new UIImmutableComponentEmbedPrototypeArguments(mappings, rendererName, embedArguments);
	}

	@Override
	public @Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> getMappingsView() {
		return ImmutableMap.copyOf(getMappings());
	}

	@Override
	public Optional<? extends String> getRendererName() {
		return Optional.ofNullable(rendererName);
	}

	@Override
	public @Immutable Map<String, ? extends IUIComponentArguments.IEmbedPrototype> getEmbedArgumentsView() {
		return ImmutableMap.copyOf(getEmbedArguments());
	}

	protected Map<String, ? extends IUIComponentArguments.IEmbedPrototype> getEmbedArguments() {
		return embedArguments;
	}

	protected Map<IIdentifier, ? extends IUIPropertyMappingValue> getMappings() {
		return mappings;
	}
}
