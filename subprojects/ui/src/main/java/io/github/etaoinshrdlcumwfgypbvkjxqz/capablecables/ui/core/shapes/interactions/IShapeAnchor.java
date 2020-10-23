package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions;

import com.google.common.collect.ImmutableMap;
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

		@NonNls
		private static final ImmutableMap<String, Function<IShapeAnchor, ?>> OBJECT_VARIABLES_MAP =
				ImmutableMap.<String, Function<IShapeAnchor, ?>>builder()
						.put("target", IShapeAnchor::getTarget)
						.put("originSide", IShapeAnchor::getOriginSide)
						.put("targetSide", IShapeAnchor::getTargetSide)
						.put("borderThickness", IShapeAnchor::getBorderThickness)
						.put("container", IShapeAnchor::getContainer)
						.build();

		public static ImmutableMap<String, Function<IShapeAnchor, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
