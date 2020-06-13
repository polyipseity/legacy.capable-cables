package $group__.client.gui.traits;

import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IColoredUser<T extends IColored<C>, C> extends IColored<C> {
	T getColored();

	default void setColored(T colored) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetColored(colored)); }

	boolean trySetColored(T colored);

	default Optional<T> tryGetColored() { return Optional.of(getColored()); }

	@Nullable
	@Override
	default C getColor() { return getColored().getColor(); }

	@Override
	default boolean trySetColor(@Nullable C color) { return getColored().trySetColor(color); }
}
