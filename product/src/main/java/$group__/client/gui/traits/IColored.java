package $group__.client.gui.traits;

import $group__.client.gui.components.GuiColor;
import $group__.client.gui.components.GuiColorNull;
import $group__.logging.ILogging;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.helpers.specific.Throwables;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public interface IColored<T> {
	/* SECTION static methods */

	static <T, L extends Logger> IColored<T> of(@Nullable T color, IMutatorImmutablizable<?, ?> mutator,
	                                            ILogging<Logger> logging) {
		return color == null ?
				GuiColorNull.getInstance() : new GuiColor<>(color, mutator, logging);
	}


	/* SECTION getters & setters */

	@Nullable
	T getColor();

	default void setColor(@Nullable T color) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetColor(color)); }

	boolean trySetColor(@Nullable T color);

	default Optional<? extends T> tryGetColor() { return Optional.ofNullable(getColor()); }
}
