package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs;

import javax.annotation.meta.When;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.Constants.GROUP;

@SuppressWarnings("SpellCheckingInspection")
public interface IImmutablizable<M> {
	@MethodOverrideable(group = GROUP, when = When.ALWAYS)
	<I extends M> I toImmutable();

	@MethodOverrideable(group = GROUP, when = When.MAYBE)
	default boolean isImmutable() { return getClass().getSimpleName().toLowerCase().contains("immutable"); }
}
