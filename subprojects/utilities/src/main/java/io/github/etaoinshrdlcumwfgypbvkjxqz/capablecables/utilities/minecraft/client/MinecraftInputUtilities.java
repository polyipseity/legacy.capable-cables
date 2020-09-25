package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.EnumMinecraftUICoordinateSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.CommonUICoordinateSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.CoordinateSystemUtilities;
import net.minecraft.client.MouseHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public enum MinecraftInputUtilities {
	;

	public static Point2D getScaledCursorPosition() {
		Point2D ret = getUnscaledCursorPosition();
		return CoordinateSystemUtilities.convertPoint(ret, ret,
				CommonUICoordinateSystem.INSTANCE, EnumMinecraftUICoordinateSystem.SCALED);
	}

	public static Point2D getUnscaledCursorPosition() {
		MouseHelper mouseHelper = MinecraftClientUtilities.getMinecraftNonnull().mouseHelper;
		return new Point2D.Double(mouseHelper.getMouseX(), mouseHelper.getMouseY());
	}
}
