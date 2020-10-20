package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.inputs;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftInputUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class MinecraftInputPointerDevice
		implements IInputPointerDevice {
	@Override
	public Point2D getPositionView() { return MinecraftInputUtilities.getScaledCursorPosition(); }
}
