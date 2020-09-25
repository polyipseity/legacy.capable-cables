package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.ICoordinateSystem;

import java.awt.geom.Point2D;

public enum EnumMinecraftUICoordinateSystem
		implements ICoordinateSystem {
	SCALED {
		@Override
		public <R extends Point2D> R specialize(Point2D source, R destination) {
			double scaleFactor = getScaleFactor();
			destination.setLocation(source.getX() / scaleFactor, source.getY() / scaleFactor);
			return destination;
		}

		@Override
		public <R extends Point2D> R generalize(Point2D source, R destination) {
			double scaleFactor = getScaleFactor();
			destination.setLocation(source.getX() * scaleFactor, source.getY() * scaleFactor);
			return destination;
		}
	},
	NATIVE {
		@Override
		public <R extends Point2D> R generalize(Point2D source, R destination) {
			destination.setLocation(source.getX(), getFrameBufferHeight() - source.getY());
			return destination;
		}

		@Override
		public <R extends Point2D> R specialize(Point2D source, R destination) {
			destination.setLocation(source.getX(), getFrameBufferHeight() - source.getY());
			return destination;
		}
	},
	;

	private static double getScaleFactor() { return MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getGuiScaleFactor(); }

	private static double getFrameBufferHeight() { return MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getFramebufferHeight(); }
}
