package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.components.basic;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle4;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public abstract class ImmutableRectangleThemed extends RectangleThemed {
    public ImmutableRectangleThemed(Rectangle4.i rect, int color, Theme theme) { super(rect, color, theme); }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRect(Rectangle4.i rect) { throw rejectUnsupportedOperation(); }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(int color) { throw rejectUnsupportedOperation(); }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTheme(Theme theme) { throw rejectUnsupportedOperation(); }
}
