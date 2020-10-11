package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.lang.annotation.*;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIViewComponentConstructor {
	@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
	interface IArguments {
		@Immutable
		Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView();
	}

	class ImmutableArguments
			implements IArguments {
		private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;

		public ImmutableArguments(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
			this.mappings = ImmutableMap.copyOf(mappings);
		}

		@Override
		public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }
	}
}
