package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.components.basic;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle4;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.concurrent.Immutable;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
@Immutable
public class ImmutableRectangle extends Rectangle {
    public ImmutableRectangle(Rectangle4.i rect, int color) { super(rect, color); }

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
}
