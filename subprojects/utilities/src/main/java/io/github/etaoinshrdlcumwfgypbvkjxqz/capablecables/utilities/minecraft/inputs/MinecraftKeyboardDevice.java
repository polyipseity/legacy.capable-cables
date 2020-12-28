package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.inputs;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IKeyboardDevice;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MinecraftKeyboardDevice
		implements IKeyboardDevice {
	@Override
	public boolean isKeyDown(int key) {
		return InputMappings.isKeyDown(MinecraftOpenGLUtilities.getWindowHandle(), key);
	}
}
