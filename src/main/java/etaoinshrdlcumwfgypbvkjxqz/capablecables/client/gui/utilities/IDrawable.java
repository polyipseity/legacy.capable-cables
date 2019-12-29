package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IDrawable<N extends Number, O extends Cloneable> extends Cloneable {
    void draw(Minecraft minecraft);
    Rectangle<N> specs();
    O clone();
}
