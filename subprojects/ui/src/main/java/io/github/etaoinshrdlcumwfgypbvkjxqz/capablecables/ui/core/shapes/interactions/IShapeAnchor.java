package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
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

		private static final ImmutableList<Function<? super IShapeAnchor, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IShapeAnchor::getTarget, IShapeAnchor::getOriginSide, IShapeAnchor::getTargetSide, IShapeAnchor::getBorderThickness, IShapeAnchor::getContainer);
		@NonNls
		private static final ImmutableMap<String, Function<? super IShapeAnchor, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("target", "originSide", "targetSide", "borderThickness", "container"), getObjectVariables()));

		public static ImmutableList<Function<? super IShapeAnchor, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super IShapeAnchor, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
