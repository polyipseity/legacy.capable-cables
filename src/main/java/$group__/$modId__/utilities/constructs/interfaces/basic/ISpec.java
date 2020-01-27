package $group__.$modId__.utilities.constructs.interfaces.basic;

import java.util.Optional;

@FunctionalInterface
public interface ISpec<T> {
	/* SECTION methods */
	
	Optional<? extends T> spec();
}
