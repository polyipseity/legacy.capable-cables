package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IDrawableThemed<N extends Number, TH extends IThemed.ITheme<TH>, T extends IDrawable<N, T>> extends IDrawable<N, T>, IThemed<TH> {
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
	T clone();
}
