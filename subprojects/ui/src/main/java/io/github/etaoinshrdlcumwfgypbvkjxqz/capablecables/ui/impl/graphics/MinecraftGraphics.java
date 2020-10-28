package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sun.java2d.SunGraphics2D;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public enum MinecraftGraphics {
	;

	private static final Color COLORLESS;
	private static final Graphics2D GRAPHICS;

	static {
		COLORLESS = new Color(0, 0, 0, 0);
		GRAPHICS = new SunGraphics2D(MinecraftSurfaceData.getInstance(), getColorless(), getColorless(), new Font(null)); // TODO fix font
	}

	public static Graphics2D getGraphics() {
		return GRAPHICS;
	}

	private static Color getColorless() {
		return COLORLESS;
	}
}
