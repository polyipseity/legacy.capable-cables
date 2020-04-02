package $group__.$modId__.client.gui.themes;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public interface IThemedUser<T extends IThemed<TH>, TH extends ITheme<TH>> extends IThemed<TH> {
	/* SECTION getters & setters */

	T getThemed();

	default void setThemed(T themed) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetThemed(themed)); }

	boolean trySetThemed(T themed);

	default Optional<T> tryGetThemed() { return Optional.of(getThemed()); }

	@Nullable
	@Override
	default TH getTheme() { return getThemed().getTheme(); }

	@Override
	default boolean trySetTheme(@Nullable TH theme) { return getThemed().trySetTheme(theme); }
}
