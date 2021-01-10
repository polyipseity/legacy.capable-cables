package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.io;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.software.MinecraftSoftwareGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IGraphicsDevice;

public class MinecraftUIGraphicsDevice
		implements IGraphicsDevice {
	@Override
	public AutoCloseableGraphics2D createGraphics() {
		return MinecraftSoftwareGraphics2D.createGraphics();
	}
}
