package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.ICloneable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.IImmutablizable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.ISpecified;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References.Client.GAME;

@SideOnly(Side.CLIENT)
public interface IDrawable<N extends Number, D extends IDrawable<N, D>> extends ICloneable<D>, ISpecified<Rectangle<N, ?>>, IImmutablizable<D> {
	void draw(Minecraft game);

	default void draw() { draw(GAME); }
}
