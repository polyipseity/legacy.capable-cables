package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableIntersection;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IUIComponentModifier {
	@SuppressWarnings("UnstableApiUsage")
	static <M> Stream<IIntersection<IUIComponentModifier, M>> streamSpecificModifiersIntersection(Iterator<? extends IUIComponentModifier> modifiers,
	                                                                                              Class<M> modifierClass) {
		return Streams.stream(modifiers)
				.filter(modifierClass::isInstance)
				.map(modifier -> ImmutableIntersection.of(modifier, modifierClass.cast(modifier)));
	}

	static <M> void handleComponentModifiers(M component,
	                                         Iterable<? extends IUIComponentModifier> modifiers,
	                                         Class<M> modifierClass,
	                                         Consumer<@Nonnull ? super M> action) {
		EnumModifyStage.handleModifiers(() -> action.accept(component),
				modifiers,
				modifiers2 -> streamSpecificModifiers(modifiers2.iterator(), modifierClass)
						.forEachOrdered(action));
	}

	@SuppressWarnings("UnstableApiUsage")
	static <M> Stream<M> streamSpecificModifiers(Iterator<? extends IUIComponentModifier> modifiers,
	                                             Class<M> modifierClass) {
		return Streams.stream(modifiers)
				.filter(modifierClass::isInstance)
				.map(modifierClass::cast);
	}

	static <R, M, RInter> Optional<R> handleComponentModifiers(M component,
	                                                           Iterable<? extends IUIComponentModifier> modifiers,
	                                                           Class<M> modifierClass,
	                                                           Function<@Nonnull ? super M, ? extends RInter> action,
	                                                           Function<@Nonnull ? super Iterable<? extends RInter>, ? extends R> combiner) {
		return EnumModifyStage.handleModifiers(() -> action.apply(component),
				modifiers,
				modifiers2 -> streamSpecificModifiers(modifiers2.iterator(), modifierClass)
						.map(action)
						.collect(Collectors.toList()) /* COMMENT need to allow for 'null' */,
				(self, pre, post) -> combiner.apply(Iterables.concat(
						pre,
						Collections.singleton(self), // COMMENT need to allow for 'null'
						post
				)));
	}

	Optional<? extends IUIComponent> getTargetComponent();

	void setTargetComponent(@Nullable IUIComponent targetComponent);

	void advanceModifyStage()
			throws IllegalStateException;

	void resetModifyStage();

	EnumModifyStage getModifyStage();
}
