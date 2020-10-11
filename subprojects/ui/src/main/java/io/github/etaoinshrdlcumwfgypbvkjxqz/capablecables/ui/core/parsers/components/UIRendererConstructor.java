package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.lang.annotation.*;
import java.util.Map;
import java.util.Optional;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIRendererConstructor {
	interface IArguments {
		Optional<? extends String> getName();

		@Immutable
		Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView();

		Class<?> getContainerClass();
	}

	@Immutable
	class ImmutableArguments
			implements IArguments {
		@Nullable
		private final String name;
		private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
		private final Class<?> containerClass;

		public ImmutableArguments(@Nullable String name,
		                          Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings,
		                          Class<?> containerClass) {
			this.name = name;
			this.mappings = ImmutableMap.copyOf(mappings);
			this.containerClass = containerClass;
		}

		@Override
		public Optional<? extends String> getName() { return Optional.ofNullable(name); }

		@Override
		public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }

		@Override
		public Class<?> getContainerClass() { return containerClass; }
	}
}
