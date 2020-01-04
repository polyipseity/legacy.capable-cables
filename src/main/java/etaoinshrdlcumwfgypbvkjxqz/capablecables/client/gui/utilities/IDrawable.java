package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IDrawable<N extends Number, D extends IDrawable<N, D>> extends Cloneable, IImmutablizable<D> {
    void draw(Minecraft minecraft);
    Rectangle<N> specs();
    D clone();
}
