package $group__.$modId__.traits;

import java.util.Optional;

/**
 * An object with a spec.
 * The information represented by the spec is to-be-defined by subclasses.
 *
 * @param <T> supertype of the spec of this object
 *
 * @author William So
 * @since 0.0.1.0
 */
@FunctionalInterface
public interface ISpec<T> {
	/* SECTION methods */

	/**
	 * Returns the spec of this object.
	 * <p>
	 * Overriding methods should {@code return} a spec that cannot change this object's
	 * state by any means through it.
	 *
	 * @return the spec of this object, optional
	 *
	 * @apiNote See the overriding methods for its meaning.
	 * @since 0.0.1.0
	 */
	Optional<T> spec();
}
