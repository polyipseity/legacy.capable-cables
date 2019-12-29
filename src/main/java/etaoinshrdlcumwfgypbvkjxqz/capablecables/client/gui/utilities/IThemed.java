package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends Enum<T>> {
    void setTheme(T theme);
    T getTheme();

    enum Theme {
        NONE
    }
}
