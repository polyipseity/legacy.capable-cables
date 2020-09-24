package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftCoordinateUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
import net.minecraft.client.MouseHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum MinecraftInputUtilities {
	;

	public static ImmutablePoint2D getScaledCursorPosition() {
		return ImmutablePoint2D.of(MinecraftCoordinateUtilities.toScaledPoint(getNativeCursorPosition())); // TODO
	}

	public static ImmutablePoint2D getNativeCursorPosition() {
		MouseHelper mh = MinecraftClientUtilities.getMinecraftNonnull().mouseHelper;
		return ImmutablePoint2D.of(mh.getMouseX(), mh.getMouseY());
	}
}
