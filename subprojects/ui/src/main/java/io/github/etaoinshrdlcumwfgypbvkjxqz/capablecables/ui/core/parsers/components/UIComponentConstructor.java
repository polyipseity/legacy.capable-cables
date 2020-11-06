package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.AbstractNamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import java.lang.annotation.*;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIComponentConstructor {
	interface IArguments
			extends INamed {
		@Immutable
		Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView();

		IShapeDescriptor<?> getShapeDescriptor();
	}

	class ImmutableArguments
			extends AbstractNamed
			implements IArguments {
		private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
		private final IShapeDescriptor<?> shapeDescriptor;

		public ImmutableArguments(@NonNls @Nullable CharSequence name,
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
}
