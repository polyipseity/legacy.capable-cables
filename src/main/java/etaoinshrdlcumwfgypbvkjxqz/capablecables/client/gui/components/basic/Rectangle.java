package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.components.basic;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle4;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Rectangle extends Gui implements IDrawable {
    protected Rectangle4.i rect;
    protected int color;

    public Rectangle(Rectangle4.i rect, int color) {
        this.rect = rect;
        this.color = color;
    }

    public void setRect(Rectangle4.i rect) { this.rect = rect; }
    public Rectangle4.i getRect() { return rect; }
    public void setColor(int color) { this.color = color; }
    public int getColor() { return color; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() { drawRect(rect.a(), rect.b(), rect.c(), rect.d(), color); }
}
