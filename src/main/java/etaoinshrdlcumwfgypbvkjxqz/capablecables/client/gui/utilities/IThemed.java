package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.XY;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.throw_;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends Enum<T>> {
    void setTheme(T theme);
    T getTheme();

    enum Theme {
        NONE(new ThemeHandler<Theme>() {
            /** {@inheritDoc} */
            @Override
            public Theme getTheme() { return Theme.NONE; }
        }),
        CUSTOM(null);

        @Nullable
        private ThemeHandler<Theme> handler;

        Theme(@Nullable ThemeHandler<Theme> handler) { this.handler = handler; }

        public void setHandler(@Nullable ThemeHandler<Theme> handler) {
            if (this != CUSTOM) throw rejectUnsupportedOperation();
            this.handler = handler;
        }
        public ThemeHandler<Theme> getHandler() {
            if (handler == null) throw throw_(new IllegalStateException(String.format("Set %s first", ThemeHandler.class.toGenericString())));
            return handler;
        }

        public interface ThemeHandler<T extends Enum<T>> {
            T getTheme();
            default <N extends Number> void drawRect(XY<N> offset, XY<N> size, Color color) { Gui.drawRect(offset.getX().intValue(), offset.getY().intValue(), size.getX().intValue(), size.getY().intValue(), color.getColor()); }
        }
    }
}
