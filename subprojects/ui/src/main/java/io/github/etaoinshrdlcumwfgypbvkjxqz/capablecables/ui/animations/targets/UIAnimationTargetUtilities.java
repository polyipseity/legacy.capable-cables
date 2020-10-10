package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.targets;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.easings.EnumCommonUIAnimationEasing;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationEasing;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.DoubleConsumer;

public enum UIAnimationTargetUtilities {
	;

	public static IUIAnimationTarget compose(IUIAnimationTarget... targets) { return compose(ImmutableList.copyOf(targets)); }

	public static IUIAnimationTarget compose(Iterable<? extends IUIAnimationTarget> targets) {
		@Immutable List<IUIAnimationTarget> targetsCopy = ImmutableList.copyOf(targets);
		return progress ->
				targetsCopy.forEach(target ->
						target.accept(progress));
	}

	public static IUIAnimationTarget range(DoubleConsumer action, double from, double to) {
		return range(action, from, to, EnumCommonUIAnimationEasing.LINEAR);
	}

	public static IUIAnimationTarget range(DoubleConsumer action, double from, double to, IUIAnimationEasing easing) {
		return diff(action, from, to - from, easing);
	}

	public static IUIAnimationTarget diff(DoubleConsumer action, double from, double diff, IUIAnimationEasing easing) {
		return progress -> action.accept(from + diff * easing.applyAsDouble(progress));
	}

	public static IUIAnimationTarget diff(DoubleConsumer action, double from, double diff) {
		return diff(action, from, diff, EnumCommonUIAnimationEasing.LINEAR);
	}

	public enum ShapeDescriptors {
		;

		public static DoubleConsumer translateX(IShapeDescriptorProvider provider) {
			return transform(provider, 0, (previousX, x) -> AffineTransform.getTranslateInstance(x - previousX, 0));
		}

		public static DoubleConsumer transform(IShapeDescriptorProvider provider,
		                                       double initialValue,
		                                       BiFunction<? super Double, ? super Double, ? extends AffineTransform> valueDiffFunction) {
			return new ChangeAwareDoubleConsumer(initialValue, (previousValue, value) -> provider.modifyShape(() -> {
				provider.getShapeDescriptor()
						.transform(valueDiffFunction.apply(previousValue, value));
				return true;
			}));
		}

		public static DoubleConsumer translateY(IShapeDescriptorProvider provider) {
			return transform(provider, 0, (previousY, y) -> AffineTransform.getTranslateInstance(0, y - previousY));
		}

		public static DoubleConsumer rotate(IShapeDescriptorProvider provider) {
			return transform(provider, 0, (previousAngle, angle) -> AffineTransform.getRotateInstance(angle - previousAngle));
		}

		public static DoubleConsumer rotate(IShapeDescriptorProvider provider, Point2D anchor) {
			double anchorX = anchor.getX();
			double anchorY = anchor.getY();
			return transform(provider, 0, (previousAngle, angle) -> AffineTransform.getRotateInstance(angle - previousAngle, anchorX, anchorY));
		}

		public static DoubleConsumer scale(IShapeDescriptorProvider provider) {
			return transform(provider, 1, (previousScale, scale) -> {
				double scaleFactor = scale / previousScale;
				return AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
			});
		}

		public static DoubleConsumer scaleX(IShapeDescriptorProvider provider) {
			return transform(provider, 1, (previousScale, scale) -> AffineTransform.getScaleInstance(scale / previousScale, 1));
		}

		public static DoubleConsumer scaleY(IShapeDescriptorProvider provider) {
			return transform(provider, 1, (previousScale, scale) -> AffineTransform.getScaleInstance(1, scale / previousScale));
		}

		private static class ChangeAwareDoubleConsumer
				implements DoubleConsumer {
			private final BiConsumer<? super Double, ? super Double> delegate;
			private final Object acceptLockObject = new Object();
			private double previousValue;

			protected ChangeAwareDoubleConsumer(double initialValue, BiConsumer<? super Double, ? super Double> delegate) {
				this.previousValue = initialValue;
				this.delegate = delegate;
			}

			@Override
			public void accept(double value) {
				synchronized (getAcceptLockObject()) {
					double previous = getPreviousValue();
					setPreviousValue(value);
					getDelegate().accept(previous, value);
				}
			}

			protected Object getAcceptLockObject() { return acceptLockObject; }

			protected double getPreviousValue() { return previousValue; }

			protected BiConsumer<? super Double, ? super Double> getDelegate() { return delegate; }

			protected void setPreviousValue(double previousValue) { this.previousValue = previousValue; }
		}
	}
}
