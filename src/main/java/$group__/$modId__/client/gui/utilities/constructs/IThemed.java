package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import $group__.$modId__.utilities.constructs.interfaces.IStructure;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;

import static $group__.$modId__.utilities.helpers.Casts.castChecked;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends IThemed.ITheme<T>> {
	/* SECTION methods */

	void setTheme(T theme);

	T getTheme();


	/* SECTION static methods */

	static <T extends ITheme<T>> boolean isThemeInstanceOf(Class<T> t, Object o) { return o instanceof IThemed<?> && t.isAssignableFrom(((IThemed<?>) o).getTheme().getClass()); }

	@Nullable
	static <T extends ITheme<T>> IThemed<T> tryCastThemedTo(Object o) { try { return castChecked(o); } catch (ClassCastException e) { return null; } }


	/* SECTION static classes */

	interface ITheme<T extends ITheme<T>> extends IStructure<T> {
		/* SECTION methods */

		default T getTheme() { return castUnchecked(this); }

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
