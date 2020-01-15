package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.helpers.GuiHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castChecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends IThemed.ITheme<T>> {
	/* SECTION methods */

	void setTheme(T theme);

	T getTheme();


	/* SECTION static methods */

	static <T extends ITheme<T>> boolean isThemeInstanceOf(Class<T> t, Object o) { return o instanceof IThemed<?> && t.isAssignableFrom(((IThemed<?>) o).getTheme().getClass()); }

	@Nullable
	static <T extends ITheme<T>> IThemed<T> tryCastThemedTo(Object o) { return castChecked(o, t -> null); }


	/* SECTION static classes */

	interface ITheme<T extends ITheme<T>> {
		/* SECTION methods */

		default T getTheme() { return castUnchecked(this); }

		default void drawRect(Rectangle<?, ?> rect, Color color) { GuiHelper.drawRect(rect, color); }
	}


	enum EnumTheme implements ITheme<EnumTheme> {
		/* SECTION enums */
		NONE
	}
}
