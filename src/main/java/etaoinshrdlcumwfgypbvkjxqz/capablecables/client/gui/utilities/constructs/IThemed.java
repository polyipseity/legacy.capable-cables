package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.helpers.GuiHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.throw_;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends Enum<T>> {
	void setTheme(T theme);

	T getTheme();


	static <T extends Enum<T>> boolean isThemeInstanceOf(Class<T> t, Object o) {
		return o instanceof IThemed<?> && ((IThemed<?>) o).getTheme().getClass() == t;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	static <T extends Enum<T>> IThemed<T> tryCastTo(Class<T> t, Object o) { return isThemeInstanceOf(t, o) ? (IThemed<T>) o : null; }


	enum EnumTheme {
		NONE(new ThemeHandler<EnumTheme>() {
			/** {@inheritDoc} */
			@Override
			public EnumTheme getTheme() { return EnumTheme.NONE; }
		}),
		CUSTOM(null);

		@Nullable
		private ThemeHandler<EnumTheme> handler;

		EnumTheme(@Nullable ThemeHandler<EnumTheme> handler) { this.handler = handler; }

		public void setHandler(@Nullable ThemeHandler<EnumTheme> handler) {
			if (this != CUSTOM) throw rejectUnsupportedOperation();
			this.handler = handler;
		}

		public ThemeHandler<EnumTheme> getHandler() {
			if (handler == null)
				throw throw_(new IllegalStateException("Set '" + ThemeHandler.class + "' first"));
			return handler;
		}


		public interface ThemeHandler<T extends Enum<T>> {
			T getTheme();

			default <N extends Number> void drawRect(Rectangle<?, ?> rect, Color color) { GuiHelper.drawRect(rect, color); }
		}
	}
}
