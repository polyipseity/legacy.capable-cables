package $group__.client.gui.themes;

import $group__.logging.ILogging;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.helpers.specific.Throwables;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public interface IThemed<T extends ITheme<T>> {
	/* SECTION static methods */

	static <T extends ITheme<T>, L extends Logger> IThemed<T> of(@Nullable T theme,
	                                                             IMutatorImmutablizable<?, ?> mutator,
	                                                             ILogging<Logger> logging) {
		return theme == null ?
				GuiThemedNull.getInstance() : new GuiThemed<>(theme, mutator, logging);
	}


	/* SECTION getters & setters */

	@Nullable
	T getTheme();

	default void setTheme(@Nullable T theme) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetTheme(theme)); }

	boolean trySetTheme(@Nullable T theme);

	default Optional<? extends T> tryGetTheme() { return Optional.ofNullable(getTheme()); }
}
