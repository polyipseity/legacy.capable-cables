package $group__.utilities.client.minecraft.specific;

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
		try (NativeImage i = image) {
			ByteBuffer buf = ByteBuffer.allocateDirect(4 * i.getWidth() * i.getHeight()).order(ByteOrder.LITTLE_ENDIAN);
			for (int y = 0; y < i.getHeight(); ++y) {
				for (int x = 0; x < i.getWidth(); ++x)
					buf.putInt(i.getPixelRGBA(x, y));
			}
			buf.rewind();
			return GLFWImage.malloc().set(i.getWidth(), i.getHeight(), buf);
		}
	}
}
