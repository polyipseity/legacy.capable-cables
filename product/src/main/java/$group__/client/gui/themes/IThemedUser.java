package $group__.client.gui.themes;

import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IThemedUser<T extends IThemed<TH>, TH extends ITheme<TH>> extends IThemed<TH> {
	/* SECTION getters & setters */

	T getThemed();

	default void setThemed(T themed) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetThemed(themed)); }

	boolean trySetThemed(T themed);

	default Optional<T> tryGetThemed() { return Optional.of(getThemed()); }

	@Nullable
	@Override
	default TH getTheme() { return getThemed().getTheme(); }

	@Override
	default boolean trySetTheme(@Nullable TH theme) { return getThemed().trySetTheme(theme); }
}
