package $group__.utilities.minecraft.client;

import $group__.utilities.AssertionUtilities;
import net.minecraft.client.Minecraft;

public enum ClientUtilities {
	;

	public static Minecraft getMinecraftNonnull() { return AssertionUtilities.assertNonnull(Minecraft.getInstance()); }
}
