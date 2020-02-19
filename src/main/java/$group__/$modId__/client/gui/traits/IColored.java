package $group__.$modId__.client.gui.traits;

import $group__.$modId__.client.gui.components.GuiColor;
import $group__.$modId__.client.gui.components.GuiColorNull;
import $group__.$modId__.concurrent.IImmutablizable;
import $group__.$modId__.concurrent.IMutator;
import $group__.$modId__.concurrent.IMutatorImmutablizable;
import $group__.$modId__.logging.ILogging;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

@SideOnly(Side.CLIENT)
public interface IColored<T> {
	/* SECTION static methods */

	static <T, L extends Logger> IColored<T> of(@Nullable T color, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) { return color == null ? GuiColorNull.getInstance() : new GuiColor<>(color, mutator, logging); }


	/* SECTION getters & setters */

	@Nullable
	T getColor();

	boolean trySetColor(@Nullable T color);


	default Optional<? extends T> tryGetColor() { return Optional.ofNullable(getColor()); }

	default void setColor(@Nullable T color) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetColor(color)); }
}
