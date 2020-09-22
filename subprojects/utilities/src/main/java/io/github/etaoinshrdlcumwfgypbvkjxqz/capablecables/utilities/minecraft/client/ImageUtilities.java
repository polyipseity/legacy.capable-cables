package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFWImage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@OnlyIn(Dist.CLIENT)
public enum ImageUtilities {
	;

	public static GLFWImage toGLFWImageCursor(NativeImage image) {
		ByteBuffer buf = ByteBuffer.allocateDirect(4 * image.getWidth() * image.getHeight()).order(ByteOrder.LITTLE_ENDIAN);
		for (int y = 0; y < image.getHeight(); ++y) {
			for (int x = 0; x < image.getWidth(); ++x)
				buf.putInt(image.getPixelRGBA(x, y));
		}
		buf.rewind();
		return GLFWImage.malloc().set(image.getWidth(), image.getHeight(), buf);
	}
}
