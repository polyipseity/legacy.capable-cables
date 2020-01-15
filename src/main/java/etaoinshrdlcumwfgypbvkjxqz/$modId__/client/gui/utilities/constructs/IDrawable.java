package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.ISpecified;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStructure;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.References.Client.GAME;

@SideOnly(Side.CLIENT)
public interface IDrawable<N extends Number, T extends IDrawable<N, T>> extends IStructure<T>, ISpecified<Rectangle<N, ?>> {
	/* SECTION methods */

	void draw(Minecraft game);

	default void draw() { draw(GAME); }


	/** {@inheritDoc} */
	@Override
	String toString();

	/** {@inheritDoc} */
	@Override
	int hashCode();

	/** {@inheritDoc} */
	@Override
	boolean equals(Object o);

	/** {@inheritDoc} */
	@Override
	T clone();
}
