package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.IStructure;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends IThemed.ITheme<T>> {
	/* SECTION methods */

	void setTheme(T theme);

	T getTheme();


	/* SECTION static methods */

	static <T extends ITheme<T>> boolean isThemeInstanceOf(Class<T> t, Object o) { return o instanceof IThemed<?> && t.isAssignableFrom(((IThemed<?>) o).getTheme().getClass()); }


	/* SECTION static classes */

	interface ITheme<T extends ITheme<T>> extends IStructure<T> {
		/* SECTION methods */

		default T getTheme() { return castUncheckedUnboxedNonnull(this); }

		default void drawRect(Rectangle<?, ?> rect, Color color) {
			XY<?, ?> a = rect.a(), c = rect.c();
			float cr = GL11.glGetFloat(GL11.GL_RED), cg = GL11.glGetFloat(GL11.GL_GREEN), cb = GL11.glGetFloat(GL11.GL_BLUE), ca = GL11.glGetFloat(GL11.GL_ALPHA);
			Gui.drawRect(a.getX().intValue(), a.getY().intValue(), c.getX().intValue(), c.getY().intValue(), color.getRGB());
			GlStateManager.color(cr, cg, cb, ca);
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


		@Override
		String toString();

		@Override
		int hashCode();

		@Override
		boolean equals(Object o);
	}


	enum EnumTheme implements ITheme<EnumTheme> {
		/* SECTION enums */
		NONE;


		/* SECTION methods */

		@SuppressWarnings("EmptyMethod")
		@Override
		public String toString() { return super.toString(); }


		@Override
		public EnumTheme toImmutable() { return this; }

		@Override
		public boolean isImmutable() { return true; }
	}
}
