package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IDrawableThemed<N extends Number, T extends IThemed.ITheme<T>, D extends IDrawable<N, D>> extends IDrawable<N, D>, IThemed<T> {
	/* SECTION methods */

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
	D clone();
}
