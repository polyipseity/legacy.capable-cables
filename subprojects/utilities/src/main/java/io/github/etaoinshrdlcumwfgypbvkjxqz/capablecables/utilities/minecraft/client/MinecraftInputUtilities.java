package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.EnumMinecraftUICoordinateSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.CommonUICoordinateSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.CoordinateSystemUtilities;
import net.minecraft.client.MouseHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum MinecraftInputUtilities {
	;

	public static ImmutablePoint2D getScaledCursorPosition() {
		return CoordinateSystemUtilities.convertPoint(getUnscaledCursorPosition(),
				CommonUICoordinateSystem.INSTANCE, EnumMinecraftUICoordinateSystem.SCALED);
	}

	public static ImmutablePoint2D getUnscaledCursorPosition() {
		MouseHelper mouseHelper = MinecraftClientUtilities.getMinecraftNonnull().mouseHelper;
		return ImmutablePoint2D.of(mouseHelper.getMouseX(), mouseHelper.getMouseY());
	}
}
