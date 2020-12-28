package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import net.minecraft.client.Minecraft;

public enum MinecraftClientUtilities {
	;

	public static Minecraft getMinecraftNonnull() { return AssertionUtilities.assertNonnull(Minecraft.getInstance()); }
}
