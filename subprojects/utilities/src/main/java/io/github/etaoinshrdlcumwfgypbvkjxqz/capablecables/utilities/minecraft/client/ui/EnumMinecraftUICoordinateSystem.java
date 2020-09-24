package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.ICoordinateSystem;

import java.awt.geom.Point2D;

public enum EnumMinecraftUICoordinateSystem
		implements ICoordinateSystem {
	SCALED {
		@Override
		public Point2D specialize(Point2D point) {
			double scaleFactor = getScaleFactor();
			return ImmutablePoint2D.of(point.getX() / scaleFactor, point.getY() / scaleFactor);
		}

		@Override
		public Point2D generalize(Point2D point) {
			double scaleFactor = getScaleFactor();
			return ImmutablePoint2D.of(point.getX() * scaleFactor, point.getY() * scaleFactor);
		}
	},
	NATIVE {
		@Override
		public Point2D generalize(Point2D point) {
			return ImmutablePoint2D.of(point.getX(), getFrameBufferHeight() - point.getY());
		}

		@Override
		public Point2D specialize(Point2D point) {
			return ImmutablePoint2D.of(point.getX(), getFrameBufferHeight() - point.getY());
		}
	},
	;

	private static double getScaleFactor() { return MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getGuiScaleFactor(); }

	private static double getFrameBufferHeight() { return MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getFramebufferHeight(); }
}
