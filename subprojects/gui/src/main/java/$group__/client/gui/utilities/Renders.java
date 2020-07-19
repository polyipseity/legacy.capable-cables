package $group__.client.gui.utilities;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Rectangle2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum Renders {
	;

	public static void translateAndScaleFromTo(MatrixStack matrix, Rectangle2D from, Rectangle2D to) {
		float scaleX = (float) (to.getWidth() / from.getWidth()),
				scaleY = (float) (to.getHeight() / from.getHeight());
		matrix.translate(from.getX() * scaleX - to.getX(), from.getY() * scaleY - to.getY(), 0);
		matrix.scale(scaleX, scaleY, 1);
	}
}
