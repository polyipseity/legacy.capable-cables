package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum MinecraftOpenGLUtilities {
	;

	public static final int GL_MASK_ALL_BITS = 0xFFFFFFFF;

	public static long getWindowHandle() { return MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getHandle(); }

	public static int getGlMaskAllBits() {
		return GL_MASK_ALL_BITS;
	}
}
