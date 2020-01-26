package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import $group__.$modId__.utilities.constructs.interfaces.IStructure;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

		default void drawRect(Rectangle<?, ?> rect, Color color) { Guis.drawRect(rect, color); }


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
