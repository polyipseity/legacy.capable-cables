package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ColorUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sun.java2d.SunGraphics2D;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public enum MinecraftGraphics {
	;

	private static Graphics2D graphics;

	static {
		graphics = initializeGraphics();
	}

	public static AutoCloseableGraphics2D createGraphics() {
		return AutoCloseableGraphics2D.of((Graphics2D) getGraphics().create());
	}

	private static Graphics2D getGraphics() {
		return graphics;
	}

	public static void draw() {
		MinecraftSurfaceData.getInstance().draw();
	}

	public static void clear() {
		MinecraftSurfaceData.getInstance().clear();
	}

	public static void recreateGraphics() {
		graphics.dispose();
		graphics = initializeGraphics();
	}

	private static Graphics2D initializeGraphics() {
		Graphics2D graphics = new SunGraphics2D(MinecraftSurfaceData.getInstance(), ColorUtilities.getColorless(), ColorUtilities.getColorless(), new Font(null));
		/* COMMENT
		The default blend func for Minecraft is in RenderSystem.defaultBlendFunc.

		source factor = source alpha
		destination factor = 1 - source alpha
		source alpha factor = 1
		destination alpha factor = 0
		 */
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
		return graphics;
	}
}
