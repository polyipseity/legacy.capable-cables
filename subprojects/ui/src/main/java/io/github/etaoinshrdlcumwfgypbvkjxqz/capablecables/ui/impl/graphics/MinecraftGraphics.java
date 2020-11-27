package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.TextUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ColorUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sun.java2d.SunGraphics2D;

import java.awt.*;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public enum MinecraftGraphics {
	;

	private static final Map<RenderingHints.Key, Object> DEFAULT_RENDERING_HINTS;
	private static Graphics2D graphics;

	static {
		// COMMENT optimize for speed, Minecraft does not require beautiful graphics, and these hints can be overridden anyway
		DEFAULT_RENDERING_HINTS = ImmutableMap.<RenderingHints.Key, Object>builder()
				.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED)
				.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)
				.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED)
				.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT)
				.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT)
				.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
				.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED)
				.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT)
				.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
				/* CODE RenderingHints.KEY_TEXT_LCD_CONTRAST */
				.build();
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
		Graphics2D graphics = new SunGraphics2D(MinecraftSurfaceData.getInstance(), ColorUtilities.getColorless(), ColorUtilities.getColorless(), TextUtilities.getDefaultFont());
		graphics.setRenderingHints(getDefaultRenderingHints());
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

	public static @Immutable Map<RenderingHints.Key, Object> getDefaultRenderingHints() {
		return ImmutableMap.copyOf(DEFAULT_RENDERING_HINTS);
	}
}
