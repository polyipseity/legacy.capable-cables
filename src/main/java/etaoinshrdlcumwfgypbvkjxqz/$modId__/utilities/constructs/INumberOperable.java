package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

public interface INumberOperable<N extends Number & INumberOperable<N>> extends IOperable<N, Number>, Comparable<N> {
	/* SECTION methods */

	N sum(Number... o);

	N minus(Number o);
}
