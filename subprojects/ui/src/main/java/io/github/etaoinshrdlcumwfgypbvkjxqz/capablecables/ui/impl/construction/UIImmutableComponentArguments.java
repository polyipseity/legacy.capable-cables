package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.AbstractNamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.Optional;

public final class UIImmutableComponentArguments
		extends AbstractNamed
		implements IUIComponentArguments {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final IShapeDescriptor<?> shapeDescriptor;
	private final @Nullable String rendererName;

	private UIImmutableComponentArguments(@Nullable @NonNls CharSequence name,
	                                      Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings,
	                                      IShapeDescriptor<?> shapeDescriptor,
	                                      @Nullable @NonNls CharSequence rendererName) {
		super(name);
		this.mappings = ImmutableMap.copyOf(mappings);
		this.shapeDescriptor = shapeDescriptor;
		this.rendererName = Optional.ofNullable(rendererName).map(CharSequence::toString).orElse(null);
	}

	public static UIImmutableComponentArguments of(@Nullable @NonNls CharSequence name,
	                                               Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings,
	                                               IShapeDescriptor<?> shapeDescriptor,
	                                               @Nullable @NonNls CharSequence rendererName) {
		return new UIImmutableComponentArguments(name, mappings, shapeDescriptor, rendererName);
	}

	@Override
	public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public Optional<? extends String> getRendererName() {
		return Optional.ofNullable(rendererName);
	}
}
