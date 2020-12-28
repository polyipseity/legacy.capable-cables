package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui;

import net.minecraft.client.renderer.Vector4f;

public enum MinecraftVectorUtilities {
	;

	public static Vector4f copyVector4f(Vector4f instance) {
		return new Vector4f(instance.getX(), instance.getY(), instance.getZ(), instance.getW());
	}

	public static void setVector4f(Vector4f instance, Vector4f value) {
		instance.set(value.getX(), value.getY(), value.getZ(), value.getW());
	}
}
