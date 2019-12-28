package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.components.basic;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IGuiThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle4;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RectangleThemed extends Rectangle implements IGuiThemed<IGuiThemed.Theme> {
    public RectangleThemed(Rectangle4.i rect, int color, Theme theme) {
        super(rect, color);
        this.theme = theme;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void draw();

    protected Theme theme;
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTheme(Theme theme) { this.theme = theme; }
    /**
     * {@inheritDoc}
     */
    @Override
    public Theme getTheme() { return theme; }
}
