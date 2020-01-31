package $group__.$modId__.utilities.constructs.interfaces.basic;

import java.util.Optional;

/**
 * An object with a spec.
 * The information represented by the spec is to-be-defined by subclasses.
 *
 * @author William So
 * @param <T> supertype of the spec of this object
 * @since 0.0.1.0
 */
@FunctionalInterface
public interface ISpec<T> {
	/* SECTION methods */

	/**
	 * Returns the spec of this object.
	 * <p>
	 * Overriding methods should return a spec that is {@code new}, but equals spec
	 * given that the state of this object is not changed, each time the method is
	 * invoked.
	 *
	 * @apiNote See the overriding methods for its meaning.
	 *
	 * @return the spec of this object, optional
	 * @since 0.0.1.0
	 */
	Optional<? extends T> spec();
}
