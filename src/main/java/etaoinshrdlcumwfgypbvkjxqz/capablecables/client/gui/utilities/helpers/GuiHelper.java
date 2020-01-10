package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.helpers;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References.Client.GAME;

@SideOnly(Side.CLIENT)
public enum GuiHelper {
	;
	public static void resetColor() { GlStateManager.resetColor(); }


	public static void bindTexture(Minecraft game, ResourceLocation tex) { game.renderEngine.bindTexture(tex); }

	public static void bindTexture(ResourceLocation tex) { bindTexture(GAME, tex); }


	public static void drawRect(Rectangle<?, ?> rect, Color color) {
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
}
