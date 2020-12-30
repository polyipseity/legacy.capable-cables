package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import org.jetbrains.annotations.NonNls;

import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.Function;

// TODO messy, needs improvement
public interface IShapeAnchor {

	Optional<? extends IShapeDescriptorProvider> getTarget();

	EnumUISide getOriginSide();

	EnumUISide getTargetSide();

	double getBorderThickness();

	Optional<? extends IShapeAnchorSet> getContainer();

	void anchor(IShapeDescriptorProvider from)
			throws ConcurrentModificationException;

	void onContainerAdded(IShapeAnchorSet container);

	void onContainerRemoved();

	enum StaticHolder {
		;

		@SuppressWarnings("AutoBoxing")
		@NonNls
		private static final ImmutableMap<String, Function<@Nonnull IShapeAnchor, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull IShapeAnchor, @Nullable ?>>builder()
						.put("target", IShapeAnchor::getTarget)
						.put("originSide", IShapeAnchor::getOriginSide)
						.put("targetSide", IShapeAnchor::getTargetSide)
						.put("borderThickness", IShapeAnchor::getBorderThickness)
						.put("container", IShapeAnchor::getContainer)
						.build();

		public static ImmutableMap<String, Function<@Nonnull IShapeAnchor, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
	}
}
