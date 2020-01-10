package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IDrawableThemed<N extends Number, D extends IDrawable<N, D>, T extends Enum<T>> extends IDrawable<N, D>, IThemed<T> {}
