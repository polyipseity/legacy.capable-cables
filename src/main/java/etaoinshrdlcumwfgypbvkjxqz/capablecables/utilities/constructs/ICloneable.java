package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs;

public interface ICloneable<T> extends Cloneable {
	/** {@inheritDoc} */
	T clone();

	default T copy() { return clone(); }
}
