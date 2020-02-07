package $group__.$modId__.client.gui.traits;

import $group__.$modId__.client.gui.themes.ITheme;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends ITheme<T>> {
	/* SECTION methods */

	default Optional<T> getTheme() { return Optional.empty(); }

	default boolean setTheme(@Nullable T theme) { return false; }
}
