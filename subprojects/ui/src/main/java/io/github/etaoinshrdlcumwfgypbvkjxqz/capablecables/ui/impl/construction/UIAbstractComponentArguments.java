package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentEmbedArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.AbstractNamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import org.jetbrains.annotations.NonNls;

import java.util.Optional;
import java.util.function.Function;

public abstract class UIAbstractComponentArguments
		extends AbstractNamed
		implements IUIComponentArguments {
	public UIAbstractComponentArguments(@NonNls @Nullable CharSequence name) {
		super(name);
	}

	@Override
	public Optional<IUIComponentEmbedArguments> tryComputeEmbedArgument(CharSequence key, Function<@Nonnull ? super IUIComponentArguments, @Nonnull ? extends IUIComponent> constructor, IShapeDescriptor<?> shapeDescriptor) {
		String key1 = key.toString();
		return Optional2.of(
				() -> getName().orElse(null),
				() -> getEmbedArgumentsView().get(key1))
				.map((parentName, embed) -> {
					ImmutableSet<String> key2 = ImmutableSet.of(key1);
					return UIImmutableComponentEmbedArguments.of(constructor,
							UIImmutableComponentArguments.of(IUIComponentArguments.computeEmbedName(parentName, key2),
									embed.getMappingsView(),
									shapeDescriptor,
									OptionalUtilities.<String>upcast(embed.getRendererName())
											.orElseGet(() ->
													getRendererName()
															.map(rendererName -> IUIComponentArguments.computeEmbedName(rendererName, key2))
															.orElse(null)),
									embed.getEmbedArgumentsView())
					);
				});
	}

	@Override
	public IUIComponentEmbedArguments computeEmbedArgument(CharSequence key, Function<@Nonnull ? super IUIComponentArguments, @Nonnull ? extends IUIComponent> constructor, IShapeDescriptor<?> shapeDescriptor) {
		String key1 = key.toString();
		Optional<? extends IEmbedPrototype> embed = Optional.ofNullable(getEmbedArgumentsView().get(key1));
		ImmutableSet<String> key2 = ImmutableSet.of(key1);
		return UIImmutableComponentEmbedArguments.of(constructor,
				UIImmutableComponentArguments.of(getName()
								.map(name -> IUIComponentArguments.computeEmbedName(name, key2))
								.orElse(null),
						embed.map(IEmbedPrototype::getMappingsView).orElseGet(ImmutableMap::of),
						shapeDescriptor,
						OptionalUtilities.<String>upcast(embed.flatMap(IEmbedPrototype::getRendererName))
								.orElseGet(() ->
										getRendererName()
												.map(rendererName -> IUIComponentArguments.computeEmbedName(rendererName, key2))
												.orElse(null)),
						embed.map(IEmbedPrototype::getEmbedArgumentsView).orElseGet(ImmutableMap::of)));
	}
}
