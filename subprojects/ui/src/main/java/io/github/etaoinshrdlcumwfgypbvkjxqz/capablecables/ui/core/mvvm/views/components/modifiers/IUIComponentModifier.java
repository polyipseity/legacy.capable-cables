package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IUIComponentModifier {
	@SuppressWarnings("UnstableApiUsage")
	static <M> Stream<Map.Entry<IUIComponentModifier, M>> streamSpecificModifiersUnion(Iterable<? extends IUIComponentModifier> modifiers,
	                                                                                   Class<M> modifierClass) {
		return Streams.stream(modifiers).sequential()
				.filter(modifierClass::isInstance)
				.map(modifier -> Maps.immutableEntry(modifier, modifierClass.cast(modifier)));
	}

	static <M> void handleComponentModifiers(M component,
	                                         Iterable<? extends IUIComponentModifier> modifiers,
	                                         Class<M> modifierClass,
	                                         Consumer<? super M> action) {
		EnumModifyStage.handleModifiers(() -> action.accept(component),
				modifiers,
				modifiers2 -> streamSpecificModifiers(modifiers2, modifierClass)
						.forEachOrdered(action));
	}

	@SuppressWarnings("UnstableApiUsage")
	static <M> Stream<M> streamSpecificModifiers(Iterable<? extends IUIComponentModifier> modifiers,
	                                             Class<M> modifierClass) {
		return Streams.stream(modifiers).sequential()
				.filter(modifierClass::isInstance)
				.map(modifierClass::cast);
	}

	static <R, M, RInter> Optional<R> handleComponentModifiers(M component,
	                                                           Iterable<? extends IUIComponentModifier> modifiers,
	                                                           Class<M> modifierClass,
	                                                           Function<? super M, ? extends RInter> action,
	                                                           Function<? super Iterable<? extends RInter>, ? extends R> combiner) {
		// todo return
		return EnumModifyStage.handleModifiers(() -> action.apply(component),
				modifiers,
				modifiers2 -> streamSpecificModifiers(modifiers2, modifierClass)
						.map(action)
						.collect(Collectors.toList()),
				(self, pre, post) -> {
					assert pre != null;
					assert post != null;
					return combiner.apply(Iterables.concat(pre, Collections.singleton(self), post));
				});
	}

	Optional<? extends IUIComponent> getTargetComponent();

	void setTargetComponent(@Nullable IUIComponent targetComponent);

	void advanceModifyStage()
			throws IllegalStateException;

	void resetModifyStage();

	EnumModifyStage getModifyStage();
}
