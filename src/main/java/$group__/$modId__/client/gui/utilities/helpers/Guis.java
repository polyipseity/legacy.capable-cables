package $group__.$modId__.client.gui.utilities.helpers;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.helpers.Casts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.requireRunOnceOnly;
import static $group__.$modId__.utilities.variables.Globals.Client.CLIENT;
import static net.minecraft.client.renderer.GlStateManager.*;

@SuppressWarnings("SpellCheckingInspection")
@SideOnly(Side.CLIENT)
public enum Guis {
	/* MARK empty */;


	/* SECTION static variables */

	public static final TextureManager TEXTURE_MANAGER = CLIENT.getTextureManager();
	public static final GuiWithPublicZLevel GUI = new GuiWithPublicZLevel();


	/* SECTION static methods */

	public static void resetColor() { color(1F, 1F, 1F, 1F); }


	public static void bindTexture(ResourceLocation tex) { TEXTURE_MANAGER.bindTexture(tex); }

	public static void drawRect(IDrawable<?, ?> that, Rectangle<?, ?> rect, Color color) { getOrDefaultTheme(that).drawRect(rect, color); }

	public static IThemed.ITheme<?> getOrDefaultTheme(IDrawable<?, ?> parent) {
		return Casts.<IThemed<?>>castChecked(parent, castUncheckedUnboxedNonnull(IThemed.class)).map(t -> (IThemed.ITheme<?>) t.getTheme()).orElse(castUncheckedUnboxedNonnull(IThemed.EnumTheme.NONE));
	}

	public static void drawModalRectWithCustomSizedTexture(IDrawable<?, ?> that, Rectangle<?, ?> rect, Rectangle<?, ?> tex) { getOrDefaultTheme(that).drawModalRectWithCustomSizedTexture(rect, tex); }

	public static void drawScaledCustomSizeModalRect(IDrawable<?, ?> that, Rectangle<?, ?> rect, Rectangle<?, ?> tex, XY<?, ?> tile) { getOrDefaultTheme(that).drawScaledCustomSizeModalRect(rect, tex, tile); }


	public static void translateAndScaleFromTo(Rectangle<?, ?> from, Rectangle<?, ?> to) {
		XY<?, ?> fromO = from.getOffset(), fromS = from.getSize(),
				toO = to.getOffset(), toS = to.getSize();
		double scaleX = toS.getX().doubleValue() / fromS.getX().doubleValue(), scaleY = toS.getY().doubleValue() / fromS.getY().doubleValue();
		scale(scaleX, scaleY, 1D);
		translate(toO.getX().doubleValue() / scaleX - fromO.getX().doubleValue(), toO.getY().doubleValue() / scaleY - fromO.getY().doubleValue(), 0D);
	}


	/* SECTION static classes */

	private static final class GuiWithPublicZLevel extends Gui {
		/* SECTION constructors */

		private GuiWithPublicZLevel() { requireRunOnceOnly(); }


		/* SECTION methods */

		public float getZLevel() { return zLevel; }

		public void setZLevel(float zLevel) { this.zLevel = zLevel; }
	}
}
