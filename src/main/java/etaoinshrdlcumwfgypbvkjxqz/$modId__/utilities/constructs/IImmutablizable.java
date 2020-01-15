package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import javax.annotation.meta.When;
import java.util.Locale;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

@SuppressWarnings("SpellCheckingInspection")
public interface IImmutablizable<T> {
	/* SECTION methods */

	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	T toImmutable();

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	default boolean isImmutable() { return getClass().getSimpleName().toLowerCase(Locale.ROOT).contains("immutable"); }
}
