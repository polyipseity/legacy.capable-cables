package $group__.$modId__.client.gui.traits;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public interface IColored<T> {
	/* SECTION methods */

	default Optional<T> getColor() { return Optional.empty(); }

	default boolean setColor(@Nullable T theme) { return false; }
}
