package $group__.client.gui.utilities;

import $group__.Globals;
import $group__.client.gui.coordinates.XY;
import $group__.client.gui.polygons.Rectangle;
import $group__.client.gui.themes.ITheme;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;

import static $group__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;
import static net.minecraft.client.renderer.GlStateManager.*;

@SuppressWarnings("SpellCheckingInspection")
@SideOnly(Side.CLIENT)
public enum Guis {
	/* MARK empty */;


	/* SECTION static variables */

	public static final TextureManager TEXTURE_MANAGER = Globals.Client.CLIENT.getTextureManager();
	public static final GuiWithPublicZLevel GUI = new GuiWithPublicZLevel();


	/* SECTION static methods */

	public static void resetColor() { color(1F, 1F, 1F, 1F); }


	public static void bindTexture(ResourceLocation tex) { TEXTURE_MANAGER.bindTexture(tex); }


	public static ITheme<?> getOrDefaultTheme(@Nullable ITheme<?> theme) {
		return theme == null ? ITheme.NULL :
				theme;
	}

	public static void drawRectangle(@Nullable ITheme<?> that, Rectangle<?, ?> rect, Color color) { getOrDefaultTheme(that).drawRect(rect, color); }

	public static void drawModalRectWithCustomSizedTexture(@Nullable ITheme<?> theme, Rectangle<?, ?> rect, Rectangle<
			?, ?> tex) { getOrDefaultTheme(theme).drawModalRectWithCustomSizedTexture(rect, tex); }

	public static void drawScaledCustomSizeModalRect(@Nullable ITheme<?> theme, Rectangle<?, ?> rect,
	                                                 Rectangle<?, ?> tex, XY<?, ?> tile) { getOrDefaultTheme(theme).drawScaledCustomSizeModalRect(rect, tex, tile); }

	public static void translateAndScaleFromTo(Rectangle<?, ?> from, Rectangle<?, ?> to) {
		XY<?, ?> fromO = from.getOffset(), fromS = from.getSize(),
				toO = to.getOffset(), toS = to.getSize();
		double scaleX = toS.getX().doubleValue() / fromS.getX().doubleValue(), scaleY =
				toS.getY().doubleValue() / fromS.getY().doubleValue();
		scale(scaleX, scaleY, 1D);
		translate(toO.getX().doubleValue() / scaleX - fromO.getX().doubleValue(),
				toO.getY().doubleValue() / scaleY - fromO.getY().doubleValue(), 0D);
	}


	/* SECTION static classes */

	private static final class GuiWithPublicZLevel extends Gui {
		/* SECTION constructors */

		private GuiWithPublicZLevel() { requireRunOnceOnly(Globals.LOGGER); }


		/* SECTION methods */

		public float getZLevel() { return zLevel; }

		public void setZLevel(float zLevel) { this.zLevel = zLevel; }
	}
}
