package $group__.$modId__.client.gui.themes;

import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.traits.IStructure;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public interface ITheme<T extends ITheme<T>> extends IStructure<T> {
	/* SECTION methods */

	default void drawRect(Rectangle<?, ?> rect, Color color) {
		Color pColor = new Color(GL11.glGetInteger(GL11.GL_CURRENT_COLOR), true);
		XY<?, ?> a = rect.a(), c = rect.c();
		Gui.drawRect(a.getX().intValue(), a.getY().intValue(), c.getX().intValue(), c.getY().intValue(), color.getRGB());
		GlStateManager.color(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), pColor.getAlpha());
	}

	default void drawModalRectWithCustomSizedTexture(Rectangle<?, ?> rect, Rectangle<?, ?> tex) {
		XY<?, ?> rectO = rect.getOffset(), rectS = rect.getSize(),
				texO = tex.getOffset(), texS = tex.getSize();
		Gui.drawModalRectWithCustomSizedTexture(rectO.getX().intValue(), rectO.getY().intValue(), texO.getX().floatValue(), texO.getY().floatValue(), rectS.getX().intValue(), rectS.getY().intValue(), texS.getX().floatValue(), texS.getY().floatValue());
	}

	default void drawScaledCustomSizeModalRect(Rectangle<?, ?> rect, Rectangle<?, ?> tex, XY<?, ?> tile) {
		XY<?, ?> rectO = rect.getOffset(), rectS = rect.getSize(),
				texO = tex.getOffset(), texS = tex.getSize();
		Gui.drawScaledCustomSizeModalRect(rectO.getX().intValue(), rectO.getY().intValue(), texO.getX().floatValue(), texO.getY().floatValue(), texS.getX().intValue(), texS.getY().intValue(), rectS.getX().intValue(), rectS.getY().intValue(), tile.getX().floatValue(), tile.getY().floatValue());
	}
}
