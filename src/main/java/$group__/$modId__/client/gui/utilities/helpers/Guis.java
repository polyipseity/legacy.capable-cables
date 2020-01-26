package $group__.$modId__.client.gui.utilities.helpers;

import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static $group__.$modId__.utilities.variables.Globals.Client.CLIENT;

@SuppressWarnings("SpellCheckingInspection")
@SideOnly(Side.CLIENT)
public enum Guis {
	/* MARK empty */ ;


	/* SECTION static variables */

	public static final TextureManager TEXTURE_MANAGER = CLIENT.getTextureManager();
	public static final GuiWithPublicZLevel GUI = new GuiWithPublicZLevel();


	/* SECTION static methods */

	public static void pushMatrix() { GlStateManager.pushMatrix(); }

	public static void popMatrix() { GlStateManager.popMatrix(); }


	public static void reset() {
		resetScale();
		resetColor();
	}


	public static void resetScale() { GlStateManager.scale(1, 1, 1); }

	public static void resetColor() { GlStateManager.resetColor(); }


	public static void bindTexture(ResourceLocation tex) { TEXTURE_MANAGER.bindTexture(tex); }


	public static void drawRect(Rectangle<?, ?> rect, Color color) {
		resetColor();
		XY<?, ?> a = rect.a(), c = rect.c();
		Gui.drawRect(a.getX().intValue(), a.getY().intValue(), c.getX().intValue(), c.getY().intValue(), color.getRGB());
	}

	public static void drawModalRectWithCustomSizedTexture(Rectangle<?, ?> rect, Rectangle<?, ?> tex) {
		XY<?, ?> rectO = rect.getOffset(), rectS = rect.getSize(),
				texO = tex.getOffset(), texS = tex.getSize();
		Gui.drawModalRectWithCustomSizedTexture(rectO.getX().intValue(), rectO.getY().intValue(), texO.getX().floatValue(), texO.getY().floatValue(), rectS.getX().intValue(), rectS.getY().intValue(), texS.getX().floatValue(), texS.getY().floatValue());
	}

	public static void drawScaledCustomSizeModalRect(Rectangle<?, ?> rect, Rectangle<?, ?> tex, XY<?, ?> tile) {
		XY<?, ?> rectO = rect.getOffset(), rectS = rect.getSize(),
				texO = tex.getOffset(), texS = tex.getSize();
		Gui.drawScaledCustomSizeModalRect(rectO.getX().intValue(), rectO.getY().intValue(), texO.getX().floatValue(), texO.getY().floatValue(), texS.getX().intValue(), texS.getY().intValue(), rectS.getX().intValue(), rectS.getY().intValue(), tile.getX().floatValue(), tile.getY().floatValue());
	}


	/* SECTION static classes */

	private static final class GuiWithPublicZLevel extends Gui {
		/* SECTION methods */

		public void setZLevel(float zLevel) { this.zLevel = zLevel; }

		public float getZLevel() { return zLevel; }
	}
}
