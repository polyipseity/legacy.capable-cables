package $group__.$modId__.client.gui.traits;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public interface IColoredUser<T extends IColored<C>, C> extends IColored<C> {
	/* SECTION getters & setters */

	T getColored();

	default void setColored(T colored) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetColored(colored)); }

	boolean trySetColored(T colored);

	default Optional<T> tryGetColored() { return Optional.of(getColored()); }

	@Nullable
	@Override
	default C getColor() { return getColored().getColor(); }

	@Override
	default boolean trySetColor(@Nullable C color) { return getColored().trySetColor(color); }
}
