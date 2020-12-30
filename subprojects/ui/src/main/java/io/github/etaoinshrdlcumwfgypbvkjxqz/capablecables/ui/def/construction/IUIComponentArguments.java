package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface IUIComponentArguments
		extends INamed {
	static @NonNls String computeEmbedName(@NonNls CharSequence parentName, @NonNls Iterable<? extends CharSequence> embedNames) {
		return parentName.toString() + StaticHolder.getEmbedNameSeparator() + String.join(StaticHolder.getEmbedNameSeparator(), embedNames);
	}

	IShapeDescriptor<?> getShapeDescriptor();

	Optional<? extends String> getRendererName();

	@Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> getMappingsView();

	@Immutable Map<String, ? extends IEmbedPrototype> getEmbedArgumentsView();

	IUIComponentArguments withMappings(Map<? extends IIdentifier, ? extends IUIPropertyMappingValue> mappings);

	Optional<IUIComponentEmbedArguments> tryComputeEmbedArgument(CharSequence key, Function<@Nonnull ? super IUIComponentArguments, @Nonnull ? extends IUIComponent> constructor, IShapeDescriptor<?> shapeDescriptor);

	IUIComponentEmbedArguments computeEmbedArgument(CharSequence key, Function<@Nonnull ? super IUIComponentArguments, @Nonnull ? extends IUIComponent> constructor, IShapeDescriptor<?> shapeDescriptor);

	enum StaticHolder {
		;

		public static final char EMBED_NAME_SEPARATOR_CHARACTER = '.';
		public static final @NonNls String EMBED_NAME_SEPARATOR = "" + EMBED_NAME_SEPARATOR_CHARACTER;

		public static char getEmbedNameSeparatorCharacter() {
			return EMBED_NAME_SEPARATOR_CHARACTER;
		}

		public static String getEmbedNameSeparator() {
			return EMBED_NAME_SEPARATOR;
		}
	}

	interface IEmbedPrototype {
		@Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> getMappingsView();

		Optional<? extends String> getRendererName();

		@Immutable Map<String, ? extends IEmbedPrototype> getEmbedArgumentsView();
	}
}
