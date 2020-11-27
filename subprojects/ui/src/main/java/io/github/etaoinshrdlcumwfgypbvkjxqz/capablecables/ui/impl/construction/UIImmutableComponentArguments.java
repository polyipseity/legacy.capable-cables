package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.Optional;

public final class UIImmutableComponentArguments
		extends UIAbstractComponentArguments
		implements IUIComponentArguments {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final IShapeDescriptor<?> shapeDescriptor;
	private final @Nullable String rendererName;
	private final Map<String, IEmbedPrototype> embedArguments;

	private UIImmutableComponentArguments(@Nullable @NonNls CharSequence name,
	                                      Map<? extends INamespacePrefixedString, ? extends IUIPropertyMappingValue> mappings,
	                                      IShapeDescriptor<?> shapeDescriptor,
	                                      @Nullable @NonNls CharSequence rendererName,
	                                      Map<? extends String, ? extends IEmbedPrototype> embedArguments) {
		super(name);
		this.mappings = ImmutableMap.copyOf(mappings);
		this.shapeDescriptor = shapeDescriptor;
		this.rendererName = Optional.ofNullable(rendererName).map(CharSequence::toString).orElse(null);
		this.embedArguments = ImmutableMap.copyOf(embedArguments);
	}

	public static UIImmutableComponentArguments of(@Nullable @NonNls CharSequence name,
	                                               Map<? extends INamespacePrefixedString, ? extends IUIPropertyMappingValue> mappings,
	                                               IShapeDescriptor<?> shapeDescriptor,
	                                               @Nullable @NonNls CharSequence rendererName,
	                                               Map<? extends String, ? extends IEmbedPrototype> embedArguments) {
		return new UIImmutableComponentArguments(name, mappings, shapeDescriptor, rendererName, embedArguments);
	}

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public Optional<? extends String> getRendererName() {
		return Optional.ofNullable(rendererName);
	}

	@Override
	public @Immutable Map<INamespacePrefixedString, ? extends IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@Override
	public @Immutable Map<String, ? extends IEmbedPrototype> getEmbedArgumentsView() {
		return ImmutableMap.copyOf(getEmbedArguments());
	}

	protected Map<String, ? extends IEmbedPrototype> getEmbedArguments() {
		return embedArguments;
	}

	protected Map<INamespacePrefixedString, ? extends IUIPropertyMappingValue> getMappings() {
		return mappings;
	}

}
