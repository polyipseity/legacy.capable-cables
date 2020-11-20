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

public final class UIImmutableComponentArguments
		extends AbstractNamed
		implements IUIComponentArguments {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final IShapeDescriptor<?> shapeDescriptor;

	public UIImmutableComponentArguments(@NonNls @Nullable CharSequence name,
	                                     Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings,
	                                     IShapeDescriptor<?> shapeDescriptor) {
		super(name);
		this.mappings = ImmutableMap.copyOf(mappings);
		this.shapeDescriptor = shapeDescriptor;
	}

	@Override
	public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }
}
