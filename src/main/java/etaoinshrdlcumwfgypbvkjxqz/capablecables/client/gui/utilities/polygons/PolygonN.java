package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Vector;

@SideOnly(Side.CLIENT)
public class PolygonN<T> extends Vector<T> {
    @SafeVarargs
    public PolygonN(T... e) { super(Arrays.asList(e)); }
}
