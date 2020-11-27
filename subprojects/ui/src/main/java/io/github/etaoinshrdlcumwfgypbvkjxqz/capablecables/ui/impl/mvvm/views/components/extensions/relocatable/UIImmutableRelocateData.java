package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.TupleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.Optional;

@Immutable
public final class UIImmutableRelocateData
		implements IUIComponentUserRelocatableExtension.IRelocateData {
	private final IIntersection<WeakReference<? extends IUIComponent>, WeakReference<? extends IUIReshapeExplicitly<?>>> targetComponent;
	private final Point2D previousPoint;

	private <TC extends IUIComponent & IUIReshapeExplicitly<?>> UIImmutableRelocateData(@Nullable TC targetComponent,
	                                                                                    Point2D previousPoint) {
		WeakReference<TC> targetComponentReference = new WeakReference<>(targetComponent);
		this.targetComponent = ImmutableIntersection.of(targetComponentReference, targetComponentReference);
		this.previousPoint = (Point2D) previousPoint.clone();
	}

	public static <TC extends IUIComponent & IUIReshapeExplicitly<?>> UIImmutableRelocateData of(@Nullable TC targetComponent,
	                                                                                             Point2D previousPoint) {
		return new UIImmutableRelocateData(targetComponent, previousPoint);
	}

	@Override
	public Optional<? extends IIntersection<? extends IUIComponent, ? extends IUIReshapeExplicitly<?>>> getTargetComponent() {
		return TupleUtilities.flattenWeakIntersection(targetComponent);
	}

	@Override
	public Point2D getPreviousPointView() { return (Point2D) getPreviousPoint().clone(); }

	protected Point2D getPreviousPoint() { return previousPoint; }

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public Optional<? extends Shape> handle(Point2D point) {
		return getTargetComponent()
				.map(ITuple2::getLeft)
				.map(IUIComponent::getShape)
				.map(shape -> {
					Rectangle2D result = shape.getBounds2D();
					Point2D previousPoint = getPreviousPoint();
					UIObjectUtilities.acceptRectangularShape(result,
							(x, y, width, height) -> result.setFrame(x + (point.getX() - previousPoint.getX()), y + (point.getY() - previousPoint.getY()),
									width, height));
					return result;
				});
	}
}
