package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.Optional;

public final class UIImmutableComponentArguments
		extends UIAbstractComponentArguments
		implements IUIComponentArguments {
	private final Map<IIdentifier, IUIPropertyMappingValue> mappings;
	private final IShapeDescriptor<?> shapeDescriptor;
	private final @Nullable String rendererName;
	private final Map<String, IEmbedPrototype> embedArguments;

	private UIImmutableComponentArguments(@Nullable @NonNls CharSequence name,
	                                      Map<? extends IIdentifier, ? extends IUIPropertyMappingValue> mappings,
	                                      IShapeDescriptor<?> shapeDescriptor,
	                                      @Nullable @NonNls CharSequence rendererName,
	                                      Map<? extends String, ? extends IEmbedPrototype> embedArguments) {
		super(name);
		this.mappings = ImmutableMap.copyOf(mappings);
		this.shapeDescriptor = shapeDescriptor;
		this.rendererName = Optional.ofNullable(rendererName).map(CharSequence::toString).orElse(null);
		this.embedArguments = ImmutableMap.copyOf(embedArguments);
	}

	@Override
	public @Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public Optional<? extends String> getRendererName() {
		return Optional.ofNullable(rendererName);
	}

	@Override
	public IUIComponentArguments withMappings(Map<? extends IIdentifier, ? extends IUIPropertyMappingValue> mappings) {
		return of(getName().orElse(null),
				mappings,
				getShapeDescriptor(),
				getRendererName().orElse(null),
				getEmbedArguments());
	}

	@Override
	public @Immutable Map<String, ? extends IEmbedPrototype> getEmbedArgumentsView() {
		return ImmutableMap.copyOf(getEmbedArguments());
	}

	public static UIImmutableComponentArguments of(@Nullable @NonNls CharSequence name,
	                                               Map<? extends IIdentifier, ? extends IUIPropertyMappingValue> mappings,
	                                               IShapeDescriptor<?> shapeDescriptor,
	                                               @Nullable @NonNls CharSequence rendererName,
	                                               Map<? extends String, ? extends IEmbedPrototype> embedArguments) {
		return new UIImmutableComponentArguments(name, mappings, shapeDescriptor, rendererName, embedArguments);
	}

	protected Map<String, ? extends IEmbedPrototype> getEmbedArguments() {
		return embedArguments;
	}

	protected Map<IIdentifier, ? extends IUIPropertyMappingValue> getMappings() {
		return mappings;
	}

}
